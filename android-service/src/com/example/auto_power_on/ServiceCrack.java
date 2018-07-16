package com.example.auto_power_on;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class ServiceCrack extends Service{

	@Override
	public IBinder onBind(Intent arg0) {
	    return null;
	}

	@Override
	public void onCreate(){
           super.onCreate();
           Log.d("auto_xxx","Service 启动成功");
  }
}
