package sk.matusskerlik.chordbrowser.di;

import android.app.Application;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    private Application mApplication;

    public AppModule(Application application) {
        mApplication = application;
    }

    @Provides
    @ApplicationScope
    public Application providesApplication() {
        return mApplication;
    }
}