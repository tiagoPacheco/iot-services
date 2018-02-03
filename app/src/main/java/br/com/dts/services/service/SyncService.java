/*
 * Copyright (c) 2017, CESAR.
 * All rights reserved.
 *
 * This software may be modified and distributed under the terms
 * of the BSD license. See the LICENSE file for details.
 *
 *
 */

package br.com.dts.services.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.WorkerThread;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;

import br.com.dts.services.util.PoolingTimer;


public class SyncService extends Service {

    /**
     * Tag used only for logs
     */
    private static final String TAG = "Diego";

    /**
     * Action used to sync all data using a internal pooling until the action {@link #ACTION_STOP_SYNC_DATA}
     */
    public static final String ACTION_SYNC_DATA = "br.com.dts.services.service.ACTION_SYNC_DATA";

    /**
     * Action used to stop the service
     */
    public static final String ACTION_STOP_SYNC_DATA = "br.com.dts.services.service.ACTION_STOP_SYNC_DATA";

    /**
     * Action used to get the current status of service
     */
    public static final String ACTION_SYNC_STATUS = "br.com.dts.services.service.ACTION_SYNC_STATUS";

    /**
     * Action used to result the current status of service
     */
    public static final String RESULT_SYNC_STATUS = "br.com.dts.services.service.RESULT_SYNC_STATUS";

    /**
     * The constant EXTRA_SERVICE_STATUS.
     */
    public static final String EXTRA_SERVICE_STATUS = "extra_service_status";

    private PoolingTimer poolingTimer;

    private SyncStatus syncStatus = SyncStatus.IDLE;


    /**
     * Enum used to handle the status of service
     */
    public enum SyncStatus {
        /**
         * Idle sync status.
         */
        IDLE,
        /**
         * Busy sync status.
         */
        BUSY
    }

    public SyncService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // init the listener of pooling
        this.poolingTimer = new PoolingTimer(poolingListener);
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            Log.d(TAG, "Action - " + intent.getAction());
            final String action = intent.getAction();
            if (!TextUtils.isEmpty(action) && syncStatus != SyncStatus.BUSY) {
                if (action.equals(ACTION_SYNC_DATA)) {
                    syncAndStartPooling();
                }
            } else if (action.equals(ACTION_SYNC_STATUS)) {
                sendSyncStatus();
            } else if (action.equals(ACTION_STOP_SYNC_DATA)) {
                stopService();
            } else {
                Log.d(TAG, "The service is bussy");
            }
        }
        return Service.START_STICKY;
    }

    /**
     * Stop the current service and its dependencies
     */
    private void stopService() {
        // stop the pooling
        if (poolingTimer != null) {
            poolingTimer.stopPooling();
        }
        // kill the current service
        stopSelf();
    }

    private void syncAndStartPooling() {
        // Force a sync and reeschedule the last pooling
        if (poolingTimer != null) {
            poolingTimer.stopPooling();
            poolingTimer.startPooling();
        } else {
            this.poolingTimer = new PoolingTimer(poolingListener);
            poolingTimer.startPooling();
        }
        // Sync all data of devices
        syncData();
    }

    @WorkerThread
    private void syncData() {
        if (syncStatus != SyncStatus.BUSY) {
            syncStatus = SyncStatus.BUSY;

            //What would you like to do here
            syncStatus = SyncStatus.IDLE;
            Log.d(TAG, "Data to be synced");
        }
    }

    /**
     * Send the current status of service
     */
    private void sendSyncStatus() {
        Intent sendSyncStatus = new Intent(RESULT_SYNC_STATUS);
        sendSyncStatus.putExtra(EXTRA_SERVICE_STATUS, syncStatus);
        LocalBroadcastManager.getInstance(SyncService.this).sendBroadcast(sendSyncStatus);
    }

    ///////////////////////////////////////////////////////////////////////////
    // Listener
    ///////////////////////////////////////////////////////////////////////////
    private PoolingTimer.PoolingListener poolingListener = new PoolingTimer.PoolingListener() {
        @Override
        public void onPoolingFinished() {
            Log.d(TAG, "onPoolingFinished");
            syncData();
        }
    };
}
