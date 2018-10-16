package com.bowen.tcm.common.database.util;

import android.content.Context;


import com.bowen.tcm.common.database.gen.DaoMaster;
import com.bowen.tcm.common.database.gen.DaoSession;

import org.greenrobot.greendao.database.Database;

/**
 * Created by AwenZeng on 2018/7/18.
 */
public class DataBaseManager {
    private static String DB_NAME = "bowen_tcm.db";
    private static DataBaseManager mDaoManager;
    private static MySqlLiteOpenHelper mySqlLiteOpenHelper;
    private static DaoSession mDaoSession;
    private static Database mDatabase;
    private DataBaseManager() {}

    public static DataBaseManager getInstance(){
        if (mDaoManager == null){
            synchronized (DataBaseManager.class){
                if (mDaoManager == null){
                    mDaoManager = new DataBaseManager();
                }
            }
        }
        return mDaoManager;
    }

    public static void init(Context context){
        mySqlLiteOpenHelper = new MySqlLiteOpenHelper(context,DB_NAME,null);
        mDatabase = mySqlLiteOpenHelper.getWritableDb();
        mDaoSession = new DaoMaster(mDatabase).newSession();
    }

    public  DaoSession getDaoSession(){
        return mDaoSession;
    }
}
