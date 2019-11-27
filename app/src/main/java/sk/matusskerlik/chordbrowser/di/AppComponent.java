package sk.matusskerlik.chordbrowser.di;


import dagger.Component;
import dagger.android.AndroidInjectionModule;
import dagger.android.AndroidInjector;
import sk.matusskerlik.chordbrowser.BaseApplication;

@Component(modules = {
        AndroidInjectionModule.class,
        MainActivityModule.class,
        ExecutorModule.class,
        DatabaseModule.class,
        AppModule.class,
})
@ApplicationScope
public interface AppComponent extends AndroidInjector<BaseApplication> {

    void inject(BaseApplication application);
}
