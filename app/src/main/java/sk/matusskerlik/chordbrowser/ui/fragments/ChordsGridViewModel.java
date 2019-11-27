package sk.matusskerlik.chordbrowser.ui.fragments;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executor;

import javax.inject.Inject;

import retrofit2.Response;
import sk.matusskerlik.chordbrowser.model.Chord;
import sk.matusskerlik.chordbrowser.model.ChordGroup;
import sk.matusskerlik.chordbrowser.model.database.ChordDatabase;
import sk.matusskerlik.chordbrowser.model.webservice.ChordsWebService;

public class ChordsGridViewModel extends ViewModel {

    private ChordsWebService chordsWebService;
    private ChordDatabase chordDatabase;
    private Executor executor;

    @Inject
    public ChordsGridViewModel(ChordsWebService chordsWebService, ChordDatabase chordDatabase, Executor executor) {
        this.chordsWebService = chordsWebService;
        this.chordDatabase = chordDatabase;
        this.executor = executor;
    }

    public LiveData<List<ChordGroup>> getAllChordGroups() {

        final MutableLiveData<List<ChordGroup>> mData = new MutableLiveData<>();

        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    List<Chord> allChords = chordDatabase.chordDao().getAll();


                    if (allChords.size() > 0) { // if we have chords in database, load them
                        ChordGroup[] chordGroups = new ChordGroup[Chord.CHORD_KEY.values().length];

                        for (int i = 0; i < Chord.CHORD_KEY.values().length; i++) {
                            chordGroups[i] = new ChordGroup();
                            String chordGroupKey = Chord.CHORD_KEY.values()[i].getLabel();

                            for (Chord chord : allChords) {
                                if (chord.getKey().getLabel().equals(chordGroupKey))
                                    chordGroups[i].addChord(chord);
                            }
                        }
                        mData.postValue(Arrays.asList(chordGroups));
                    } else {  // else fetch from api
                        List<ChordGroup> chordGroups = new ArrayList<>();

                        for (Chord.CHORD_KEY key : Chord.CHORD_KEY.values()) {
                            Response<ChordGroup> response = chordsWebService
                                    .fetchChordsOfName(key.getLabel()).execute();
                            ChordGroup chordGroup = response.body();

                            assert chordGroup != null;

                            chordDatabase.chordDao().insertAll(chordGroup.getChords());
                            chordGroups.add(chordGroup);
                        }
                        mData.postValue(chordGroups);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    //TODO
                }
            }
        });

        return mData;
    }
}
