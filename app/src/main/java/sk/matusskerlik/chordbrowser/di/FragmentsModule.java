/*
 * Copyright (c) 2019 Matúš Škerlík.
 * All rights reserved.
 */

package sk.matusskerlik.chordbrowser.di;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import sk.matusskerlik.chordbrowser.ui.fragments.ChordsGridFragment;
import sk.matusskerlik.chordbrowser.ui.fragments.LoadingFragment;

@Module
abstract class FragmentsModule {

    @FragmentScope
    @ContributesAndroidInjector(modules = {
            ViewModelModule.class,
    })
    abstract LoadingFragment contributeLoginInjector();

    @FragmentScope
    @ContributesAndroidInjector(modules = {
            ViewModelModule.class,
    })
    abstract ChordsGridFragment contributeChordsGridInjector();
}
