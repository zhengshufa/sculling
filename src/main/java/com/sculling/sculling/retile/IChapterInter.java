package com.sculling.sculling.retile;

import java.util.List;

/**
 * @description Created by jwing on 2018/2/5.
 */
public interface IChapterInter {

    /**
     * 获取一个完整的url链接，显示所有章节列表
     * @param @param url
     * @param @return
     * @return
     * @throws
     */
    public List<Chapter> getChapter(String url);
}
