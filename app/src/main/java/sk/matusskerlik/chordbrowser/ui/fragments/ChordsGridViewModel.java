/*
 * Copyright (c) 2019 Matúš Škerlík.
 * All rights reserved.
 */

package sk.matusskerlik.chordbrowser.ui.fragments;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import javax.inject.Inject;

import sk.matusskerlik.chordbrowser.model.ChordGroup;
import sk.matusskerlik.chordbrowser.model.repository.ChordRepository;

public class ChordsGridViewModel extends ViewModel {

    private ChordRepository chordRepository;

    @Inject
    public ChordsGridViewModel(ChordRepository chordRepository) {
        this.chordRepository = chordRepository;
    }

    public LiveData<List<ChordGroup>> getAllChordGroups() {
        return chordRepository.getAllChordGroups();
    }
}
