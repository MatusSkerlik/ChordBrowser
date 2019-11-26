package sk.matusskerlik.chordbrowser.audio;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Handler;
import android.util.SparseIntArray;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicIntegerArray;

import sk.matusskerlik.chordbrowser.model.Chord;

public class ChordPlayer {

    public static int CHORD_SEQUENCE_DELAY = 333;
    public static int NOTE_LENGTH = 4000;

    private Context context;
    private List<AtomicBoolean> playableReferences = new ArrayList<>();
    private AtomicIntegerArray playedResources = new AtomicIntegerArray(6);

    private SoundPool soundPool = new SoundPool.Builder()
            .setAudioAttributes(
                    new AudioAttributes.Builder()
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .setFlags(AudioAttributes.FLAG_AUDIBILITY_ENFORCED)
                            .setUsage(AudioAttributes.USAGE_GAME)
                            .build()
            )
            .setMaxStreams(38)
            .build();

    private SparseIntArray pitchToRes = new SparseIntArray();
    private Handler mHandler = new Handler();

    public ChordPlayer(Context context) {
        this.context = context;

        init();
    }

    private void init() {

        playableReferences.add(new AtomicBoolean(true));

        for (int i = 40; i < 79; i++) {

            int resId = soundPool.load(context, context.getResources().getIdentifier(
                    "res" + String.valueOf(i), "raw",
                    context.getPackageName()), 1);
            System.out.println("Loaded sound: " + resId);
            pitchToRes.put(i, resId);
        }
    }

    private int mapStringFretToAudio(int string, int fret){

        int pitch = Chord.PitchHelper.getPitchNumber(string, fret);

        System.out.println("String : " + string + " Fret : " +  fret + " " + "play pitch -> " + pitch);

        return pitchToRes.get(pitch);
    }

    public void playChord(Chord chord){

        int delay = 0;

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
                        soundPool.play(resId, 0.8f, 0.8f, 1, 0, 1);
                }
            }, delay);

            delay+=CHORD_SEQUENCE_DELAY;
        }
    }
}
