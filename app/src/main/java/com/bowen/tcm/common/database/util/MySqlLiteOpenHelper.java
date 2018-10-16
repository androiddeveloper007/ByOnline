package com.bowen.tcm.common.database.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;


import com.bowen.commonlib.utils.LogUtil;
import com.bowen.tcm.common.database.gen.ChatMessageDao;
import com.bowen.tcm.common.database.gen.DaoMaster;

import org.greenrobot.greendao.database.Database;

/**
 * Created by AwenZeng on 2018/7/18.
 */
public class MySqlLiteOpenHelper extends DaoMaster.OpenHelper{

    public MySqlLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        LogUtil.androidLog("oldVersion:"+oldVersion+",newVersion"+newVersion);
        MigrationHelper.getInstance().migrate(db,ChatMessageDao.class);
    }

}
