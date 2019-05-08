package com.winallgz.kitchee.myimageloader;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.StatFs;
import android.support.annotation.NonNull;
import android.util.Log;
import android.util.LruCache;
import android.widget.ImageView;

import com.jakewharton.disklrucache.DiskLruCache;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.NoSuchFileException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by kitchee on 2019/4/29.
 * Desc: 图片加载器
 */

public class ImageLoader {

    /**
     * 一个完整的图片加载器，应该是拥有三级缓存的机制
     */

    private static final String TAG = "ImageLoader";

    public static final int MESSAGE_POST_RESULT = 1;

    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();

    private static final int CORE_POOL_SIZE = CPU_COUNT + 1;

    private static final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;

    private static final long KEEP_ALIVE = 10L;

    private static final int TAG_KEY_URI = R.id.imageloader_uri;

    private static final long DISK_CACHE_SIZE = 1024 * 1024 * 50;

    private static final int DISK_CACHE_INDEX = 0;

    private static final ThreadFactory sThreadFactory = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);
        @Override
        public Thread newThread(@NonNull Runnable r) {
            return new Thread(r,"ImageLoader#"+ mCount.getAndIncrement());
        }
    };

    private static final Executor THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(CORE_POOL_SIZE,MAXIMUM_POOL_SIZE,KEEP_ALIVE, TimeUnit.SECONDS,new LinkedBlockingDeque<Runnable>(),sThreadFactory);



    //主线程中更新UI
    private Handler mMainHandler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
            //处理更新
            if (msg.what == MESSAGE_POST_RESULT){
                LoaderResult loaderResult = (LoaderResult) msg.obj;
                ImageView imageView = loaderResult.imageView;
                String uri = (String) imageView.getTag(TAG_KEY_URI);
                if(uri.equals(loaderResult.url)){
                    imageView.setImageBitmap(loaderResult.bitmap);
                }else {
                    Log.w(TAG, "handleMessage: set image bitmap, but url has changed , ignored" );
                }

            }
        }
    };

    private LruCache<String, Bitmap> mMemoryCache;
    private DiskLruCache mDiskLruCache;
    private Context mContext;
    private boolean mIsDiskLruCacheCreated = false;
    private ImageResizer mImageResize;
    private int IO_BUFFER_SIZE = 8 * 1024;

    private ImageLoader(Context context) {
        //在初始化时，创建内存缓存和磁盘缓存
        mContext = context.getApplicationContext();
        //当前进程分配的最大内存
        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        //LruCache缓存的总容量
        int cacheSize = maxMemory / 8;
        //内存缓存
        mMemoryCache = new LruCache<String, Bitmap>(cacheSize){
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                //重写sizeOf方法，用来计算缓存对象的大小
                return bitmap.getRowBytes() * bitmap.getHeight() / 1024;
            }
        };
        //磁盘缓存
        File diskCacheDir = getDiskCacheDir(mContext,"bitmap");
        if (!diskCacheDir.exists()){
            boolean makeSuccess = diskCacheDir.mkdirs();
            if (!makeSuccess){

            }
        }
        if (getUsableSpace(diskCacheDir) > DISK_CACHE_SIZE){
            try {
                mDiskLruCache = DiskLruCache.open(diskCacheDir,1,1,DISK_CACHE_SIZE);
                mIsDiskLruCacheCreated = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        mImageResize = new ImageResizer();
    }

    /**
     * load bitmap from memory cache or disk cache or network async,then bind imageView and bitmap
     * NOTE THAT: should run in UI Thread
     * @param uri
     * @param imageView
     */
    public void bindBitmap(String uri, ImageView imageView){
        Log.d(TAG, "bindBitmap: imageView.width = "+ (imageView !=null?imageView.getWidth():0));
        bindBitmap(uri,imageView,0,0);
    }

    private void bindBitmap(final String uri, final ImageView imageView, final int reqWidht, final int reqHeight) {
        imageView.setTag(TAG_KEY_URI,uri);
        Bitmap bitmap = loadBitmapFromMemoryCache(uri);
        if (bitmap != null){
            imageView.setImageBitmap(bitmap);
            Log.d(TAG, "bindBitmap: loadBitmapFromMemoryCache-->url = "+ uri);
            return;
        }

        //缓存中不存在，开启线程去从磁盘缓存或者网络上加载
        Runnable loadBitmapTask = new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = loadBitmap(uri,reqWidht,reqHeight);
                if (bitmap != null){
                    //切换回主线程更新UI
                    LoaderResult result = new LoaderResult(imageView,uri,bitmap);
                    mMainHandler.obtainMessage(MESSAGE_POST_RESULT,result).sendToTarget();

                }
            }
        };
        THREAD_POOL_EXECUTOR.execute(loadBitmapTask);
    }


    /**
     * load bitmap from memory cache or disk or network
     * @param uri
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public Bitmap loadBitmap(String uri,int reqWidth, int reqHeight){
        //先从内存缓存中获取
        Bitmap bitmap = loadBitmapFromMemoryCache(uri);
        if (bitmap != null){
            Log.d(TAG, "loadBitmap: loadBitmapFromMemoryCache-->url = "+ uri);
            return bitmap;
        }
        //从磁盘缓存中获取
        try {
            bitmap = loadBitmapFromDiskCache(uri,reqWidth,reqHeight);
            if (bitmap != null){
                Log.d(TAG, "loadBitmap: loadBitmapFromDiskCache-->url = "+ uri);
                return bitmap;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //从网络上下载到磁盘缓存中
        try {
            bitmap = loadBitmapFromHttp(uri,reqWidth,reqHeight);
            Log.d(TAG, "loadBitmap: loadBitmapFromHttp-->url = "+ uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //防止创建磁盘缓存失败从而导致加载不成功
        if (bitmap == null && !mIsDiskLruCacheCreated){
            Log.w(TAG, "loadBitmap: encounter error, DisLruCache is not created !");
            //直接从网络中加载Bitmap
            bitmap = downloadBitmapFromUrl(uri);
        }
        return bitmap;
    }

    private Bitmap loadBitmapFromMemoryCache(String uri) {
        String key = hashKeyFormUrl(uri);
        return getBitmapFromMemCache(key);
    }

    /**
     * 添加Bitmap到内存缓存中
     * @param key
     * @param bitmap
     */
    private void addBitmapToMemoryCache(String key, Bitmap bitmap){
        if (getBitmapFromMemCache(key) == null){
            mMemoryCache.put(key,bitmap);
        }
    }

    /**
     * 获取Bitmap从内存缓存中
     * @param key
     * @return
     */
    private Bitmap getBitmapFromMemCache(String key){
        return mMemoryCache.get(key);

    }

    /**
     * 从磁盘缓存中获取Bitmap
     * @param url
     * @param reqWidth
     * @param reqHeight
     * @return
     * @throws IOException
     */
    private Bitmap loadBitmapFromDiskCache(String url, int reqWidth, int reqHeight) throws IOException {
        if (Looper.myLooper() == Looper.getMainLooper()){
            Log.w(TAG, "loadBitmapFromDiskCache: load bitmap from UI Thread, it's not recommended !" );
        }
        if (mDiskLruCache == null){
            return null;
        }
        //磁盘缓存的读取需要通过Snapshot来完成，通过Snapshot可以得到磁盘缓存对象对应的FileInputStream,但是FileInputStream无法便捷的压缩，
        //所以通过FileDescriptor来加载压缩后的图片
        Bitmap bitmap = null;
        String key = hashKeyFormUrl(url);

        DiskLruCache.Snapshot snapshot = mDiskLruCache.get(key);
        if (snapshot != null){
            FileInputStream inputStream = (FileInputStream) snapshot.getInputStream(DISK_CACHE_INDEX);
            FileDescriptor fileDescriptor = inputStream.getFD();
            bitmap = mImageResize.decodeSampledBitmapFromFileDescriptor(fileDescriptor,reqWidth,reqHeight);
            if (bitmap != null){
                addBitmapToMemoryCache(key, bitmap);
            }
        }
        return bitmap;
    }

    /**
     * 从网络上获取Bitmap信息
     * @param url
     * @param reqWidth
     * @param reqHeight
     * @return
     * @throws IOException
     */
    private Bitmap loadBitmapFromHttp(String url,int reqWidth, int reqHeight) throws IOException {
        if (Looper.myLooper() == Looper.getMainLooper()){
            throw new RuntimeException("can't visit network from UI Thread.");
        }

        if (mDiskLruCache == null){
            return null;
        }
        //磁盘缓存的添加需要通过Editor来完成，Editor提供了commit和abort方法来提交和撤销对文件系统的写操作
        String key = hashKeyFormUrl(url);
        DiskLruCache.Editor mEditor = mDiskLruCache.edit(key);
        if (mEditor != null){
            OutputStream outputStream = mEditor.newOutputStream(DISK_CACHE_INDEX);
            if (downloadUrlToStream(url,outputStream)){
                //成功写入流
                mEditor.commit();
            }else {
                mEditor.abort();
            }
            mDiskLruCache.flush();
        }
        //bitmap从网络中写入到磁盘缓存中，获取Bitmap可以直接从磁盘缓存中读取了
        return loadBitmapFromDiskCache(url,reqWidth,reqHeight);
    }

    private boolean downloadUrlToStream(String urlStr, OutputStream outputStream) {
        HttpURLConnection urlConnection = null;
        BufferedOutputStream bos = null;
        BufferedInputStream bis = null;

        try {
            URL url = new URL(urlStr);
            urlConnection = (HttpURLConnection) url.openConnection();
            bos = new BufferedOutputStream(outputStream,IO_BUFFER_SIZE);
            bis = new BufferedInputStream(urlConnection.getInputStream(),IO_BUFFER_SIZE);
            int b;
            while ((b = bis.read()) != -1){
                bos.write(b);
            }
            return true;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null){
                urlConnection.disconnect();
            }
            CloseUtils.closeQuietly(bos);
            CloseUtils.closeQuietly(bis);
        }
        return false;
    }

    private Bitmap downloadBitmapFromUrl(String urlStr){
        Bitmap bitmap = null;
        HttpURLConnection urlConnection = null;
        BufferedInputStream in = null;

        try {
            URL url = new URL(urlStr);
            urlConnection = (HttpURLConnection) url.openConnection();
            in = new BufferedInputStream(urlConnection.getInputStream(),IO_BUFFER_SIZE);
            bitmap = BitmapFactory.decodeStream(in);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (urlConnection != null){
                urlConnection.disconnect();
            }
            CloseUtils.closeQuietly(in);
        }
        return bitmap;
    }

    /**
     * 图片URL经过MD5加密生成唯一的key值，避免了URL中可能含有非法字符问题
     * @param url 图片的URL
     * @return 唯一key值
     */
    private String hashKeyFormUrl(String url) {
        String cacheKey;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(url.getBytes());
            cacheKey = bytesToHexString(messageDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            cacheKey = String.valueOf(url.hashCode());
        }
        return cacheKey;
    }

    private String bytesToHexString(byte[] digest) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0 ; i < digest.length; i++){
            String hex = Integer.toHexString(0xFF & digest[i]);
            if (hex.length() == 1){
                sb.append("0");
            }
            sb.append(hex);
        }
        return sb.toString();
    }


    /**
     * 获取可用的空间
     * @param path
     * @return
     */
    private long getUsableSpace(File path) {
        //返回此抽象路径名指定的分区上可用于此虚拟机的字节数。若有可能，此方法将检查写权限和其他操作系统限制，因此与 getfreespace() 相比，此方法能更准确地估计可实际写入的新数据数。
        //API > 9
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD){
            return path.getUsableSpace();
        }

        StatFs statFs = new StatFs(path.getPath());
        return statFs.getBlockSize() * statFs.getAvailableBlocks();
    }

    /**
     * 获取磁盘缓存路径
     * @param context 上下文
     * @param uniqueName 文件夹名
     * @return 文件
     */
    private File getDiskCacheDir(Context context, String uniqueName) {
        //外部存储是否可用
        boolean externalStorageAvailable = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        String cachePath;
        if (externalStorageAvailable){
            // SDCard/Android/data/你的应用包名/cache/目录，一般存放临时缓存数据
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return new File(cachePath + File.separator+uniqueName);
    }

    public static ImageLoader build(Context context){
        return new ImageLoader(context);
    }

    private static class LoaderResult{
        ImageView imageView;
        String url;
        Bitmap bitmap;

        public LoaderResult(ImageView imageView, String url, Bitmap bitmap) {
            this.imageView = imageView;
            this.url = url;
            this.bitmap = bitmap;
        }
    }


}
