package com.sculling.sculling.novel.api.website.biqumu;

import com.sculling.sculling.novel.api.website.common.ChapterBean;

public class SearchResultBean {

    public String author;
    public String novelName;

    public String url;                  // 小说地址
    public String tag;                  // 小说分类标签
    public ChapterBean lastChapter;     // 最新章节

}
