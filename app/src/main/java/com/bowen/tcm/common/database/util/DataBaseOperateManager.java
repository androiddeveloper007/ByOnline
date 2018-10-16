package com.bowen.tcm.common.database.util;

import com.bowen.commonlib.utils.EmptyUtils;
import com.bowen.commonlib.utils.LogUtil;
import com.bowen.tcm.common.database.entity.ChatMessage;
import com.bowen.tcm.common.database.gen.ChatMessageDao;
import com.bowen.tcm.common.database.gen.ChatMessageDao.Properties;
import com.bowen.tcm.common.database.gen.DaoSession;

import java.util.List;

/**
 * Created by AwenZeng on 2018/7/18.
 */
public class DataBaseOperateManager implements IOperateDao<ChatMessage> {
    private DaoSession mDaoSession = DataBaseManager.getInstance().getDaoSession();

    @Override
    public boolean insert(ChatMessage chatMessage) {
        return mDaoSession.getChatMessageDao()
                .insert(chatMessage) > 0 ? true : false;
    }

    @Override
    public boolean delete(ChatMessage user) {
        try {
            mDaoSession.getChatMessageDao()
                    .delete(user);
        } catch (Exception e) {
            LogUtil.androidLog("删除失败");
            return false;
        }
        return true;
    }

    @Override
    public boolean update(ChatMessage user) {
        try {
            mDaoSession.getChatMessageDao()
                    .update(user);
        } catch (Exception e) {
            LogUtil.androidLog("更新失败");
            return false;
        }
        return true;
    }

    @Override
    public List<ChatMessage> queryAll(int pageNo,int pageSize) {
        return mDaoSession.getChatMessageDao().queryBuilder().offset(pageNo * pageSize).limit(pageSize).list();
    }

    public List<ChatMessage> queryAll(String userId,int pageNo,int pageSize) {
        return mDaoSession.getChatMessageDao().queryBuilder().where(Properties.UserId.eq(userId)).offset(pageNo * pageSize).limit(pageSize).list();
    }

    @Override
    public ChatMessage queryById(long id) {
        return mDaoSession.getChatMessageDao()
                .loadByRowId(id);
    }

    @Override
    public List<ChatMessage> queryByObj(String where, String... params) {
        return mDaoSession.getChatMessageDao()
                .queryRaw(where, params);
    }

    public ChatMessage queryByName(String name) {
        return mDaoSession.getChatMessageDao()
                .queryBuilder()
                .where(ChatMessageDao.Properties.Name.eq(name))
                .build()
                .unique();
    }


    /**
     * 获取数据列表长度
     * @param userId
     * @return
     */
    public int getSize(String userId) {
        List<ChatMessage> list =  mDaoSession.getChatMessageDao().queryBuilder().where(ChatMessageDao.Properties.UserId.eq(userId)).build().list();
        return EmptyUtils.isNotEmpty(list)?list.size():0;
    }


}
