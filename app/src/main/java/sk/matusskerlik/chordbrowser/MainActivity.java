/*
 * Copyright (c) 2019 Matúš Škerlík.
 * All rights reserved.
 */

package sk.matusskerlik.chordbrowser;

import android.os.Bundle;

import dagger.android.support.DaggerAppCompatActivity;

public class MainActivity extends DaggerAppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
