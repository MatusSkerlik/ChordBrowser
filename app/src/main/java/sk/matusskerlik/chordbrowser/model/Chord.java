package sk.matusskerlik.chordbrowser.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@Entity(tableName = "chords")
public class Chord implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @JsonIgnore
    public Integer id;

    public static class StringHelper {

        public enum GUITAR_STRING {
            E(1, "E2", 2, CHORD_KEY.E),
            A(2, "A2", 2, CHORD_KEY.A),
            D(3, "D3", 3, CHORD_KEY.D),
            G(4, "G3", 3, CHORD_KEY.G),
            B(5, "B3", 3, CHORD_KEY.B),
            e(6, "E4", 4, CHORD_KEY.E);

            int num;
            int octave;
            String label;
            CHORD_KEY key;

            GUITAR_STRING(int num, String label, int octave, CHORD_KEY key) {
                this.num = num;
                this.octave = octave;
                this.label = label;
                this.key = key;
            }

            public int getNum() {
                return num;
            }

            public int getOctave() {
                return octave;
            }

            public String getLabel() {
                return label;
            }

            public CHORD_KEY getKey() {
                return key;
            }
        }

        public static String getName(int string){

            for (GUITAR_STRING s: GUITAR_STRING.values()) {
                if (s.num == string)
                    return s.getLabel();
            }

            throw new IllegalArgumentException();
        }

        public static int getOctave(int string){

            for (GUITAR_STRING s: GUITAR_STRING.values()) {
                if (s.num == string)
                    return s.getOctave();
            }

            throw new IllegalArgumentException();
        }

        public static CHORD_KEY getKey(int string){

            for (GUITAR_STRING s: GUITAR_STRING.values()) {
                if (s.num == string)
                    return s.getKey();
            }

            throw new IllegalArgumentException();
        }
    }

    @ColumnInfo()
    @TypeConverters(ChordTypeConverter.class)
    @JsonProperty("modf")
    public CHORD_TYPE type;
    @ColumnInfo()
    @TypeConverters(ChordKeyConverter.class)
    @JsonProperty("chord")
    public CHORD_KEY key;
    @ColumnInfo()
    @JsonProperty("e")
    public String e;

    //minor, major, aug, dim, sus, add9, m6, m7, m9, maj7, maj9, mmaj7, -5, 11, 13, 5, 6, 6add9, 7, 7-5, 7maj5, 7sus4, 9
    public enum CHORD_TYPE {
        @JsonProperty("major")
        MAJOR("major"),
        @JsonProperty("minor")
        MINOR("minor"),
        @JsonProperty("aug")
        AUG("aug"),
        @JsonProperty("dim")
        DIM("dim"),
        @JsonProperty("sus")
        SUS("sus"),
        @JsonProperty("add9")
        ADD9("add9"),
        @JsonProperty("m6")
        M6("m6"),
        @JsonProperty("m7")
        M7("m7"),
        @JsonProperty("m9")
        M9("m9"),
        @JsonProperty("maj7")
        MAJ7("maj7"),
        @JsonProperty("maj9")
        MAJ9("maj9"),
        @JsonProperty("mmaj7")
        MMAJ7("mmaj7"),
        @JsonProperty("-5")
        MINUS5("-5"),
        @JsonProperty("11")
        ELEVEN("11"),
        @JsonProperty("13")
        THIRTEEN("13"),
        @JsonProperty("5")
        FIVE("11"),
        @JsonProperty("6")
        SIX("6"),
        @JsonProperty("6add9")
        SIX_ADD9("6add9"),
        @JsonProperty("7-5")
        SEVEN_MINUS_FIVE("7-5"),
        @JsonProperty("7maj5")
        SEVEN_MAJ5("7maj5"),
        @JsonProperty("7sus4")
        SEVEN_SUS4("7sus4"),
        @JsonProperty("9")
        NINE("9");


        private String label;

        CHORD_TYPE(String type) {
            this.label = type;
        }

        public String getLabel() {
            return label;
        }
    }

    public enum CHORD_KEY {
        @JsonProperty("A")
        A("A"),
        @JsonProperty("A#/Bb")
        As("A#"),
        @JsonProperty("B")
        B("B"),
        @JsonProperty("C")
        C("C"),
        @JsonProperty("C#/Db")
        Cs("C#"),
        @JsonProperty("D")
        D("D"),
        @JsonProperty("D#/Eb")
        Ds("D#"),
        @JsonProperty("E")
        E("E"),
        @JsonProperty("F")
        F("F"),
        @JsonProperty("F#/Gb")
        Fs("F#"),
        @JsonProperty("G")
        G("G"),
        @JsonProperty("G#/Ab")
        Gs("G#");

        private String label;

        CHORD_KEY(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }
    }

    @ColumnInfo()
    @JsonProperty("a")
    public String a;
    @ColumnInfo()
    @JsonProperty("d")
    public String d;
    @ColumnInfo()
    @JsonProperty("g")
    public String g;
    @ColumnInfo()
    @JsonProperty("b")
    public String b;
    @ColumnInfo()
    @JsonProperty("e2")
    public String e2;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public static class PitchHelper {

        public static int getPitchNumber(int stringNum, int fret) {

            //TUNING = E2–A2–D3–G3–B3–E4.
            return 40 + (stringNum - 1) * 5 + fret + (stringNum == StringHelper.GUITAR_STRING.B.getNum() ? -1 : 0);
        }
    }

    public static class FretHelper {

        private static int tryParseString(String string) {

            try {
                return Integer.parseInt(string);
            } catch (NumberFormatException ignore) {
            }

            return -1;
        }

        public static int getFret(int string, Chord chord) {

            switch (string) {
                case 1:
                    return tryParseString(chord.getE());
                case 2:
                    return tryParseString(chord.getA());
                case 3:
                    return tryParseString(chord.getD());
                case 4:
                    return tryParseString(chord.getG());
                case 5:
                    return tryParseString(chord.getB());
                case 6:
                    return tryParseString(chord.getE2());
            }

            throw new IllegalStateException();
        }
    }

    public Chord() {
    }

    public void setType(CHORD_TYPE type) {
        this.type = type;
    }

    public void setKey(CHORD_KEY key) {
        this.key = key;
    }

    public CHORD_TYPE getType() {
        return type;
    }

    public CHORD_KEY getKey() {
        return key;
    }

    public String getE() {
        return e;
    }

    public String getE2() {
        return e2;
    }

    public void setE(String e) {
        this.e = e;
    }

    public void setE2(String e2) {
        this.e2 = e2;
    }

    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }

    public String getD() {
        return d;
    }

    public void setD(String d) {
        this.d = d;
    }

    public String getG() {
        return g;
    }

    public void setG(String g) {
        this.g = g;
    }

    public String getB() {
        return b;
    }

    public void setB(String b) {
        this.b = b;
    }

    public static class ChordKeyConverter {

        @TypeConverter
        public String convert(CHORD_KEY chord_key) {

            return chord_key.getLabel();
        }

        @TypeConverter
        public CHORD_KEY convertBack(String chord_key) {

            for (CHORD_KEY key : CHORD_KEY.values()) {
                if (key.getLabel().equals(chord_key))
                    return key;
            }
            throw new IllegalArgumentException();
        }
    }

    public static class ChordTypeConverter {

        @TypeConverter
        public String convert(CHORD_TYPE chord_key) {

            return chord_key.getLabel();
        }

        @TypeConverter
        public CHORD_TYPE convertBack(String chord_type) {

            for (CHORD_TYPE type : CHORD_TYPE.values()) {
                if (type.getLabel().equals(chord_type))
                    return type;
            }
            throw new IllegalArgumentException();
        }
    }

    @NonNull
    @Override
    public String toString() {
        return getType().getLabel();
    }
}
