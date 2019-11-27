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

import javax.inject.Provider;

import dagger.MapKey;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoMap;
import sk.matusskerlik.chordbrowser.model.repository.ChordRepository;
import sk.matusskerlik.chordbrowser.ui.fragments.ChordsGridViewModel;
import sk.matusskerlik.chordbrowser.ui.fragments.LoadingViewModel;

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
    public ViewModel chordsViewModel(ChordRepository chordRepository) {
        return new ChordsGridViewModel(chordRepository);
    }

    @Provides
    @IntoMap
    @FragmentScope
    @ViewModelKey(LoadingViewModel.class)
    public ViewModel loadingViewModel(ChordRepository chordRepository) {
        return new LoadingViewModel(chordRepository);
    }

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @MapKey
    public @interface ViewModelKey {
        Class<? extends ViewModel> value();
    }
}