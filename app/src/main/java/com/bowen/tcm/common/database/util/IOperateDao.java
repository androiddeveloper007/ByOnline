package com.bowen.tcm.common.database.util;

import java.util.List;

/**
 * Created by AwenZeng on 2018/7/18.
 */
public interface IOperateDao<T> {
    boolean insert(T t);

    boolean delete(T t);

    boolean update(T t);

    List<T> queryAll(int pageNo,int pageSize);

    T queryById(long id);

    List<T> queryByObj(String where, String... params);
}
