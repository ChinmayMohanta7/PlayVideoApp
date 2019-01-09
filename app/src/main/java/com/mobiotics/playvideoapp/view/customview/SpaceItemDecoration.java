package com.mobiotics.playvideoapp.view.customview;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class SpaceItemDecoration extends RecyclerView.ItemDecoration {
    private static final boolean DEFAULT_ADD_SPACE_ABOVE_FIRST_ITEM = false;
    private static final boolean DEFAULT_ADD_SPACE_BELOW_LAST_ITEM = false;

    private final int space;
    private final boolean addSpaceAboveFirstItem;
    private final boolean addSpaceBelowLastItem;

    public SpaceItemDecoration(int space) {
        this(space, DEFAULT_ADD_SPACE_ABOVE_FIRST_ITEM, DEFAULT_ADD_SPACE_BELOW_LAST_ITEM);
    }

    public SpaceItemDecoration(int space, boolean addSpaceAboveFirstItem,
                               boolean addSpaceBelowLastItem) {
        this.space = space;
        this.addSpaceAboveFirstItem = addSpaceAboveFirstItem;
        this.addSpaceBelowLastItem = addSpaceBelowLastItem;
    }

    @Override public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                                         RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int childCount = parent.getChildCount();
        final int itemPosition = parent.getChildAdapterPosition(view);
        final int itemCount = state.getItemCount();

        outRect.left = 0;
        outRect.right = 0;
        outRect.bottom = 0;
        outRect.top = space;
    }

}
