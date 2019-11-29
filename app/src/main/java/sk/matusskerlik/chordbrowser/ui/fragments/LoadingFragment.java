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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;

import java.util.List;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;
import sk.matusskerlik.chordbrowser.R;
import sk.matusskerlik.chordbrowser.model.ChordGroup;
import sk.matusskerlik.chordbrowser.model.repository.ChordRepository;

public class LoadingFragment extends DaggerFragment {

    @Inject
    public LoadingViewModel mViewModel;

    private LinearLayout linearLayoutContainer;
    private TextView loadingTextView;

    private LoadingFragmentCallbacks mCallback;

    public static LoadingFragment newInstance() {
        return new LoadingFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View infView = inflater.inflate(R.layout.loading_fragment, container, false);

        linearLayoutContainer = infView.findViewById(R.id.loading_layout_container);
        loadingTextView = infView.findViewById(R.id.loading_message);

        return infView;
    }

    @Override
    public void onAttach(final Context context) {
        super.onAttach(context);

        if (context instanceof LoadingFragmentCallbacks)
            mCallback = (LoadingFragmentCallbacks) context;
        else
            throw new RuntimeException("Activity must implement LoadingFragmentCallbacks");

        fetchChords(context);
    }

    private void fetchChords(final Context context) {

        mViewModel.getAllChordGroups(new ChordRepository.RepositoryListener() {
            @Override
            public void onError(Exception e) {
                loadingTextView.setText(R.string.loading_error);

                final ProgressBar progressBar = linearLayoutContainer.findViewById(R.id.loading_bar);
                linearLayoutContainer.removeView(progressBar);

                final Button resetButton = new Button(context);
                resetButton.setText(R.string.loading_reset);

                resetButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        linearLayoutContainer.removeView(resetButton);
                        linearLayoutContainer.addView(progressBar);
                        loadingTextView.setText(R.string.loading);
                        fetchChords(context);
                    }
                });

                linearLayoutContainer.addView(resetButton);
            }
        }).observe(this, new Observer<List<ChordGroup>>() {
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
