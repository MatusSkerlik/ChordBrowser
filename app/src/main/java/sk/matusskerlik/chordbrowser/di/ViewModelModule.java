/*
 * Copyright (c) 2019 Matúš Škerlík.
 * All rights reserved.
 */

package sk.matusskerlik.chordbrowser.di;

import androidx.lifecycle.ViewModel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;
import java.util.concurrent.Executor;

import javax.inject.Provider;

import dagger.MapKey;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoMap;
import sk.matusskerlik.chordbrowser.model.database.ChordDatabase;
import sk.matusskerlik.chordbrowser.model.webservice.ChordsWebService;
import sk.matusskerlik.chordbrowser.ui.fragments.ChordsGridViewModel;

@Module
public class ViewModelModule {

    @Provides
    @FragmentScope
    public ViewModelFactory viewModelFactory(Map<Class<? extends ViewModel>, Provider<ViewModel>> providerMap) {
        return new ViewModelFactory(providerMap);
    }

    @Provides
    @IntoMap
    @FragmentScope
    @ViewModelKey(ChordsGridViewModel.class)
    public ViewModel chordsViewModel(ChordsWebService chordsWebService, ChordDatabase chordDatabase, Executor executor) {
        return new ChordsGridViewModel(chordsWebService, chordDatabase, executor);
    }

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @MapKey
    public @interface ViewModelKey {
        Class<? extends ViewModel> value();
    }
}