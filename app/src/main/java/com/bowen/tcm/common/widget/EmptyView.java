package com.bowen.tcm.common.widget;

import android.app.Activity;
import android.content.Context;
import android.text.method.LinkMovementMethod;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bowen.commonlib.utils.SpannableStringUtils;
import com.bowen.commonlib.widget.SpannableStringClickSpan;
import com.bowen.tcm.R;

/**
 * Created by AwenZeng on 2017/1/3.
 */

public class EmptyView extends RelativeLayout {
    private Activity mContext;
    private TextView mContentTv;
    private int type;
    private String mContentStr;
    private String mGotoStr;


    public EmptyView(Context context) {
        super(context);
        init(context);
    }

    public EmptyView(Context context, int type, String mContentStr, String mGotoStr) {
        super(context);
        this.mContentStr = mContentStr;
        this.mGotoStr = mGotoStr;
        init(context);
    }


    public EmptyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public EmptyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mContext = (Activity) context;
        SpannableStringClickSpan clickSpan = new SpannableStringClickSpan(mContext);
        clickSpan.setOnlickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        mContext.getLayoutInflater().inflate(R.layout.layout_view_empty, this);
        mContentTv = (TextView) findViewById(R.id.mContentTv);
        mContentTv.setText(SpannableStringUtils.getBuilder(mContentStr)
                .setForegroundColor(getResources().getColor(R.color.color_main_black))
                .append(mGotoStr)
                .setClickSpan(clickSpan)
                .create());
        mContentTv.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
