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

public class LoadingViewModel extends ViewModel {

    private ChordRepository chordRepository;

    @Inject
    public LoadingViewModel(ChordRepository chordRepository) {
        this.chordRepository = chordRepository;
    }

    LiveData<List<ChordGroup>> getAllChordGroups(ChordRepository.RepositoryListener mListener) {
        return chordRepository.getAllChordGroups(mListener);
    }
}
