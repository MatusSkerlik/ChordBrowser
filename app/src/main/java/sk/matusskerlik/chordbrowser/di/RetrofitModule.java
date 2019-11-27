/*
 * Copyright (c) 2019 Matúš Škerlík.
 * All rights reserved.
 */

package sk.matusskerlik.chordbrowser.di;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import sk.matusskerlik.chordbrowser.model.webservice.ChordsWebService;

@Module
public class RetrofitModule {

    @Provides
    @ActivityScope
    public Retrofit providesRetrofit() {

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new HttpLoggingInterceptor()
                        .setLevel(HttpLoggingInterceptor.Level.BODY)).build();

        return new Retrofit.Builder()
                .baseUrl("http://pargitaru.id.lv/api/")
                .client(client)
                .addConverterFactory(JacksonConverterFactory.create(
                        new ObjectMapper()
                                .configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true)
                                .configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, true)
                ))
                .build();
    }

    @Provides
    @ActivityScope
    public ChordsWebService providesChordsWebService(Retrofit retrofit) {
        return retrofit.create(ChordsWebService.class);
    }
}
