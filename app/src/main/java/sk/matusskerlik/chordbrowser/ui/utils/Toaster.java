/*
 * Copyright (c) 2019 Matúš Škerlík.
 * All rights reserved.
 */

package sk.matusskerlik.chordbrowser.ui.utils;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.widget.Toast;

public class Toaster {

    private Context context;

    public Toaster(Application context) {
        this.context = context;
    }

    public void showText(final String text) {

        Runnable mRunnable = new Runnable() {
            @Override
            public void run() {
                Toast toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP | Gravity.CENTER, 0, 192 + 64);
                toast.show();
            }
        };

        // always run on ui thread
        if (Looper.myLooper() == Looper.getMainLooper()) {
            mRunnable.run();
        } else {
            new Handler(Looper.getMainLooper()).post(mRunnable);
        }

    }
}
