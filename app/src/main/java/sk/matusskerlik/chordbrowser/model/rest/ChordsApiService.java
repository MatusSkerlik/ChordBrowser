package sk.matusskerlik.chordbrowser.model.rest;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import sk.matusskerlik.chordbrowser.model.Chord;
import sk.matusskerlik.chordbrowser.model.ChordGroup;

public interface ChordsApiService {

    @GET("?request=chords&type=json")
    Call<ChordGroup> fetchChordsOfName(@Query("chord") String key);
}
