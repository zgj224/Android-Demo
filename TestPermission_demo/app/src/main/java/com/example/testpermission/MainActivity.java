package com.example.testpermission;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static android.os.Build.VERSION.SDK_INT;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private Button btWrite;
    private int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 1;
    Class<?> ownerClass = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btWrite = (Button) findViewById(R.id.write);
        initEvent();
    }

    private void initEvent() {
        btWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                 && ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED
                 //&& ContextCompat.checkCallingPermission("android.permission.READ_PRIVILEGED_PHONE_STATE") != PackageManager.PERMISSION_GRANTED
                ){
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    Manifest.permission.READ_PHONE_STATE/*,"android.permission.READ_PRIVILEGED_PHONE_STATE"*/},
                            WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
                } else {
                    //To do
                    try {
                        //0.提升权限，调用Android的hide方法
                        raisUpPermission();//通过元反射，然后在设置setHiddenApiExemptions()，经测试Android10无效

                        TelephonyManager mTelephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);//获取"phone"服务类的对象
                        ownerClass = Class.forName("android.telephony.TelephonyManager");//获取包名
                        //1.获取4G信号
                        Field field = ownerClass.getField("NETWORK_CLASS_4_G");
                        int sinal_4G = (Integer) field.get(mTelephonyManager);
                        toast("获取4G信号 =====  " + sinal_4G);

                        //2.获取IMEI SV号码
                        Method method = (Method) ownerClass.getDeclaredMethod("getDeviceSoftwareVersion", int.class);
                        String imeiSV = (String) method.invoke(mTelephonyManager, 0);
                        toast("获取imei SV ======== " + imeiSV);

                        //3.Android8.0以下反射获取IMEI可以正常获取,Android9.0以上需要系统签名
                        //setHiddenApiExemptions.invoke(sVmRuntime, new Object[]{new String[]{"L"}});
                        //method = ownerClass.getDeclaredMethod("getDeviceId",int.class);
                        //method = ownerClass.getDeclaredMethod("getImei",int.class);
                        //String imei = (String) method.invoke(mTelephonyManager,0);


                        //4.获取Android ID，系统恢复出厂设置后随即改变
                        String android_ID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
                        toast("android_ID ======== " + android_ID);
                        //Log.e("1111", "imei1 = " + method.invoke(mTelephonyManager,0) + " imei2 = " + method.invoke(mTelephonyManager,1));
                    } catch (IllegalAccessException | NoSuchMethodException | NoSuchFieldException | ClassNotFoundException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == WRITE_EXTERNAL_STORAGE_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission Granted
                Toast.makeText(getApplicationContext(), "授权成功", Toast.LENGTH_SHORT).show();
                toast("授权成功");
            } else {
                // Permission Denied
                Toast.makeText(getApplicationContext(), "授权拒绝", Toast.LENGTH_SHORT).show();
                toast("授权拒绝");
            }
        }
    }

    private void toast(String msg) {
        if (TextUtils.isEmpty(msg)) {
            return;
        }
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        Log.i(TAG, msg);
    }

    void raisUpPermission() {
        Object sVmRuntime;
        Method setHiddenApiExemptions;
        if (SDK_INT >= Build.VERSION_CODES.P) {
            try {
                Method forName = Class.class.getDeclaredMethod("forName", String.class);
                Method getDeclaredMethod = Class.class.getDeclaredMethod("getDeclaredMethod", String.class, Class[].class);

                Class<?> vmRuntimeClass = (Class<?>) forName.invoke(null, "dalvik.system.VMRuntime");
                Method getRuntime = (Method) getDeclaredMethod.invoke(vmRuntimeClass, "getRuntime", null);
                setHiddenApiExemptions = (Method) getDeclaredMethod.invoke(vmRuntimeClass, "setHiddenApiExemptions", new Class[]{String[].class});
                sVmRuntime = getRuntime.invoke(null);
                setHiddenApiExemptions.invoke(sVmRuntime, new Object[]{new String[]{"L"}});
            } catch (Throwable e) {
                Log.e(TAG, "reflect bootstrap failed:", e);
            }
        }
    }
}