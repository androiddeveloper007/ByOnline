package com.bowen.commonlib.widget.callback;

import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;

/**
 * 禁止复制黏贴Callback
 * Created by AwenZeng on 2017/3/13.
 */

public class BanCopyPasteCallback implements ActionMode.Callback {
    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {

    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        return false;
    }
}
