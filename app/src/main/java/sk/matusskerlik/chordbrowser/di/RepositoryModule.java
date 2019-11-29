/*
 * Copyright (c) 2019 Matúš Škerlík.
 * All rights reserved.
 */

package sk.matusskerlik.chordbrowser.di;

import java.util.concurrent.Executor;

import dagger.Module;
import dagger.Provides;
import sk.matusskerlik.chordbrowser.model.database.ChordDatabase;
import sk.matusskerlik.chordbrowser.model.repository.ChordRepository;
import sk.matusskerlik.chordbrowser.model.webservice.ChordsWebService;
import sk.matusskerlik.chordbrowser.ui.utils.Toaster;

@Module
public class RepositoryModule {

    @Provides
    @ActivityScope
    public ChordRepository providesUserRepository(ChordsWebService chordsWebService, ChordDatabase database, Executor executor, Toaster toaster) {
        return new ChordRepository(chordsWebService, database, executor, toaster);
    }
}
