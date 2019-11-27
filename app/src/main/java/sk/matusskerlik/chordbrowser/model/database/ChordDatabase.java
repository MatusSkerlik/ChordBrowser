package sk.matusskerlik.chordbrowser.model.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import sk.matusskerlik.chordbrowser.model.Chord;

@Database(entities = {Chord.class}, version = 1)
public abstract class ChordDatabase extends RoomDatabase {
    public abstract ChordDao chordDao();
}
