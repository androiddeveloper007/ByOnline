package com.bowen.commonlib.widget;

import android.content.Context;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import com.bowen.commonlib.R;


/**
 * Created by AwenZeng on 2017/1/7.
 */

public class SpannableStringClickSpan extends ClickableSpan {
    private Context mContext;
    private View.OnClickListener mListener;

    public void setOnlickListener(View.OnClickListener listener){
        mListener = listener;
    }

    public SpannableStringClickSpan(Context context) {
        super();
        mContext = context;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        ds.setColor(mContext.getResources().getColor(R.color.color_main));
        ds.setUnderlineText(true);
    }

    @Override
    public void onClick(View widget) {
        if(mListener!=null){
            mListener.onClick(widget);
        }
    }
}
