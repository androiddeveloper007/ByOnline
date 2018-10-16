package com.bowen.tcm.common.model;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

/**
 * Describe: 拖动辅助类，主要通过ItemTouchHelper实现
 * Created by AwenZeng on 2018/5/8.
 */

public class DragItemTouchHelper extends ItemTouchHelper.Callback {


    public interface DragItemTouchBack{
        void onDragItemTouch(int oldPosition, int newPosition);
    }

    private DragItemTouchBack dragItemTouchBack;

    public DragItemTouchHelper(DragItemTouchBack dragItemTouchBack) {
        this.dragItemTouchBack = dragItemTouchBack;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return false; // swiped disabled
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlags;// movements drag
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            dragFlags = ItemTouchHelper.DOWN | ItemTouchHelper.UP
                    | ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT;
        } else {
            dragFlags = ItemTouchHelper.DOWN | ItemTouchHelper.UP;
        }
        return makeFlag(ItemTouchHelper.ACTION_STATE_DRAG, dragFlags); // as parameter, action drag and flags drag
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder
            target) {
        int fromPosition = viewHolder.getAdapterPosition();
        int toPosition = target.getAdapterPosition();
        dragItemTouchBack.onDragItemTouch(fromPosition, toPosition);
        // information to the interface
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        // swiped disabled
    }
}
