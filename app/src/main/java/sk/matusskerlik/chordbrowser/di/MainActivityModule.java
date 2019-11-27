package sk.matusskerlik.chordbrowser.di;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import sk.matusskerlik.chordbrowser.MainActivity;

@Module
abstract class MainActivityModule {

    @ActivityScope
    @ContributesAndroidInjector(modules = {
            FragmentsModule.class,
            RetrofitModule.class
    })
    abstract MainActivity contributeActivityInjector();
}
