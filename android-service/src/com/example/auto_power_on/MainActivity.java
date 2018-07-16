package com.example.auto_power_on;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Context;
import android.content.Intent;
import static java.lang.Thread.sleep;

public class MainActivity extends Activity {

   @Override
   protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_main);

       //1.把Activity移除,资源并没有回收.
       finish();

       //2.启动Service
       Intent service = new Intent(this,ServiceCrack.class);
       startService(service);
       Log.d("auto_xxx","启动ServiceCrack服务....");

       //stopService(service);
    }
}
