/*
 * Copyright (c) 2019 Matúš Škerlík.
 * All rights reserved.
 */

package sk.matusskerlik.chordbrowser.di;

import android.util.Log;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import dagger.Module;
import dagger.Provides;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
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
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(@NotNull Chain chain) throws IOException {
                        Request request = chain.request();

                        // try the request
                        Response response = chain.proceed(request);

                        int tryCount = 0;
                        while (!response.isSuccessful() && tryCount < 5) {

                            Log.d("intercept", "Request is not successful - " + tryCount);

                            tryCount++;

                            // retry the request
                            response = chain.proceed(request);
                        }

                        // otherwise just pass the original response on
                        return response;
                    }
                })
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
