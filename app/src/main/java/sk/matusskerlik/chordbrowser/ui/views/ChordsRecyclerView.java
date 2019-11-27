/*
 * Copyright (c) 2019 Matúš Škerlík.
 * All rights reserved.
 */

package sk.matusskerlik.chordbrowser.ui.views;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class ChordsRecyclerView extends RecyclerView {

    int itemWidth = 0;

    public ItemDecoration decoration = new ItemDecoration() {


        @Override
        public void getItemOffsets(@NonNull Rect outRect, int itemPosition, @NonNull RecyclerView parent) {

            assert getLayoutManager() != null;
            assert getAdapter() != null;

            int offset = (getWidth() / 2) - (itemWidth / 2);

            if (itemPosition == 0)
                outRect.set(offset, 0, 0, 0);
            else if (itemPosition == (getAdapter().getItemCount() - 1))
                outRect.set(0, 0, offset, 0);
            else
                super.getItemOffsets(outRect, itemPosition, parent);
        }
    };

    public ChordsRecyclerView(@NonNull Context context) {
        super(context);
        init(context, null, 0);
    }

    public ChordsRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public ChordsRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    private void init(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle){
        addItemDecoration(decoration);

        itemWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                96, context.getResources().getDisplayMetrics());
    }
}
