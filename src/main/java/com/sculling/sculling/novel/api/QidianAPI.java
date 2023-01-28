package com.sculling.sculling.novel.api;

import com.sculling.sculling.novel.NovelSource;

public class QidianAPI implements API {

    @Override
    public NovelSource getNovelSource() {
        return NovelSource.Qidian;
    }

}
