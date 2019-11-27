/*
 * Copyright (c) 2019 Matúš Škerlík.
 * All rights reserved.
 */

package sk.matusskerlik.chordbrowser.di;

import android.app.Application;

import androidx.room.Room;

import dagger.Module;
import dagger.Provides;
import sk.matusskerlik.chordbrowser.model.database.ChordDatabase;

@Module
public class DatabaseModule {

    @Provides
    @ApplicationScope
    public ChordDatabase provideDatabase(Application application) {
        return Room.databaseBuilder(application, ChordDatabase.class, "chords.db").build();
    }
}
