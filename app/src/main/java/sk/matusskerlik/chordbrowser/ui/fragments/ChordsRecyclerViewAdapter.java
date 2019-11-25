package sk.matusskerlik.chordbrowser.ui.fragments;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import sk.matusskerlik.chordbrowser.R;
import java.util.List;

public class ChordsRecyclerViewAdapter<T> extends RecyclerView.Adapter<ChordsRecyclerViewAdapter.ViewHolder> {

    private final List<T> mValues;

    ChordsRecyclerViewAdapter(List<T> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_chords, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        holder.mItem = mValues.get(position);
        holder.mContentView.setText(mValues.get(position).toString());

    }

    void updateBy(List<T> update){
        mValues.clear();
        mValues.addAll(update);
        notifyDataSetChanged();
    }

    T getItemAt(int position){

        return mValues.get(position);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public static class ViewHolder<T> extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mContentView;
        public T mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mContentView = (TextView) view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
