/*
 * Copyright (c) 2019 Matúš Škerlík.
 * All rights reserved.
 */

package sk.matusskerlik.chordbrowser;

import android.os.Bundle;

import androidx.navigation.Navigation;

import dagger.android.support.DaggerAppCompatActivity;
import sk.matusskerlik.chordbrowser.ui.fragments.LoadingFragment;

public class MainActivity extends DaggerAppCompatActivity implements LoadingFragment.LoadingFragmentCallbacks {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    public void dataEndLoading() {
        Navigation.findNavController(this, R.id.nav_host_fragment)
                .navigate(R.id.chordsGridFragment);
    }
}
