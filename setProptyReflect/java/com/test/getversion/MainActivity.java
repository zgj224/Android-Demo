package com.test.getversion;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import java.lang.reflect.Method;

public class MainActivity extends Activity {
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    findViewById(R.id.btnGetVersion);    
  }

  public void getVersionClick(View v) {
    String value;
    //以下两个不允许修改：setprop ro.build.version.sdk 23 修改不成功
    //setProp("ro.build.version.sdk","20");//不允许修改，给paltform系统权限也不行
    //setProp("ro.build.version.release","5.1.2");//不允许修改，给paltform系统权限也不行
        
    setProp("wlan.driver.ath","3");//set修改成功
    value = getProp("wlan.driver.ath","");
    Log.e("xxx----","androidSDK版本号 = "+ value);

    value = getProp("ro.build.version.release","");//get:可以正常获取到
    Log.e("xxx----","android系统版本号 = "+ value);

    value = getProp("ro.build.version.sdk","");
    Log.e("xxx----","androidSDK版本号 = "+ value);
  }

  private String getProp(String key,String defaultValue){
    String value = defaultValue;
    try {
      Class<?> cla = Class.forName("android.os.SystemProperties"); 
      Method get = cla.getMethod("get",String.class,String.class);
      value = (String) get.invoke(cla, key,"unknow");
    } catch (Exception e) {
      e.printStackTrace();          
    }    
    return  value;
  }

  public static void setProp(String key,String value){
    try {    
      Class<?> cla = Class.forName("android.os.SystemProperties"); 
      Method set = cla.getMethod("set",String.class,String.class);
      Log.e("xxx----","key = " + key + " vlaue = "+ value);
      set.invoke(cla,key,value);
    }catch (Exception e) {
      e.printStackTrace();          
    }    
  }

}
