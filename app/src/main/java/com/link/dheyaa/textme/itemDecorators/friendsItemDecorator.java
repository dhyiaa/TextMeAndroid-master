package com.link.dheyaa.textme.itemDecorators;
/* TextMe Team
 * Jan 2019
 * friendsItemDecorator:
 * controls the friends' items and how they look
 */
import android.content.Context;
import android.graphics.Rect;
import android.view.View;
import androidx.annotation.DimenRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/*
makking the class and extending the ItemDecoration from RecyclerView
*/

public class friendsItemDecorator extends RecyclerView.ItemDecoration {

    /*
        defining the attributes of friendsItemDecorator
    */
    private int mItemOffset;

    /**
     * main constructor
     * @param itemOffset
     */
    public friendsItemDecorator(int itemOffset) {
        // setting the value of mItemOffset property by itemOffset variable
        mItemOffset = itemOffset;
    }


    /**
     * another constructor which takes the params below :
     * @param context the context of the activity
     * @param itemOffsetId the item index id
     */
    public friendsItemDecorator(@NonNull Context context, @DimenRes int itemOffsetId) {
        this(context.getResources().getDimensionPixelSize(itemOffsetId));
    }

    @Override

    /**
     * getItemOffsets return where is the item interms of it's index and offset
     */
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.set(mItemOffset, mItemOffset, mItemOffset, mItemOffset);

    }

}