package sk.matusskerlik.chordbrowser.model.webservice;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import sk.matusskerlik.chordbrowser.model.ChordGroup;

public interface ChordsWebService {

    @GET("?request=chords&type=json")
    Call<ChordGroup> fetchChordsOfName(@Query("chord") String key);
}
