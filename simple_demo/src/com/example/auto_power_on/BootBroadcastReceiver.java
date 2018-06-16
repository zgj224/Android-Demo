package com.example.auto_power_on;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootBroadcastReceiver extends BroadcastReceiver {
    static final String ACTION = "android.intent.action.BOOT_COMPLETED";

    @Override
    public void onReceive(Context context, Intent intent) {
    	Log.d("xxx", "intent.getAction() ===== " + intent.getAction());
        if (intent.getAction().equals(ACTION)) {

            Intent mainActivityIntent = new Intent(context, MainActivity.class);
            Log.d("auto_xxx","开机自启动一个Activity");
            mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(mainActivityIntent);            
        }
    }
}
