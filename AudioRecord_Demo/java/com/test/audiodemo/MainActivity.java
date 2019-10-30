package com.test.audiodemo;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.app.Activity;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.text.SimpleDateFormat;

public class MainActivity extends Activity implements OnClickListener {
  public static final String TAG = "audiodemo";
  public Button mBtnControl;
  public boolean isRecording;
  public AudioRecord audioRecord;
  public SimpleDateFormat dateFormat;
  public String currentTimeStamp;
  public File dir_path;
  public File pcm_path;
  public String AUDIO_PATH = "/audio_pcm/";/* /sdcard/audio_pcm */
  public int minBufferSize;
  public int sampleRate = 16000;
  //单声道:CHANNEL_IN_MONO; 立体声：CHANNEL_IN_STEREO
  public int CHANNELS = AudioFormat.CHANNEL_IN_STEREO;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    mBtnControl = (Button) findViewById(R.id.btn_control);
    mBtnControl.setOnClickListener(this);
  }

  @Override
  public void onClick(View view) {
    switch (view.getId()) {
    case R.id.btn_control:
      Button button = (Button) view;
      if (button.getText().toString().equals(getString(R.string.start_record))) {
	button.setText(getString(R.string.stop_record));
	startRecord();
      } else {
	button.setText(getString(R.string.start_record));
	stopRecord();
      }
      break;
    default:
      break;
    }
  }

  public String getCurrentTimeStamp(){
    try {
      dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
      currentTimeStamp = dateFormat.format(new Date());
      Log.e(TAG,fun(new Exception())+ ", line = "+getLineNumber(new Exception()) + " currentTimeStamp = " + currentTimeStamp);
      return currentTimeStamp;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  StackTraceElement[] trace =new Exception().getStackTrace();
  public void startRecord(){    
    minBufferSize = AudioRecord.getMinBufferSize(sampleRate, CHANNELS, AudioFormat.ENCODING_PCM_16BIT);
    audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, sampleRate, CHANNELS, AudioFormat.ENCODING_PCM_16BIT, minBufferSize);
    Log.e(TAG, ((StackTraceElement)(new Throwable().getStackTrace()[0])).getFileName() + "()"+ ", Line = " + ((StackTraceElement)(new Throwable().getStackTrace()[0])).getLineNumber());

    final byte data[] = new byte[minBufferSize];
    //目录不存在，则创建目录
    dir_path = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + AUDIO_PATH);//位置：/sdcard/audio_pcm
    Log.e(TAG,fun(new Exception())+ ", line = "+getLineNumber(new Exception()) + " dir_path = " + dir_path);
    if(!dir_path.mkdirs()) {
      Log.e(TAG,fun(new Exception())+ ", line = "+getLineNumber(new Exception()) + " Create dir: " + dir_path);
      dir_path.mkdirs();
    }
    pcm_path = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + AUDIO_PATH + getCurrentTimeStamp() + ".pcm");

    try {
      Log.e(TAG,fun(new Exception())+ ", line = "+getLineNumber(new Exception()) + " pcm_path = " + pcm_path);
      pcm_path.createNewFile();
      Log.e(TAG,fun(new Exception())+ ", line = "+getLineNumber(new Exception()) + " pcm_path = " + pcm_path);
    } catch (IOException e) {
      throw new RuntimeException("Couldn't create file on SD card", e);
    }

    audioRecord.startRecording();
    isRecording = true;

    new Thread(new Runnable() {
	@Override
	public void run() {
	  FileOutputStream os = null;
	  try {
	    os = new FileOutputStream(pcm_path);
	  } catch (FileNotFoundException e) {
	    e.printStackTrace();
	  }

	  if (null != os) {
	    while (isRecording) {
	      int read = audioRecord.read(data, 0, minBufferSize);
	      // 如果读取音频数据没有出现错误，就将数据写入到文件
	      if (AudioRecord.ERROR_INVALID_OPERATION != read) {
		try {
		  os.write(data);
		} catch (IOException e) {
		  e.printStackTrace();
		}
	      }
	    }
	    try {
	      Log.e(TAG,fun(new Exception())+ ", line = "+getLineNumber(new Exception()) + ", run: close file output stream !");
	      os.close();
	    } catch (IOException e) {
	      e.printStackTrace();
	    }
	  }
	}
      }).start();
  }


  public void stopRecord() {
    isRecording = false;
    // 释放资源
    if (null != audioRecord) {
      audioRecord.stop();
      audioRecord.release();
      audioRecord = null;
    }
  }

  //获取行号
  public static int getLineNumber(Exception e){
    StackTraceElement[] trace =e.getStackTrace();
    if(trace==null||trace.length==0)
      return -1;
    return trace[0].getLineNumber();
  }

  //获取函数名
  public static String fun(Exception e) {
    StackTraceElement[] trace = e.getStackTrace();
    if(trace == null)
      return "";
    return trace[0].getMethodName()+"()";
  }
}
