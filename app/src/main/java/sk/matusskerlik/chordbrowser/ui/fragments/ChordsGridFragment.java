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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;
import sk.matusskerlik.chordbrowser.R;
import sk.matusskerlik.chordbrowser.audio.ChordPlayer;
import sk.matusskerlik.chordbrowser.model.Chord;
import sk.matusskerlik.chordbrowser.model.ChordGroup;
import sk.matusskerlik.chordbrowser.sensor.AccelerationSensor;
import sk.matusskerlik.chordbrowser.ui.views.ChordView;

public class ChordsGridFragment extends DaggerFragment {


    @Inject
    public ChordsGridViewModel mViewModel;

    private RecyclerView chordGroupRecyclerView;
    private RecyclerView chordRecyclerView;
    private ChordView chordView;

    private ChordPlayer chordPlayer;
    private long disablePlayerUntil = 0;

    private int lastFocusedChordIndex = 0;
    private Chord lastFocusedChord;

    private ChordGroupAdapter chordGroupAdapter;
    private ChordAdapter chordAdapter;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ChordsGridFragment() {
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mViewModel.getAllChordGroups().observe(this, new Observer<List<ChordGroup>>() {
            @Override
            public void onChanged(List<ChordGroup> chordGroups) {
                chordGroupAdapter.replaceBy(chordGroups);
                chordAdapter.replaceBy(chordGroupAdapter.getItemAt(0).getChords());
                // now chords are filtered
                chordView.setChordToDraw(chordAdapter.getItemAt(0));

                lastFocusedChord = chordAdapter.getItemAt(0);
                lastFocusedChordIndex = 0;
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chords_gird, container, false);

        // Set the adapter
        Context context = view.getContext();
        chordGroupRecyclerView = view.findViewById(R.id.chordKeyList);
        chordRecyclerView = view.findViewById(R.id.chordTypeList);

        chordGroupRecyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
        chordRecyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));

        chordGroupAdapter = new ChordGroupAdapter();
        chordAdapter = new ChordAdapter();
        chordGroupRecyclerView.setAdapter(chordGroupAdapter);
        chordRecyclerView.setAdapter(chordAdapter);

        chordView = view.findViewById(R.id.chordView);

        new LinearSnapHelper().attachToRecyclerView(chordGroupRecyclerView);
        new LinearSnapHelper().attachToRecyclerView(chordRecyclerView);

        new MiddleItemFinder().attachToRecyclerView(getContext(), chordGroupRecyclerView, new MiddleItemFinder.MiddleItemCallback() {
            @Override
            public void scrollFinished(int middleElement) {

                chordAdapter.replaceBy(chordGroupAdapter.getItemAt(middleElement).getChords());

                Chord chordToDraw;
                try {
                    chordToDraw = chordAdapter.getItemAt(lastFocusedChordIndex);
                } catch (IndexOutOfBoundsException ignore) {
                    chordToDraw = chordAdapter.getItemAt(chordAdapter.getItemCount() - 1);
                }

                chordView.setChordToDraw(chordToDraw);
            }
        });

        new MiddleItemFinder().attachToRecyclerView(getContext(), chordRecyclerView, new MiddleItemFinder.MiddleItemCallback() {
            @Override
            public void scrollFinished(int middleElement) {
                if (lastFocusedChordIndex == middleElement)
                    return;

                lastFocusedChordIndex = middleElement;
                lastFocusedChord = chordAdapter.getItemAt(middleElement);
                chordView.setChordToDraw(lastFocusedChord);
                chordPlayer.playChord(lastFocusedChord);
            }
        });

        return view;
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);


        new AccelerationSensor(context).onAcceleration(new AccelerationSensor.AccelerationCallback() {
            @Override
            public void onAcceleration() {

                if (disablePlayerUntil < System.currentTimeMillis())
                    if (lastFocusedChord != null) {
                        chordPlayer.playChord(lastFocusedChord);
                        disablePlayerUntil =
                                System.currentTimeMillis() +
                                        (ChordPlayer.CHORD_SEQUENCE_DELAY * 5) +
                                        ChordPlayer.NOTE_LENGTH / 4;
                    }
            }
        });

        chordPlayer = new ChordPlayer(context);
    }

    private static class MiddleItemFinder extends RecyclerView.OnScrollListener {

        private static class Builder {

            private Context context;
            private RecyclerView recyclerView;
            private MiddleItemCallback mMiddleItemCallback;

            Builder() {
            }

            Builder setContext(Context context) {
                if (context == null)
                    throw new IllegalArgumentException();

                this.context = context;

                return this;
            }

            Builder setRecyclerView(RecyclerView recyclerView) {
                if (recyclerView == null)
                    throw new IllegalArgumentException();

                this.recyclerView = recyclerView;

                return this;
            }

            Builder setMiddleItemCallback(MiddleItemCallback mMiddleItemCallback) {
                if (mMiddleItemCallback == null)
                    throw new IllegalArgumentException();

                this.mMiddleItemCallback = mMiddleItemCallback;

                return this;
            }

            MiddleItemFinder build(){
                return new MiddleItemFinder(context,
                        (LinearLayoutManager) recyclerView.getLayoutManager(),
                        mMiddleItemCallback,
                        RecyclerView.SCROLL_STATE_IDLE
                );
            }
        }


        void attachToRecyclerView(Context context, RecyclerView recyclerView, MiddleItemCallback middleItemCallback){

            recyclerView.addOnScrollListener(
                new Builder()
                    .setContext(context)
                    .setRecyclerView(recyclerView)
                    .setMiddleItemCallback(middleItemCallback)
                    .build()
            );
        }

        private Context context;

        private LinearLayoutManager layoutManager;

        private MiddleItemCallback callback;

        private int controlState;

        static final int ALL_STATES = 10;

        MiddleItemFinder() {
        }

        private MiddleItemFinder(Context context, LinearLayoutManager layoutManager, MiddleItemCallback callback, int controlState) {
            this.context = context;
            this.layoutManager = layoutManager;
            this.callback = callback;
            this.controlState = controlState;
        }

        @Override
        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {

            if (controlState == ALL_STATES || newState == controlState) {

                int firstVisible = layoutManager.findFirstVisibleItemPosition();
                int lastVisible = layoutManager.findLastVisibleItemPosition();
                int itemsCount = lastVisible - firstVisible + 1;

                int screenCenter = context.getResources().getDisplayMetrics().widthPixels / 2;

                int minCenterOffset = Integer.MAX_VALUE;

                int middleItemIndex = 0;

                for (int index = 0; index < itemsCount; index++) {

                    View listItem = layoutManager.getChildAt(index);

                    if (listItem == null)
                        return;

                    int leftOffset = listItem.getLeft();
                    int rightOffset = listItem.getRight();
                    int centerOffset = Math.abs(leftOffset - screenCenter) + Math.abs(rightOffset - screenCenter);

                    if (minCenterOffset > centerOffset) {
                        minCenterOffset = centerOffset;
                        middleItemIndex = index + firstVisible;
                    }
                }


                callback.scrollFinished(middleItemIndex);
            }
        }

        public interface MiddleItemCallback {

            void scrollFinished(int middleElement);
        }
    }
}
