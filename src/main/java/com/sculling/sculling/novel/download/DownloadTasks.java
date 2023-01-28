package com.sculling.sculling.novel.download;

import com.sculling.sculling.novel.NovelSource;

public class DownloadTasks {

    public static DownloadTask getDownloadTask(NovelSource novelSource) {
        switch (novelSource) {
            case SfacgAPP:
                return new SfacgDownloadTask();
            case HbookerAPP:
                return new HbookerDownloadTask();
            default:
                throw new IllegalArgumentException("暂时不支持该种类型的下载任务!");
        }
    }

}
