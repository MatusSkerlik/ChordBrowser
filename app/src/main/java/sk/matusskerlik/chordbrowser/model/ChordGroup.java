package sk.matusskerlik.chordbrowser.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ChordGroup implements Serializable {

    @JsonProperty
    public List<Chord> chords;

    public ChordGroup() {
        chords = new ArrayList<>();
    }

    public List<Chord> getChords() {
        return chords;
    }

    public void setChords(@Nullable List<Chord> chords) {
        if (chords != null)
            this.chords = chords;
    }

    public void addChord(Chord chord) {
        chords.add(chord);
    }

    @NonNull
    @Override
    public String toString() {
        if ((chords != null) && (chords.size() > 0))
            return chords.get(0).getKey().getLabel();
        return "?";
    }
}
