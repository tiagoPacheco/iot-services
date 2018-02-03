/*
 * Copyright (c) 2017, CESAR.
 * All rights reserved.
 *
 * This software may be modified and distributed under the terms
 * of the BSD license. See the LICENSE file for details.
 *
 *
 */

package br.com.dts.services.util;

import android.os.CountDownTimer;
import android.util.Log;

public final class PoolingTimer {

    private PoolingListener listener;

    private static final long ONE_HOUR = 60000 * 60;
    private static final long FIVE_MINUTES = 60000 * 5;
    private static final long TWO_SECONDS = 1000 * 2;

    private CountDownTimer countDownTimer;

    private long periodPooling;

    public PoolingTimer(long intervalTick, PoolingListener listener) {
        this.listener = listener;
        this.periodPooling = intervalTick;
        countDownTimer = new Task(TWO_SECONDS, intervalTick);
    }

    public PoolingTimer(PoolingListener listener) {
        this(TWO_SECONDS, listener);
    }

    /**
     * Init the pooling
     */
    public void startPooling() {
        if (countDownTimer != null) {
            countDownTimer.cancel();

            // create a new CounterDown and start it
            countDownTimer = getTime();
            countDownTimer.start();
            Log.d("Diego", "Pooling started");
        }
    }

    /**
     * Stop the pooling
     */
    public void stopPooling() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    private Task getTime() {
        return new Task(TWO_SECONDS, periodPooling);
    }


    private class Task extends CountDownTimer {

        public Task(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long l) {
            if (listener != null) {
                Log.d("Diego", "Pooling Ticked");
                listener.onPoolingFinished();
            }
        }

        @Override
        public void onFinish() {
            // infinite loop
            Log.d("Diego", "Pooling Restarted");
            start();
        }
    }

    /**
     * Listener used to retrieve the result of pooling
     */
    public interface PoolingListener {
        public void onPoolingFinished();
    }
}
