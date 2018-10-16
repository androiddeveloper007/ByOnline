package com.bowen.commonlib.utils;


import com.bowen.commonlib.base.BaseDialog;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by AwenZeng on 2017/1/5.
 */

public class DialogManagerUtil {
    private static DialogManagerUtil instance;

    public List<SoftReference<BaseDialog>> dialoglist;

    public DialogManagerUtil() {
        dialoglist = new ArrayList<>();
    }

    public static DialogManagerUtil getInstance(){
        if(instance == null){
            synchronized (DialogManagerUtil.class){
                if(instance == null){
                    instance = new DialogManagerUtil();
                }
            }
        }
        return instance;
    }
    public void add(BaseDialog dialog){
        SoftReference<BaseDialog> dialogWR = new SoftReference<BaseDialog>(dialog);
        dialoglist.add(dialogWR);
    }
    public void remove(BaseDialog dialog){
        SoftReference<BaseDialog> dialogWR = new SoftReference<BaseDialog>(dialog);
        dialoglist.remove(dialogWR);
    }

    public void closeAll(){
        if(dialoglist==null||dialoglist.size() == 0)
            return;
        for(SoftReference<BaseDialog> dialog:dialoglist){
            if(dialog.get()!=null&&dialog.get().isShowing()){
                dialog.get().dismiss();
            }
        }
    }
}
