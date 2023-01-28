package com.sculling.sculling.novel.download;

import com.sculling.sculling.novel.NovelSource;
import com.sculling.sculling.novel.api.HbookerAPI;
import com.sculling.sculling.novel.api.hbooker.BookInfoJson;
import com.sculling.sculling.novel.api.hbooker.ChapListInfoJson;
import com.sculling.sculling.novel.api.hbooker.DivisionInfoJson;
import com.sculling.sculling.novel.beans.Chapter;
import com.sculling.sculling.novel.beans.Novel;
import com.sculling.sculling.novel.beans.Volume;

import java.io.IOException;
import java.util.List;

public class HbookerDownloadTask extends DownloadTask {

    private volatile HbookerAPI api;

    private synchronized HbookerAPI getApi() throws IOException {
        if (api == null) {
            synchronized (HbookerAPI.class) {
                if (api == null) {
                    if (downloadParams.api != null) {
                        api = (HbookerAPI) downloadParams.api;
                    } else {
                        api = new HbookerAPI(downloadParams.account, downloadParams.certificate);
                    }
                }
            }
        }
        return api;
    }

    @Override
    public Novel getNovel(long novelId) {
        Novel novel = new Novel() {
            @Override
            public boolean hasMultiVolume() {
                return true;
            }

            @Override
            public NovelSource getNovelSource() {
                return NovelSource.HbookerAPP;
            }
        };
        HbookerAPI api;
        try {
            api = getApi();
            BookInfoJson bookInfoJson = api.getBookInfoJson(novelId);
            novel.title = bookInfoJson.data.bookInfo.bookName;
            novel.author = bookInfoJson.data.bookInfo.authorName;
            List<DivisionInfoJson.Division> divisionList = api.getDivisionInfoJson(novelId).data.divisionList;
            int index = 1;
            for (DivisionInfoJson.Division division : divisionList) {
                Volume volume = new Volume();
                volume.volumeOrder = index++;
                ChapListInfoJson chapterListInfoJson = api.getChapterListInfoJson(division.divisionId);
                for (ChapListInfoJson.Chapter c : chapterListInfoJson.data.chapterList) {
                    Chapter chapter = new Chapter();
                    chapter.chapId = c.chapterId;
                    chapter.charCount = c.wordCount;
                    chapter.chapOrder = c.chapterIndex;
                    chapter.title = c.chapterTitle;
                    volume.chapterList.add(chapter);
                }
                novel.volumeList.add(volume);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return novel;
    }

    @Override
    public String getChapterContent(long novelId, long chapId) {
        try {
            return getApi().getChapterInfoJson(chapId).data.chapterInfo.txtContent;
        } catch (Exception e) {
            e.printStackTrace();;
            return "";
        }
    }

}
