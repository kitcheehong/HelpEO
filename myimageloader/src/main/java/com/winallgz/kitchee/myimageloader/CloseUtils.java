package com.winallgz.kitchee.myimageloader;

import java.io.Closeable;
import java.io.IOException;

/**
 * Created by kitchee on 2019/2/20.
 * Desc: 关闭各类继承closeable的对象，简化代码
 */

public class CloseUtils {
    private CloseUtils(){}
    public static void closeQuietly(Closeable closeable){
        if (null != closeable){
            try {
                closeable.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
