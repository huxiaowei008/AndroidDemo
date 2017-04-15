package com.hxw.frame.update;

/**
 * Created by hxw on 2017/2/15.
 */

public class DownloadStatus {

    private long totalSize;
    private long downloadSize;

    public long getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(long totalSize) {
        this.totalSize = totalSize;
    }

    public long getDownloadSize() {
        return downloadSize;
    }

    public void setDownloadSize(long downloadSize) {
        this.downloadSize = downloadSize;
    }
}
