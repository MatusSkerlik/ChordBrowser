package sk.matusskerlik.chordbrowser.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sk.matusskerlik.chordbrowser.R;
import sk.matusskerlik.chordbrowser.audio.ChordPlayer;
import sk.matusskerlik.chordbrowser.model.Chord;
import sk.matusskerlik.chordbrowser.model.ChordGroup;
import sk.matusskerlik.chordbrowser.ui.views.ChordView;

public class ChordsGridFragment extends Fragment {


    private ChordsGridViewModel mViewModel;
    private RecyclerView chordKeyList;
    private RecyclerView chordTypeList;
    private ChordView chordView;
    private ChordPlayer chordPlayer;

    private int chordKeyListFocusedItem = 0;
    private int chordTypeListFocusedItem = 0;

    private ChordsRecyclerViewAdapter<ChordGroup> chordKeyListAdapter;
    private ChordsRecyclerViewAdapter<Chord> chordTypeListAdapter;

    private List<ChordGroup> chordGroups = new ArrayList<>();

    private Callback<ChordGroup> chordsUpdateObserver = new Callback<ChordGroup>(){
        @Override
        public void onResponse(@NonNull Call<ChordGroup> call, @NonNull Response<ChordGroup> response) {

            assert response.body() != null;

            ChordGroup chordGroup = response.body();
            List<Chord> chords = chordGroup.getChords();

            if (chords.size() > 0) {

                chordGroups.add(chordGroup);
                chordKeyListAdapter.updateBy(chordGroups);

                if ((chordGroups.size() == 1)) {

                    chordTypeListAdapter.updateBy(chords);
                    chordView.setChordToDraw(chords.get(0));
                }
            }
        }

        @Override
        public void onFailure(@NonNull Call<ChordGroup> call, @NonNull Throwable t) {
            //TODO
        }
    };


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ChordsGridFragment() {
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mViewModel = ViewModelProviders.of(this).get(ChordsGridViewModel.class);

        for (Chord.CHORD_KEY key: Chord.CHORD_KEY.values()) {
            mViewModel.fetchChordsOfName(key, chordsUpdateObserver);
        }
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
        chordKeyList = (RecyclerView) view.findViewById(R.id.chordKeyList);
        chordTypeList = (RecyclerView) view.findViewById(R.id.chordTypeList);

        chordKeyList.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
        chordTypeList.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));

        chordKeyListAdapter = new ChordsRecyclerViewAdapter<>(new ArrayList<ChordGroup>());
        chordTypeListAdapter = new ChordsRecyclerViewAdapter<>(new ArrayList<Chord>());
        chordKeyList.setAdapter(chordKeyListAdapter);
        chordTypeList.setAdapter(chordTypeListAdapter);

        chordView = (ChordView) view.findViewById(R.id.chordView);

        new LinearSnapHelper().attachToRecyclerView(chordKeyList);
        new LinearSnapHelper().attachToRecyclerView(chordTypeList);

        new MiddleItemFinder().attachToRecyclerView(getContext(), chordKeyList, new MiddleItemFinder.MiddleItemCallback() {
            @Override
            public void scrollFinished(int middleElement) {

                chordKeyListFocusedItem = middleElement;
                chordTypeListAdapter.updateBy(
                        chordGroups.get(middleElement).getChords()
                );
                chordTypeListAdapter.notifyDataSetChanged();

                Chord chordToDraw;
                try {
                    chordToDraw = chordTypeListAdapter
                            .getItemAt(chordTypeListFocusedItem);
                } catch (IndexOutOfBoundsException ignore) {
                    chordToDraw = chordTypeListAdapter
                            .getItemAt(chordTypeListAdapter.getItemCount() - 1);
                }

                chordView.setChordToDraw(chordToDraw);
            }
        });

        new MiddleItemFinder().attachToRecyclerView(getContext(), chordTypeList, new MiddleItemFinder.MiddleItemCallback() {
            @Override
            public void scrollFinished(int middleElement) {
                if (chordTypeListFocusedItem == middleElement)
                    return;

                chordTypeListFocusedItem = middleElement;
                chordView.setChordToDraw(
                        chordTypeListAdapter.getItemAt(middleElement)
                );
                chordPlayer.playChord(
                        chordTypeListAdapter.getItemAt(middleElement)
                );
            }
        });

        return view;
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);


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
