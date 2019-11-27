package sk.matusskerlik.chordbrowser.di;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import sk.matusskerlik.chordbrowser.ui.fragments.ChordsGridFragment;

@Module
abstract class FragmentsModule {

    @FragmentScope
    @ContributesAndroidInjector(modules = {
            ViewModelModule.class,
    })
    abstract ChordsGridFragment contributeForgotPasswordInjector();
}
