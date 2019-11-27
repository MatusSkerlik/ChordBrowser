package sk.matusskerlik.chordbrowser.model.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import sk.matusskerlik.chordbrowser.model.Chord;

@Dao
public interface ChordDao {

    @Query("SELECT * FROM chords")
    List<Chord> getAll();

    @Insert
    void insertAll(List<Chord> chords);
}
