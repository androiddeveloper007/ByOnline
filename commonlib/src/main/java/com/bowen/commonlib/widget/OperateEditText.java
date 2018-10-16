package com.bowen.commonlib.widget;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;


/**
 * Created by AwenZeng on 2016/12/8.
 */

public class OperateEditText extends InputEditText implements TextWatcher {

    public OperateEditText(Context context) {
        super(context);
        init();
    }

    public OperateEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public OperateEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        addTextChangedListener(this);
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(s, start, lengthBefore, lengthAfter);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        String text = getText().toString();
        if (text.contains(".")) {
            int index = text.indexOf(".");
            if (index + 1 == 9) {
                text = text.substring(0, 6);
                setText(text);
                setSelection(text.length());
            } else if (index + 3 < text.length()) {
                text = text.substring(0, index + 3);
                setText(text);
                setSelection(text.length());
            }
        }else if(text.length()>6){
            text = text.substring(0, 6);
            setText(text);
            setSelection(text.length());
        }
    }
}
