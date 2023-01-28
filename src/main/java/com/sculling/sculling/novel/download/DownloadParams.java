package com.sculling.sculling.novel.download;

import com.sculling.sculling.novel.api.API;

import java.io.File;

public class DownloadParams {

    public File parent;             // 保存在该文件夹下

    public long novelId;            // 小说id，对于一个网站来说，每一本小说都应该有一个唯一id标识

    public String fileName;         // 文件名称

    public long timeout = -1;       // 超时时间

    // 是否启用多线程下载、该选项对服务器造成的压力比较大，但是下载速度非常快
    public boolean multiThreadOn = false;

    public String account;          // 账号

    public String certificate;      // 登录凭证

    public API api;                 // 如果配置了该属性，account、certificate会被忽略

}
