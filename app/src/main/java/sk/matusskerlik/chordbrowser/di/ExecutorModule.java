package sk.matusskerlik.chordbrowser.di;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import dagger.Module;
import dagger.Provides;

@Module
public class ExecutorModule {

    @Provides
    @ApplicationScope
    public Executor provideExecutor() {
        return Executors.newFixedThreadPool(4);
    }
}
