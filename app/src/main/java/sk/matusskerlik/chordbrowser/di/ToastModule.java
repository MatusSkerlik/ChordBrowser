/*
 * Copyright (c) 2019 Matúš Škerlík.
 * All rights reserved.
 */

package sk.matusskerlik.chordbrowser.di;

import android.app.Application;

import dagger.Module;
import dagger.Provides;
import sk.matusskerlik.chordbrowser.ui.utils.Toaster;

@Module
public class ToastModule {

    @Provides
    @ActivityScope
    public Toaster provideToaster(Application application) {

        return new Toaster(application);
    }
}
