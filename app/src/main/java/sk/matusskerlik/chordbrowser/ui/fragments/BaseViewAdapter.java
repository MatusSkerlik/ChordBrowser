package sk.matusskerlik.chordbrowser.ui.fragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import sk.matusskerlik.chordbrowser.R;

abstract class BaseViewAdapter<T> extends RecyclerView.Adapter<BaseViewAdapter.ViewHolder> {

    final List<T> mValues;

    BaseViewAdapter(List<T> items) {
        mValues = items;
    }

    BaseViewAdapter() {
        mValues = new ArrayList<>();
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

    abstract void replaceBy(List<T> update);

    T getItemAt(int position){

        return mValues.get(position);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public static class ViewHolder<T> extends RecyclerView.ViewHolder {
        final View mView;
        final TextView mContentView;
        T mItem;

        ViewHolder(View view) {
            super(view);
            mView = view;
            mContentView = view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
