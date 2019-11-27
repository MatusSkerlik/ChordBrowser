/*
 * Copyright (c) 2019 Matúš Škerlík.
 * All rights reserved.
 */

package sk.matusskerlik.chordbrowser.ui.fragments;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import sk.matusskerlik.chordbrowser.model.Chord;

class ChordAdapter extends BaseViewAdapter<Chord> {

    ChordAdapter() {
    }

    @Override
    void replaceBy(List<Chord> update) {

        List<Chord> copy = new ArrayList<>();
        // arrays must be of equal size to copy
        for (int i = 0; i < update.size(); i++) {
            copy.add(null);
        }
        Collections.copy(copy, update);

        // filter chords witch are played above 12 frets
        for (int i = 0; i < copy.size(); i++) {

            Chord chord = copy.get(i);

            for (int j = 1; j < 7; j++) {
                int fret = Chord.FretHelper.getFret(j, chord);
                if (fret > 12) {
                    copy.remove(i); // remove reference
                    break;
                }
            }
        }

        mValues.clear();
        mValues.addAll(copy);
        notifyDataSetChanged();
    }
}
