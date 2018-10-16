package com.bowen.commonlib.event;

public class FileLoadEvent {

    private long total;
    private long progress;

    public void setTotal(long total) {
        this.total = total;
    }

    public void setProgress(long progress) {
        this.progress = progress;
    }

    public long getProgress() {
        return progress;
    }

    public long getTotal()
    {
        return total;
    }

    public FileLoadEvent(long total, long progress) {
        this.total = total;
        this.progress = progress;
    }
}
