package com.kitchee.app.helpeo.customservicerobot;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.iflytek.sunflower.FlowerCollector;
import com.kitchee.app.helpeo.R;
import com.kitchee.app.helpeo.appCommon.Config;
import com.kitchee.app.helpeo.bean.ChatMessage;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by kitchee on 2018/8/23.
 *
 */

public class AutoChatActivity extends AppCompatActivity implements AutoChatView {

    private static final String TAG = "AutoChatActivity";
    @BindView(R.id.left_btn)
    ImageView leftBtn;
    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.title_line)
    View titleLine;
    @BindView(R.id.chatRecyclerView)
    RecyclerView recycleView;
    @BindView(R.id.chat_btn_send)
    Button chatBtnSend;
    @BindView(R.id.chat_et_content)
    EditText chatEtContent;

    AutoChatPresenter presenter;
    ChatMessageAdapter adapter;
    List<ChatMessage> list = new ArrayList<>();
    @BindView(R.id.chat_btn_voice)
    Button chatBtnVoice;
    // 语音听写UI
    private RecognizerDialog mIatDialog;
    private Toast mToast;
    // 语音听写对象
    private SpeechRecognizer mIat;
    // 引擎类型
    private String mEngineType = SpeechConstant.TYPE_CLOUD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_chat);
        ButterKnife.bind(this);
        requestPermissions();
        ChatMessage chatMessage = new ChatMessage("客服小e", "你好，客服小e很高兴为你服务！", Config.MESSAGE_TYPE_RECEIVE, new Date());
        list.add(chatMessage);
        presenter = new AutoChatPresenterImpl(this);
        adapter = new ChatMessageAdapter(list, this, "", "");
        recycleView.setLayoutManager(new LinearLayoutManager(this));
        recycleView.setAdapter(adapter);
        recycleView.setItemAnimator(new DefaultItemAnimator());
        titleTv.setText("客服小e");

        mToast = Toast.makeText(this, "", Toast.LENGTH_LONG);


        // 初始化识别无UI识别对象
        // 使用SpeechRecognizer对象，可根据回调消息自定义界面；
        mIat = SpeechRecognizer.createRecognizer(this, mInitListener);
        Log.d("kitchee", "onCreate: mIat = "+ mIat);
        // 初始化听写Dialog，如果只使用有UI听写功能，无需创建SpeechRecognizer
        // 使用UI听写功能，请根据sdk文件目录下的notice.txt,放置布局文件和图片资源
        mIatDialog = new RecognizerDialog(this, mInitListener);


    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @OnClick({R.id.left_btn, R.id.chat_btn_send, R.id.chat_et_content})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.left_btn:
                onBackPressed();
                break;
            case R.id.chat_btn_send:
                Log.d("kitchee", "onViewClicked: 点击了发送");
                String message = chatEtContent.getText().toString().trim();
                if(!checkInputText(message)){
                    Toast.makeText(AutoChatActivity.this,"请输入你想知道的问题~",Toast.LENGTH_LONG).show();
                    return;
                }
                sendMessage(message);
                chatEtContent.setText("");
                break;
            case R.id.chat_et_content:
                break;
        }
    }

    private void sendMessage(String message) {
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setName("kitchee");
        chatMessage.setType(Config.MESSAGE_TYPE_SEND);
        chatMessage.setDate(new Date());
        chatMessage.setMessage(message);
        Log.d("kitchee", "onViewClicked: message = " + message);
        presenter.sendMessage(chatMessage);
    }

    private boolean checkInputText(String message) {
        return !TextUtils.isEmpty(message);
    }

    //------------------------更新view-------------------------------//

    @Override
    public void sendMsgUpdateView(ChatMessage chatMessage) {
        //发送问题信息后更新当前页面
        Log.d("kitchee", "sendMsgUpdateView: ---------------");
        list.add(chatMessage);
        adapter.updateData(list);
        chatEtContent.setText("");
        recycleView.scrollToPosition(list.size() - 1);
    }

    @Override
    public void receiveMsgUpdateView(ChatMessage chatMessage) {
        Log.d("kitchee", "receiveMsgUpdateView: --------------");
        if (chatMessage != null){
//            list.add(chatMessage);
//            adapter.updateData(list);
            adapter.addData(chatMessage);
            recycleView.scrollToPosition(list.size() -1);

        }
    }

    @Override
    public void showRecognizerDialog(RecognizerDialogListener listener) {
        Log.d(TAG, "showRecognizerDialog: mIat = "+ mIat);
        // 设置听写引擎
        mIat.setParameter(SpeechConstant.ENGINE_TYPE, mEngineType);
        // 设置返回结果格式
        mIat.setParameter(SpeechConstant.RESULT_TYPE, "json");
        mIatDialog.setListener(listener);
        mIatDialog.show();
    }

    @Override
    public void showRecognizeRecord(String record) {
        // 显示在输入框上，等待用户自己发送
//        chatEtContent.setText(record);
//        chatEtContent.setSelection(record.length());
        Log.d(TAG, "showRecognizeRecord: 语音发出-----------------");
        // 直接发送出去
        sendMessage(record);
        
    }

    @Override
    public void showToast(String message) {
        mToast.setText(message);
        mToast.show();
    }

    @OnClick(R.id.chat_btn_voice)
    public void onViewClicked() {
        Log.d("kitchee", "onViewClicked: 点击了语音识别");
        FlowerCollector.onEvent(AutoChatActivity.this, "iat_recognize");
        presenter.soundRecording(mIat);

    }

    /**
     * 初始化监听器。
     */
    private InitListener mInitListener = new InitListener() {

        @Override
        public void onInit(int code) {
            Log.d(TAG, "SpeechRecognizer init() code = " + code);
            if (code != ErrorCode.SUCCESS) {
                showTip("初始化失败，错误码：" + code);
            }
        }
    };

    private void showTip(final String str) {
        mToast.setText(str);
        mToast.show();
    }

    @Override
    protected void onResume() {
        // 开放统计 移动数据统计分析
        FlowerCollector.onResume(AutoChatActivity.this);
        FlowerCollector.onPageStart(TAG);
        super.onResume();
    }

    @Override
    protected void onPause() {
        // 开放统计 移动数据统计分析
        FlowerCollector.onPageEnd(TAG);
        FlowerCollector.onPause(AutoChatActivity.this);
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if( null != mIat ){
            // 停止时停止监听
            mIat.stopListening();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if( null != mIat ){
            // 退出时释放连接
            mIat.cancel();
            mIat.destroy();
        }
    }

    private void requestPermissions(){
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                int permission = ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if(permission!= PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this,new String[]
                            {Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    Manifest.permission.LOCATION_HARDWARE,Manifest.permission.READ_PHONE_STATE,
                                    Manifest.permission.WRITE_SETTINGS,Manifest.permission.READ_EXTERNAL_STORAGE,
                                    Manifest.permission.RECORD_AUDIO,Manifest.permission.READ_CONTACTS},0x0010);
                }

                if(permission != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this,new String[] {
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION},0x0010);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


}
