package com.chenww.camera.ui;
import java.io.IOException;
import android.app.Activity;
import android.view.View;
import android.os.Bundle;
import android.util.Log;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.audiofx.AcousticEchoCanceler;
import android.media.audiofx.AutomaticGainControl;
import android.media.audiofx.NoiseSuppressor;
import android.media.MediaRecorder;
import android.view.InputDevice;
import android.content.Context;
import android.content.pm.PackageManager;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Test_AEC extends Activity implements OnClickListener{
  private final static int SAMPLING_RATE = 8000;
  public Button bt;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_camera);

    bt = (Button) findViewById(R.id.bt_show);
    bt.setOnClickListener(this);
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
    case R.id.bt_show:
      setAec();
      setNC();
      setAGC();
      break;
    }
  }


  //1.回声消除AEC
  public void setAec(){
    AudioRecord ar = getAudioRecord();
    AcousticEchoCanceler aec = AcousticEchoCanceler.create(ar.getAudioSessionId());
    //判断设备是否支持回声消除(AEC)
    boolean isAec = AcousticEchoCanceler.isAvailable();
    Log.d("Demo", "是否支持回声消除(AEC) ========== " + isAec);

    boolean isMic = hasMicrophone();
    Log.d("Demo", "是否支持麦克风 ========== " + isMic);

    if(isAec == true && hasMicrophone() == true){
      try {
	aec.setEnabled(true);//打开AEC
	Log.d("Demo", "AEC is Enable");
	aec.setEnabled(false);//关闭AEC
	Log.d("Demo", "AEC is Disable");
	// test passed
      } catch (IllegalStateException e) {
	Log.e("Demo","setEnabled() in wrong state");
      }
    }
  }

  //2.自动增益控制(AGC)
  public void setAGC(){
    AudioRecord ar = getAudioRecord();
    AutomaticGainControl agc = AutomaticGainControl.create(ar.getAudioSessionId());
    //判断设备是否支持自动增益控制(AEC)
    boolean isAvailable = AutomaticGainControl.isAvailable();
    Log.d("Demo", "是否支持自动增益控制(AGC) ========== " + isAvailable);

    if(isAvailable == true && hasMicrophone() == true){
      try {
	agc.setEnabled(true);
	Log.d("Demo", "AGC is Enable");

	agc.setEnabled(false);
	Log.d("Demo", "AGC is Disable");
      } catch (IllegalStateException e) {
	Log.e("Demo","setEnabled() in wrong state");
      } finally {
	agc.release();
	ar.release();
      }
    }
  }

  //3.噪声抑制器(NC)
  public void setNC(){
    boolean isAvailable = NoiseSuppressor.isAvailable();
    Log.d("Demo", "是否支持噪声抑制器(NC) ========== " + isAvailable);

    AudioRecord ar = getAudioRecord();
    NoiseSuppressor ns = NoiseSuppressor.create(ar.getAudioSessionId());

    if(isAvailable == true && hasMicrophone() == true){
      try {
	ns.setEnabled(true);
	Log.d("Demo", "AGC is Enable");

	ns.setEnabled(false);
	Log.d("Demo", "AGC is Disable");
      } catch (IllegalStateException e) {
	Log.d("Demo","setEnabled() in wrong state");
      } finally {
	ns.release();
	ar.release();
      }
    }
  }

  private AudioRecord getAudioRecord() {
    AudioRecord ar = null;
    try {
      ar = new AudioRecord(MediaRecorder.AudioSource.DEFAULT,
    			   SAMPLING_RATE,
    			   AudioFormat.CHANNEL_CONFIGURATION_MONO,
    			   AudioFormat.ENCODING_PCM_16BIT,
    			   AudioRecord.getMinBufferSize(SAMPLING_RATE,
    							AudioFormat.CHANNEL_CONFIGURATION_MONO,
    							AudioFormat.ENCODING_PCM_16BIT) * 10);
    } catch (IllegalArgumentException e) {
      Log.d("Demo","AudioRecord invalid parameter");
    }
    return ar;
  }

  private boolean hasMicrophone() {
    return this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_MICROPHONE);
  }
}
