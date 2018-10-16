package com.bowen.tcm.common.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bowen.commonlib.utils.ToastUtil;
import com.bowen.tcm.R;


/**
 * 顶部栏,带搜索的，用于偏方搜索页
 *
 * @author zzp
 * @time 2016/12/2 14:28
 */
public class SearchActionBar extends RelativeLayout {

    /**
     * 用TAG来查找Activity中的ActionTitleBar
     */
    public static final String TAG = "SearchActionBar";
    private View layout;
    public EditText action_bar_edit_text;
    //    private TextView mBackTv;
//    private ImageButton mLeftButton;
    private ImageButton mRightButton;
    private TextView mRightTextButton;
    private View mRoot;

    public SearchActionBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    public SearchActionBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public SearchActionBar(Context context) {
        super(context);
        init(context, null);
    }

    private void init(Context ctx, AttributeSet attrs) {
        //设置tag用于查找
        setTag(TAG);
        mRoot = LayoutInflater.from(ctx).inflate(R.layout.search_action_bar, this, false);
        layout = mRoot.findViewById(R.id.titleView);
        action_bar_edit_text = mRoot.findViewById(R.id.action_bar_edit_text);
        mRightButton = mRoot.findViewById(R.id.actionButton2);
        mRightTextButton = mRoot.findViewById(R.id.actionButton3);
        if (attrs != null) {
            for (int i = 0; i < attrs.getAttributeCount(); i++) {
                String name = attrs.getAttributeName(i);
                Log.d(null, name);
                if (name != null && name.equalsIgnoreCase("background")) {
                    int res = attrs.getAttributeResourceValue(i, 0);
                    if (res > 0) {
                        setBackgroundDrawable(null);
                        setBackgroundResource(res);
                    }
                    break;
                }
            }
        }
        final TypedArray a = getResources().obtainAttributes(attrs, R.styleable.ActionTitleBar);

        boolean titleViewVisible = a.getBoolean(R.styleable.ActionTitleBar_titleViewVisible, false);
        layout.setVisibility(titleViewVisible ? View.VISIBLE : View.GONE);

        //Shadow line
        boolean shadowVisible = a.getBoolean(R.styleable.ActionTitleBar_shadowLineVisible, false);
        mRoot.findViewById(R.id.line).setVisibility(shadowVisible ? View.VISIBLE : View.GONE);

//        mLeftButton.setVisibility(a.getBoolean(R.styleable.ActionTitleBar_leftIcoVisible, true)? VISIBLE: GONE);

        //Set button text
        String text = a.getString(R.styleable.ActionTitleBar_rightButtonText);
        if (text != null) {
            mRightTextButton.setText(text);
            mRightTextButton.setVisibility(View.VISIBLE);
            mRightButton.setVisibility(View.GONE);
        }
        Drawable image = a.getDrawable(R.styleable.ActionTitleBar_rightButtonImage);
        if (image != null) {
            mRightButton.setImageDrawable(image);
            mRightButton.setVisibility(View.VISIBLE);
        }
        if (mRightButton.getVisibility() == View.VISIBLE) {
            mRightButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View arg0) {
                }
            });
        }
        a.recycle();

        addView(mRoot);
    }

    @Override
    public void setBackgroundColor(int color) {
        super.setBackgroundColor(color);
        if (mRoot != null) mRoot.setBackgroundColor(color);
        for (int i = 0; i < getChildCount(); i++) {
            View v = getChildAt(i);
            v.setBackgroundColor(color);
        }
    }

    @Override
    public void setBackgroundResource(int resid) {
        super.setBackgroundResource(resid);
        if (mRoot != null) mRoot.setBackgroundResource(resid);
        for (int i = 0; i < getChildCount(); i++) {
            View v = getChildAt(i);
            v.setBackgroundResource(resid);
        }
    }

    /**
     * @return Title  view
     */
    public View getTitleView() {
        return layout;
    }

    /**
     * @return Right button
     */
    public ImageButton getRightButton() {
        return mRightButton;
    }

    public void setRightButtonVisible(int visibility) {
        mRightButton.setVisibility(visibility);
    }

    /**
     * @return Right Text button
     */
    public TextView getRightTextButton() {
        return mRightTextButton;
    }

    public TextView getSearchEditText() {
        return action_bar_edit_text;
    }


    /**
     * Set right button text
     *
     * @param text text
     */
    public void setRightButtonText(CharSequence text) {
        mRightTextButton.setText(text);
        mRightTextButton.setVisibility(View.VISIBLE);
    }

    public void setRightButtonText(int textId) {
        mRightTextButton.setText(textId);
        mRightTextButton.setVisibility(View.VISIBLE);
    }

    public void setEditTextHint(String str) {
        action_bar_edit_text.setHint(str);
    }

    public void setEditFocusRequest() {
        action_bar_edit_text.setFocusable(true);
        action_bar_edit_text.setFocusableInTouchMode(true);
        action_bar_edit_text.requestFocus();
        action_bar_edit_text.findFocus();
    }
}
