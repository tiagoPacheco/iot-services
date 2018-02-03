package br.com.dts.services.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class SampleService extends Service {
    public SampleService() {
    }

    @Override
    public IBinder onBind(Intent intent) {

        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }
}
