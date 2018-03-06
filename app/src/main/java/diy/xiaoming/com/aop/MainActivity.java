package diy.xiaoming.com.aop;

import android.content.Context;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.Random;

import diy.xiaoming.com.aop.annotation.BeforeBehavior;
import diy.xiaoming.com.aop.annotation.BehaviorTrace;

public class MainActivity extends AppCompatActivity {

    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
    }

    @BehaviorTrace("摇一摇")
    public void mShake(View view){
        SystemClock.sleep(new Random().nextInt(2000));
//        Toast.makeText(mContext, "摇一摇", Toast.LENGTH_SHORT).show();
    }

    @BehaviorTrace("语音消息")
    @BeforeBehavior("语音消息")
    public void mAudio(View view){
        SystemClock.sleep(new Random().nextInt(2000));
    }

    @BeforeBehavior("视频消息")
    public void mVideo(View view){
        SystemClock.sleep(new Random().nextInt(2000));
    }

    public void saySomething(View view){
        SystemClock.sleep(new Random().nextInt(2000));
    }
}
