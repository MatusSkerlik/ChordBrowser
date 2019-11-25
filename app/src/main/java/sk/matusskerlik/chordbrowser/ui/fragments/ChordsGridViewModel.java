package sk.matusskerlik.chordbrowser.ui.fragments;

import androidx.lifecycle.ViewModel;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import sk.matusskerlik.chordbrowser.model.Chord;
import sk.matusskerlik.chordbrowser.model.ChordGroup;
import sk.matusskerlik.chordbrowser.model.rest.ChordsApiService;

public class ChordsGridViewModel extends ViewModel {

    OkHttpClient client = new OkHttpClient.Builder()
            .addInterceptor(new HttpLoggingInterceptor()
                    .setLevel(HttpLoggingInterceptor.Level.BODY)).build();

    private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://pargitaru.id.lv/api/")
            .client(client)
            .addConverterFactory(JacksonConverterFactory.create(
                    new ObjectMapper()
                            .configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true)
                            .configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, true)
            ))
            .build();

    private ChordsApiService apiService = retrofit.create(ChordsApiService.class);


    public void fetchChordsOfName(Chord.CHORD_KEY chord_key, Callback<ChordGroup> callback){
        apiService.fetchChordsOfName(chord_key.getLabel()).enqueue(callback);
    }
}
