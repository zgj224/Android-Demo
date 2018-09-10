package com.example.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Log;


public class MainActivity extends Activity implements OnClickListener {
  private static final String TAG = MainActivity.class.getSimpleName();
  private static final java.lang.String DESCRIPTOR = "sample.hello";
  private static final int FUNC_CALLFUNCTION = 1;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    findViewById(R.id.record).setOnClickListener(this);
  }

  @Override
  public void onClick(View v) {
    int id = v.getId();
    switch (id) {
    case R.id.record:
      testService();
      break;
     }
  }

  public void testService(){
    Log.i(TAG, "Client oncreate ");
    Parcel _data = Parcel.obtain();
    Parcel _reply = Parcel.obtain();
    IBinder b = ServiceManager.getService(DESCRIPTOR);
    try {
      _data.writeInterfaceToken(DESCRIPTOR);
      b.transact(FUNC_CALLFUNCTION, _data, _reply, 0);
      _reply.readException();
      _reply.readInt();
    } catch (RemoteException e) {
      e.printStackTrace();
    } finally {
      _reply.recycle();
      _data.recycle();
    }
  }

}
