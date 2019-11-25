package sk.matusskerlik.chordbrowser.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

public class ChordGroup implements Serializable {

    @JsonProperty
    public List<Chord> chords;

    public ChordGroup() {
    }

    public List<Chord> getChords() {
        return chords;
    }

    public void setChords(@Nullable List<Chord> chords) {
        this.chords = chords;
    }

    @NonNull
    @Override
    public String toString() {
        if ((chords != null) && (chords.get(0) != null))
            return chords.get(0).getKey().getLabel();
        return "?";
    }
}
