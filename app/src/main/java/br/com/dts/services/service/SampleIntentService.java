package br.com.dts.services.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;


import java.util.logging.LogRecord;


public class SampleIntentService extends IntentService {

    public SampleIntentService() {
        super("SampleIntentService");
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            String message = msg.obj.toString();
            Toast.makeText(SampleIntentService.this, message, Toast.LENGTH_LONG).show();

        }
    };

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not implemented method");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        //It will run ia worker thread
        if (intent != null) {

            try {
                Thread.sleep(5000);
                Log.v("Diego", "Intent Service Runned");
                Message msg = new Message();
                msg.obj = "Rodou";
                mHandler.sendMessage(msg);
            } catch (InterruptedException e) {
                // Restore interrupt status.
                Thread.currentThread().interrupt();
            }

        }
    }


}
