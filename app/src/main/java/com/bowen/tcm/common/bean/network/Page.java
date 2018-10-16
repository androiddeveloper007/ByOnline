package com.bowen.tcm.common.bean.network;

import java.io.Serializable;

/**
 * Created by AwenZeng on 2017/1/3.
 */

public class Page implements Serializable{

    /**
     * prePage : 上一页页码
     * nextPage : 下一页页码
     * totalCount : 总条数
     * pageNo : 当前页码
     * pageSize : 每页条数
     * totalPages : 总页数
     */

    private int prePage;
    private int nextPage;
    private int totalCount;
    private int pageNo;
    private int pageSize;
    private int totalPages;

    public int getPrePage() {
        return prePage;
    }

    public void setPrePage(int prePage) {
        this.prePage = prePage;
    }

    public int getNextPage() {
        return nextPage;
    }

    public void setNextPage(int nextPage) {
        this.nextPage = nextPage;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}
