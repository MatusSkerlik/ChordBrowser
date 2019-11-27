/*
 * Copyright (c) 2019 Matúš Škerlík.
 * All rights reserved.
 */

package sk.matusskerlik.chordbrowser.ui.fragments;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import sk.matusskerlik.chordbrowser.model.ChordGroup;

class ChordGroupAdapter extends BaseViewAdapter<ChordGroup> {

    ChordGroupAdapter() {
    }

    @Override
    protected void replaceBy(List<ChordGroup> update) {

        List<ChordGroup> copy = new ArrayList<>();
        // arrays must be of equal size to copy
        for (int i = 0; i < update.size(); i++) {
            copy.add(null);
        }
        Collections.copy(copy, update);

        Collections.sort(copy, new Comparator<ChordGroup>() {
            @Override
            public int compare(ChordGroup o1, ChordGroup o2) {
                return o1.toString()
                        .compareTo(o2.toString());
            }
        });

        mValues.clear();
        mValues.addAll(copy);
        notifyDataSetChanged();
    }
}
