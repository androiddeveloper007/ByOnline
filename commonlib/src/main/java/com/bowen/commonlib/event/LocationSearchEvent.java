package com.bowen.commonlib.event;


import com.bowen.commonlib.base.BaseEvent;
import com.bowen.commonlib.bean.SearchResult;

import java.util.List;

/**
 * Created by AwenZeng on 2016/12/16.
 */

public class LocationSearchEvent extends BaseEvent {

    private String title;

    private List<SearchResult> addressList;

    public LocationSearchEvent() {
    }

    public LocationSearchEvent(List<SearchResult> addressList) {
        this.addressList = addressList;
    }

    public LocationSearchEvent(int eventType, List<SearchResult> addressList) {
        super(eventType);
        this.addressList = addressList;
    }

    public LocationSearchEvent(int eventType, Object data, List<SearchResult> addressList) {
        super(eventType, data);
        this.addressList = addressList;
    }

    public LocationSearchEvent(int eventType, String typeString, Object data, List<SearchResult> addressList) {
        super(eventType, typeString, data);
        this.addressList = addressList;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<SearchResult> getAddressList() {
        return addressList;
    }

    public void setAddressList(List<SearchResult> addressList) {
        this.addressList = addressList;
    }
}
