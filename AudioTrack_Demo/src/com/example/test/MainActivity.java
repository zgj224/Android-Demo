package com.example.test;

import android.app.Activity;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import java.io.File;
import java.io.FileInputStream;

public class MainActivity extends Activity implements OnClickListener {
  private Button btn_start, btn_stop;
  String TAG = "xxx-audio";
  private boolean flag;
  private MyRunnable myRunnable;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    btn_start = (Button) findViewById(R.id.start);
    btn_start.setOnClickListener(this);
    btn_stop = (Button) findViewById(R.id.stop);
    btn_stop.setOnClickListener(this);
  }

  @Override
  public void onClick(View v) {
    if (v == btn_start) {
      if (null != myRunnable) {
      	myRunnable.setRunnableFlag(false);
      }
      myRunnable = new MyRunnable();
      Thread thread = new Thread(myRunnable);
      thread.start();
      Log.d(TAG, "Start Music...");
    } else if (v == btn_stop) {
      if (null != myRunnable) {
      	myRunnable.setRunnableFlag(false);
      }
      Log.d(TAG, "Stop Music...");
    }
  }

  private void init(boolean flag, int mMinBufferSize, AudioTrack mAudioTrack) {
    int readCount = 0;
    Log.i(TAG, "isPlaying == true ");
    try {
      FileInputStream inputStream = new FileInputStream(new File("/sdcard/test_01.pcm"));
      byte[] mBuffer = new byte[2*mMinBufferSize];      
      if(flag == false){
	mAudioTrack.stop();
	// mAudioTrack.pause();
	// mAudioTrack.flush();	
      }
      else {
	Thread.sleep(50);
	mAudioTrack.play();
	while (flag && ((readCount = inputStream.read(mBuffer)) != -1)) {
	  int written = mAudioTrack.write(mBuffer, 0, readCount);
	  Log.i(TAG, "AudioTrack write " + written);
	}
        mAudioTrack.stop();
      }
      mAudioTrack.release();
      inputStream.close();
    } catch (Exception e) {
      Log.e(TAG, "Can't open /sdcard/test_01.pcm" + " " + e.toString());
    }
  }

  class MyRunnable implements Runnable {
    private boolean flag = true;
    int mMinBufferSize = AudioTrack.getMinBufferSize(16000, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT);
    AudioTrack audio_track = new AudioTrack(AudioManager.STREAM_MUSIC, 16000, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT, 2 * mMinBufferSize, AudioTrack.MODE_STREAM);
    public void setRunnableFlag(boolean flag) {
      this.flag = flag;
      if (audio_track.getPlayState() == AudioTrack.PLAYSTATE_PLAYING) {
	Log.d(TAG,"Release AudioTrack Object...");
	// audio_track.pause();
	// audio_track.flush();
	audio_track.stop();
        audio_track.release();
      }
    }
    @Override
    public void run() {
      init(flag, mMinBufferSize, audio_track);
    }
  }

}
