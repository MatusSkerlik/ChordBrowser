/*
 * Copyright (c) 2019 Matúš Škerlík.
 * All rights reserved.
 */

package sk.matusskerlik.chordbrowser.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;

import java.util.List;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;
import sk.matusskerlik.chordbrowser.R;
import sk.matusskerlik.chordbrowser.model.ChordGroup;

public class LoadingFragment extends DaggerFragment {

    @Inject
    public LoadingViewModel mViewModel;

    private LoadingFragmentCallbacks mCallback;

    public static LoadingFragment newInstance() {
        return new LoadingFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.loading_fragment, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof LoadingFragmentCallbacks)
            mCallback = (LoadingFragmentCallbacks) context;
        else
            throw new RuntimeException("Activity must implement LoadingFragmentCallbacks");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mViewModel.getAllChordGroups().observe(this, new Observer<List<ChordGroup>>() {
            @Override
            public void onChanged(List<ChordGroup> chordGroups) {
                mCallback.dataEndLoading();
            }
        });
    }

    public interface LoadingFragmentCallbacks {

        void dataEndLoading();
    }
}
