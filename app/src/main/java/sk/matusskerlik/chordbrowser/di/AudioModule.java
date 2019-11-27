/*
 * Copyright (c) 2019 Matúš Škerlík.
 * All rights reserved.
 */

package sk.matusskerlik.chordbrowser.di;

import android.app.Application;

import dagger.Module;
import dagger.Provides;
import sk.matusskerlik.chordbrowser.audio.ChordPlayer;

@Module
public class AudioModule {

    @Provides
    @ActivityScope
    public ChordPlayer provideDatabase(Application application) {
        return new ChordPlayer(application);
    }
}
