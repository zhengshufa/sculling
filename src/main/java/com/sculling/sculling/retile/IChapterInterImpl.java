package com.sculling.sculling.retile;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * @description Created by jwing on 2018/2/5.
 */
public class IChapterInterImpl implements IChapterInter {

    protected String crawl(String url) throws Exception {
        //采用HttpClient技术
        try {
            CloseableHttpClient httpClient = HttpClientBuilder.create().build();
            CloseableHttpResponse httpResponse = httpClient.execute(new HttpGet(url));
            String result = EntityUtils.toString(httpResponse.getEntity());
            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<Chapter> getChapter(String url) {
        try {
            Document doc = Jsoup.connect(url).get();
            Elements as = doc.select("a");
            List<Chapter> chapters = new ArrayList<Chapter>();
            for (Element a : as) {
                if (a.attr("href").contains("html") || a.attr("href").contains("htm")) {
                    Chapter chapter = new Chapter();
                    chapter.setTitle(a.text());
                    chapter.setUrl(a.attr("href"));
                    chapters.add(chapter);
                }
            }
            return chapters;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        String s = "/D:/workspace/device-control/target/classes/com/device/control/lang/utils/sdk/haikang/";
        System.out.println(s.replaceFirst("/",""));
    }
}
