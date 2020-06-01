package com.test.audiodemo;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.app.Activity;
import android.hardware.led.V1_0.ILed;

public class MainActivity extends Activity implements OnClickListener {
  public Button mBtnControl;
  String TAG = "hidl_java";
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
	try{
	    ILed service = ILed.getService();
	    Log.e(TAG,"Led on begin...");
	    service.on();
	    Log.e(TAG,"Led on end...");
	}catch (Exception e){
	    e.printStackTrace();
	}
      break;
    default:
      break;
    }
  }
}
