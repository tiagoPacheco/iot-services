//Este código veio do GIT github.com/nglauber
/*
Copyright (c) 2018 Nglauber - github.com/nglauber
        All rights reserved.


*/

package br.com.dts.services.ui;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import br.com.dts.services.R;
import br.com.dts.services.service.Mp3Binder;
import br.com.dts.services.service.Mp3Service;
import br.com.dts.services.service.Mp3ServiceImpl;

public class Mp3Activity extends AppCompatActivity implements ServiceConnection, SensorEventListener {

    private Mp3Service mMP3Service;
    private ProgressBar mPrgDuracao;
    private TextView mTxtMusica;
    private TextView mTxtDuracao;
    private String mMusica;
    private SensorManager mSensorManager;
    private Sensor mProximity;
    private static final int SENSOR_SENSITIVITY = 4;

    private Handler mHandler = new Handler();

    private Thread mThreadProgresso = new Thread() {
        public void run() {
            atualizarTela();
            if (mMP3Service.getTempoTotal() > mMP3Service.getTempoDecorrido()) {
                mHandler.postDelayed(this, 1000);
            }
        }
    };
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mp3);
        mPrgDuracao = (ProgressBar) findViewById(R.id.progressBar);
        mTxtMusica = (TextView) findViewById(R.id.txtMusica);
        mTxtDuracao = (TextView) findViewById(R.id.txtTempo);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mProximity = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        Intent intent = getIntent();
        Bundle b = intent.getExtras();

        if(b!=null)
        {
            mMusica =(String) b.get("musicPath");
            mTxtMusica.setText(mMusica);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent it = new Intent(this, Mp3ServiceImpl.class);
        startService(it);
        bindService(it, this, BIND_AUTO_CREATE);
        mSensorManager.registerListener(this, mProximity, SensorManager.SENSOR_DELAY_NORMAL);
    }
    @Override
    protected void onPause() {
        super.onPause();
        unbindService(this);
        mHandler.removeCallbacks(mThreadProgresso);
        mSensorManager.unregisterListener(this);
    }
    public void btnPlayClick(View v) {
        mHandler.removeCallbacks(mThreadProgresso);
        if (mMusica != null) {
            mMP3Service.play(mMusica);
            mHandler.post(mThreadProgresso);
        }
    }
    public void btnPauseClick(View v) {
        mMP3Service.pause();
        mHandler.removeCallbacks(mThreadProgresso);
    }
    public void btnStopClick(View v) {
        mMP3Service.stop();
        mHandler.removeCallbacks(mThreadProgresso);
        mPrgDuracao.setProgress(0);
        mTxtDuracao.setText(DateUtils.formatElapsedTime(0));
    }
    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        mMP3Service = ((Mp3Binder) service).getServico();
        mHandler.post(mThreadProgresso);
        mMP3Service.play(mMusica);
    }
    @Override
    public void onServiceDisconnected(ComponentName name) {
        mMP3Service = null;
    }

    private void atualizarTela() {
        mMusica = mMP3Service.getMusicaAtual();
        mTxtMusica.setText(mMusica);
        mPrgDuracao.setMax(mMP3Service.getTempoTotal());
        mPrgDuracao.setProgress(mMP3Service.getTempoDecorrido());
        mTxtDuracao.setText(
                DateUtils.formatElapsedTime(mMP3Service.getTempoDecorrido() / 1000));
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
            float sensorValue = event.values[0];
            AudioManager audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
            int volume;
            if(sensorValue > SENSOR_SENSITIVITY){
                volume = 0;
            }
            else if(sensorValue <= SENSOR_SENSITIVITY && sensorValue > 1){
                volume = 10;
            }
            else{
                volume = 20;
            }

            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}

