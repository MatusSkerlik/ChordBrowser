package sk.matusskerlik.chordbrowser.audio;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Handler;
import android.util.SparseIntArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicIntegerArray;

import sk.matusskerlik.chordbrowser.model.Chord;

public class ChordPlayer {

    private static final Object loadLock = new Object();
    public static int CHORD_SEQUENCE_DELAY = 50;
    public static int NOTE_LENGTH = 4000;
    private static String[] stringLabels = {"e", "a", "d", "g", "b", "e2"};

    private Context context;
    private List<AtomicBoolean> playableReferences = new ArrayList<>();
    private AtomicIntegerArray playedResources = new AtomicIntegerArray(6);
    private final Map<String, Integer> stringResourceMap = new HashMap<>();

    private SparseIntArray pitchToRes = new SparseIntArray();
    private SoundPool soundPool = new SoundPool.Builder()
            .setAudioAttributes(
                    new AudioAttributes.Builder()
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .setFlags(AudioAttributes.FLAG_AUDIBILITY_ENFORCED)
                            .setUsage(AudioAttributes.USAGE_GAME)
                            .build()
            )
            .setMaxStreams(6 * 12)
            .build();
    private Handler mHandler = new Handler();

    public ChordPlayer(Context context) {
        this.context = context;

        init();
    }

    private void init() {

        playableReferences.add(new AtomicBoolean(true));

        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (loadLock) {
                    for (final String stringName : stringLabels) {
                        for (int fret = 0; fret < 13; fret++) {
                            int resId = soundPool.load(context, context.getResources().getIdentifier(
                                    stringName + fret, "raw",
                                    context.getPackageName()), 1);
                            System.out.println("Loaded sound: " + stringName + fret);
                            stringResourceMap.put(stringName + fret, resId);
                        }
                    }
                }
            }
        }).start();
    }

    private int mapStringFretToAudio(int string, int fret){

        synchronized (loadLock) {

            String string_name = stringLabels[string - 1];
            System.out.println("String : " + string_name + " Fret : " + fret);

            return stringResourceMap.get(string_name + fret);
        }
    }

    public void playChord(Chord chord){

        int delay = CHORD_SEQUENCE_DELAY;

        // disable before postDelayed plays
        playableReferences.get(playableReferences.size() - 1).set(false);

        // set new AtomicBoolean for current play
        // playableRef will be passed as reference to handler closure, value inside can be updated
        final AtomicBoolean playableRef = new AtomicBoolean(true);
        playableReferences.add(playableRef);

        for (Chord.StringHelper.GUITAR_STRING string : Chord.StringHelper.GUITAR_STRING.values()) {

            int fret = Chord.FretHelper.getFret(string.getNum(), chord);
            if (fret == -1)
                continue;

            final int resId = mapStringFretToAudio(string.getNum(), fret);

            //stop current plays
            final int beforeId = playedResources.getAndSet(string.getNum() - 1, resId);
            soundPool.stop(beforeId);

            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (playableRef.get())
                        soundPool.play(resId, 1f, 1f, 1, 0, 1);
                }
            }, delay);

            delay+=CHORD_SEQUENCE_DELAY;
        }
    }
}
