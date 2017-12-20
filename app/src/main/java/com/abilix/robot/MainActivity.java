package com.abilix.robot;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.abilix.robot.apollo.ApolloBinder;
import com.abilix.robot.apollo.ApolloService;
import com.abilix.robot.event.CloudResult;
import com.abilix.robot.event.SpeechStatus;
import com.abilix.robot.iflytek.TTSBinder;
import com.abilix.robot.iflytek.TTSService;
import com.abilix.robot.iflytek.VTTBinder;
import com.abilix.robot.iflytek.VTTService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends Activity {

    @BindView(R.id.img_run)
    ImageView imageView;

    private int i = 1;

    private ServiceConnection serviceConnection_vtt;
    private ServiceConnection serviceConnection_mqtt;
    private ServiceConnection serviceConnection_tts;
    private VTTBinder vttBinder;
    private TTSBinder ttsBinder;
    private ApolloBinder apolloBinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_main);
        EventBus.getDefault().register(this);
        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        serviceConnection_vtt = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                vttBinder = (VTTBinder) service;
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                vttBinder = null;
            }
        };
        Intent intent0 = new Intent(this, VTTService.class);
        startService(intent0);
        bindService(intent0, serviceConnection_vtt, 0);
        serviceConnection_tts = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                ttsBinder = (TTSBinder) service;
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                ttsBinder = null;
            }
        };
        Intent intent1 = new Intent(this, TTSService.class);
        startService(intent1);
        bindService(intent1, serviceConnection_tts, 0);
        serviceConnection_mqtt = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                apolloBinder = (ApolloBinder) service;
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                apolloBinder = null;
            }
        };
        Intent intent2 = new Intent(this, ApolloService.class);
        startService(intent2);
        bindService(intent2, serviceConnection_mqtt, 0);
    }

    @OnClick(R.id.img_run)
    public void finishAll() {
//        finish();
        vttBinder.listenStart();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCloudResultReceivedEvent(CloudResult event) {
        Log.e("TTS", "event:" + event.getData());
        Toast.makeText(MainActivity.this, event.getData(), Toast.LENGTH_LONG).show();
        vttBinder.listenStop();
        ttsBinder.speak(event.getData(), true);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSpeechCompletedEvent(SpeechStatus event) {
        Log.e("voice", "event3:" + event.getStatus());
        if (TextUtils.equals("Completed", event.getStatus())) {
            vttBinder.listenStart();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
        unbindService(serviceConnection_tts);
        unbindService(serviceConnection_mqtt);
        unbindService(serviceConnection_vtt);
    }
}
