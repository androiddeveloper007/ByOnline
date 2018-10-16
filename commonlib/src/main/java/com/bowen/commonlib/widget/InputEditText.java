package com.bowen.commonlib.widget;

import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.bowen.commonlib.utils.CheckStringUtl;
import com.bowen.commonlib.utils.StringUtil;


/**
 * 自定义EditTextView
 * Created by AwenZeng on 2016/12/8.
 */

public class InputEditText extends AppCompatEditText implements View.OnFocusChangeListener {
    private String mInputContent = "";
    private String mShowContent = "";
    private boolean isFocus;
    private boolean isFirstFocus = true;
    private boolean isNeedShowAll = false;

    public InputEditText(Context context) {
        super(context);
        init();
    }

    public InputEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public InputEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setOnFocusChangeListener(this);
    }

    public void setText(String str) {
        mInputContent = str;
        if(TextUtils.isEmpty(mInputContent)){
            mShowContent = mInputContent;
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 15.8f);
        }else if (CheckStringUtl.isAllNumber(mInputContent)) {
            mShowContent = mInputContent;
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 16.9f);
        } else {
            if (!isNeedShowAll && mInputContent.length() > 12 && StringUtil.isChineseChar(mInputContent)) {
                mShowContent = mInputContent.substring(0, 12) + "...";
            } else {
                mShowContent = mInputContent;
            }
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 16.9f);
        }
        CharSequence cs = mShowContent;
        setText(cs);
    }

    /**
     * 获取EditText的输入内容
     *
     * @return
     */
    public String getTextContent() {
        if (TextUtils.isEmpty(mInputContent) || isFocus) {
            mInputContent = getText().toString();
        }
        return mInputContent;
    }

    /**
     * 获取EditText的输入内容
     *
     * @return
     */
    public String getTextStr() {
        return getText().toString();
    }

    @Override
    public void onFocusChange(View view, boolean b) {
        isFocus = b;
        if (b) {
            if (TextUtils.isEmpty(mInputContent)) {
                mInputContent = getText().toString();
            }
            CharSequence cs = mInputContent;
            setText(cs);
            isFirstFocus = true;
        } else {
            mInputContent = getText().toString();
            if (CheckStringUtl.isAllNumber(mInputContent)) {
                mShowContent = mInputContent;
            } else {
                if (mInputContent.length() > 12 && StringUtil.isChineseChar(mInputContent)) {
                    mShowContent = mInputContent.substring(0, 12) + "...";
                } else {
                    mShowContent = mInputContent;
                }
            }
            if (!TextUtils.isEmpty(mShowContent)) {
                setTextSize(TypedValue.COMPLEX_UNIT_SP, 16.9f);
            }
            CharSequence cs = mShowContent;
            setText(cs);
        }
    }

    @Override
    protected void onSelectionChanged(int selStart, int selEnd) {
        super.onSelectionChanged(selStart, selEnd);
        if (!isFocus)
            return;
        if (selStart == selEnd && isFirstFocus) {
            setSelection(getText().length());
            isFirstFocus = false;
        }
    }

    public boolean isNeedShowAll() {
        return isNeedShowAll;
    }

    public void setNeedShowAll(boolean needShowAll) {
        isNeedShowAll = needShowAll;
    }
}
