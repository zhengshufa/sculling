package com.sculling.sculling.novel.api.website;

import com.sculling.sculling.novel.NovelSource;
import com.sculling.sculling.novel.api.API;
import com.sculling.sculling.novel.api.website._147xs.SearchResultBean;
import com.sculling.sculling.novel.api.website.common.ChapterBean;
import com.sculling.sculling.novel.api.website.common.NovelBean;
import com.sculling.sculling.novel.api.website.common.NovelStatus;
import com.sculling.sculling.novel.utils.NetUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class _147xsWebsiteAPI implements API {

    private static final String API = "https://www.147xs.org/";

    @Override
    public NovelSource getNovelSource() {
        return NovelSource._143xsWebsite;
    }

    public List<SearchResultBean> search(String keyword) throws IOException {
        Document document = Jsoup.connect(API + "/search.php").header("User-Agent", NetUtils.getRandomUA())
                .data("keyword", keyword)
                .post();
        List<SearchResultBean> searchResultBeanList = new ArrayList<>();
        Elements results = document.select("#bookcase_list > tr");
        for (Element result : results) {
            SearchResultBean resultBean = new SearchResultBean();
            Elements tds = result.select("td");
            resultBean.tag = tds.get(0).text();
            resultBean.novelName = tds.get(1).text();
            resultBean.url = tds.get(1).select("a").attr("href");
            resultBean.author = tds.get(3).text();
            resultBean.lastChapter = new ChapterBean();
            resultBean.lastChapter.chapterName = tds.get(2).text();
            resultBean.lastChapter.url = tds.get(2).select("a").attr("href");
            resultBean.lastUpdateTime = tds.get(4).text();
            if (tds.get(5).text().contains("连载")) {
                resultBean.status = NovelStatus.Updating;
            } else {
                resultBean.status = NovelStatus.Finished;
            }
            searchResultBeanList.add(resultBean);
        }
        return searchResultBeanList;
    }

    public NovelBean getNovel(String url) throws IOException {
        NovelBean novelBean = new NovelBean(getNovelSource());
        novelBean.url = url;
        if (!url.contains(API)) {
            url = API + url;
        }
        Document document = Jsoup.connect(url).header("User-Agent", NetUtils.getRandomUA()).get();
        Elements mainInfo = document.select("#maininfo");
        novelBean.novelName = mainInfo.select("#info > h1").text();
        novelBean.author = mainInfo.select("#info > p:nth-child(2)").text().split("：", 2)[1];
        novelBean.lastUpdateTime = mainInfo.select("#info > p:nth-child(4)").text().split("：", 2)[1];
        novelBean.intro = mainInfo.select("#intro").text();
        novelBean.lastChapter = new ChapterBean();
        novelBean.lastChapter.chapterName = mainInfo.select("#info > p:nth-child(5) > a").text();
        novelBean.lastChapter.url = mainInfo.select("#info > p:nth-child(5) > a").attr("href");
        novelBean.coverUrl = document.select("#fmimg > img").attr("src");
        // 接下来解析章节目录信息
        Elements elements = document.select("#list > dl > dd > a");
        for (Element element : elements) {
            ChapterBean chapterBean = new ChapterBean();
            chapterBean.chapterName = element.text();
            chapterBean.url = element.attr("href");
            novelBean.chapterList.add(chapterBean);
        }
        return novelBean;
    }

    public NovelBean getNovel(SearchResultBean searchResultBean) throws IOException {
        return getNovel(searchResultBean.url);
    }

    public NovelBean getNovel(long novelId) throws IOException {
        return getNovel("/book/" + novelId + "/");
    }

    public ChapterBean getChapContent(ChapterBean chapter) throws IOException {
        String url;
        if (chapter.url.contains(API)) {
            url = chapter.url;
        } else {
            url = API + chapter.url;
        }
        Document document = Jsoup.connect(url).header("User-Agent", NetUtils.getRandomUA()).get();
        Elements paras = document.select("#content > p");
        StringBuilder builder = new StringBuilder();
        for (Element para : paras) {
            builder.append("\t").append(para.text()).append("\n");
        }
        chapter.content = builder.toString();
        return chapter;
    }

}
