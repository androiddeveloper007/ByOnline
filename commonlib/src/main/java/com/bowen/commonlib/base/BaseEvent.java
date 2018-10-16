package com.bowen.commonlib.base;

/**
 * EventBus事件基类
 */
public class BaseEvent {
    private int eventType;//可能类型有很多种，数据也不一样
    private String typeString;
    private Object data;//数据对象

    public BaseEvent() {
    }

    public BaseEvent(int eventType) {
        this.eventType = eventType;
    }

    public BaseEvent(String typeString) {
        this.typeString = typeString;
    }

    public BaseEvent(Object data) {
        this.data = data;
    }

    public BaseEvent(int eventType, Object data) {
        this.eventType = eventType;
        this.data = data;
    }

    public BaseEvent(int eventType, String typeString, Object data) {
        this.eventType = eventType;
        this.typeString = typeString;
        this.data = data;
    }

    public int getEventType() {
        return eventType;
    }

    public void setEventType(int eventType) {
        this.eventType = eventType;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getTypeString() {
        return typeString;
    }

    public void setTypeString(String typeString) {
        this.typeString = typeString;
    }
}
