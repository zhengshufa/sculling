package com.sculling.sculling.service;


import com.sculling.sculling.domain.Message;
import com.sculling.sculling.novel.api.website.common.ChapterBean;
import com.sculling.sculling.novel.api.website.fanqie.NovelBean;
import com.sculling.sculling.novel.api.website.fanqie.SearchResultJson;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.springframework.stereotype.Service;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ScullingService {

    String baseUrl = "";


    List<String> urlList = new ArrayList<String>();

    public Message list(String data) {
        try {
            if (!data.isEmpty()) {
                baseUrl = "https://www.biqusa.org" + URLDecoder.decode(data);
                Document d = Jsoup.connect(baseUrl).get();
                Elements es = d.getElementById("list").getElementsByTag("a");
                urlList.clear();
                for (Element e : es) {
                    urlList.add(e.attr("href"));
                }
                log.info("size:{}", urlList.size());
            }
            return new Message(0, "success", null);
        } catch (IOException e) {
            e.printStackTrace();
            return new Message(1, "failed", e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


    public Message list2(String data) {
        try {
            if (!data.isEmpty()) {
                baseUrl = "http://m.xitongliu.cn";
                Document d = Jsoup.connect(baseUrl + URLDecoder.decode(data)).get();
                Elements es = d.getElementById("chapterlist").getElementsByTag("a");
                urlList.clear();
                for (Element e : es) {
                    urlList.add(e.attr("href"));
                }
                log.info("size:{}", urlList.size());
            }
            return new Message(0, "success", null);
        } catch (IOException e) {
            e.printStackTrace();
            return new Message(1, "failed", e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public Message list3(String data) {
        try {
            if (!data.isEmpty()) {
                baseUrl = "https://www.ebookbao.org";
                Document d = Jsoup.connect(baseUrl + URLDecoder.decode(data)).get();
                Elements es = d.getElementById("list").getElementsByTag("a");
                urlList.clear();
                for (Element e : es) {
                    urlList.add(e.attr("href"));
                }
                log.info("size:{}", urlList.size());
            }
            return new Message(0, "success", null);
        } catch (IOException e) {
            e.printStackTrace();
            return new Message(1, "failed", e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public Message list4(String data) {
        try {
            if (!data.isEmpty()) {
                baseUrl = "https://www.nchdzx.com";
                log.info(baseUrl + URLDecoder.decode(data));
                Connection c = Jsoup.connect(baseUrl + URLDecoder.decode(data));
                Document d = c.get();

                Elements es = d.getElementsByTag("dd").tagName("a");
                urlList.clear();
                for (Element e : es) {
                    urlList.add(e.childNode(0).attr("href"));
                }
                log.info("size:{}", urlList.size());
            }
            return new Message(0, "success", null);
        } catch (IOException e) {
            e.printStackTrace();
            return new Message(1, "failed", e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


    public Message sculling(int index) {
        try {
            log.info(baseUrl + urlList.get(index));
            Document d = Jsoup.connect(baseUrl + urlList.get(index).split("/")[2]).get();
            log.info("title:{},index:{}", d.title(), index);
            Element e = d.getElementById("content");
            String text = e.text();
            return new Message(0, "success:" + d.title(), text);
        } catch (IOException e) {
            e.printStackTrace();
            return new Message(1, "failed", e.getMessage());
        }

    }

    public Message sculling2(int index) {
        try {
            log.info(baseUrl + urlList.get(index));
            Document d = Jsoup.connect(baseUrl + urlList.get(index)).get();
            log.info("title:{},index:{}", d.title(), index);
            Element e = d.getElementById("content");
            String text = e.text();
            return new Message(0, "success:" + d.title(), text);
        } catch (IOException e) {
            e.printStackTrace();
            return new Message(1, "failed", e.getMessage());
        }

    }

    public Message sculling3(int index) {
        try {
            log.info(baseUrl + urlList.get(index));
            Document d = Jsoup.connect(baseUrl + urlList.get(index)).get();
            log.info("title:{},index:{}", d.title(), index);
            Element e = d.getElementById("content");
            String text = e.text();
            return new Message(0, "success:" + d.title(), text);
        } catch (IOException e) {
            e.printStackTrace();
            return new Message(1, "failed", e.getMessage());
        }

    }

    public Message sculling4(int index) {
        try {
            log.info(baseUrl + urlList.get(index));
            Document d = Jsoup.connect(baseUrl + urlList.get(index)).get();
            log.info("title:{},index:{}", d.title(), index);
            Element e = d.getElementById("content");
            String text = e.text();
            return new Message(0, "success:" + d.title(), text);
        } catch (IOException e) {
            e.printStackTrace();
            return new Message(1, "failed", e.getMessage());
        }

    }


    public Message search(String name) {
        try {
            FanqieWebsiteAPI api = new FanqieWebsiteAPI();
            SearchResultJson searchResultJson = api.search(name);
            List<String> result = searchResultJson.data.bookDataList.stream().map(o->{
                String res = "【名称：" + o.bookName + "简介：" + o.intro + "bookId:" + o.bookId + "】\n";
                return res;
            }).collect(Collectors.toList());
            return new Message(0, "success", result.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return new Message(1, "failed", e.getMessage());
        }
    }



    Map<String,List<ChapterBean>> menuMap = new HashMap<>();

    public Message list5(String bookId) {
        List<ChapterBean> chapterBeanList = new ArrayList<>();

        try {
            FanqieWebsiteAPI api = new FanqieWebsiteAPI();
            NovelBean novel = api.getNovel(bookId);
            for (NovelBean.Volume volume : novel.volumeList) {
                for (ChapterBean chapterBean : volume.chapterBeanList) {
                    log.info(chapterBean.chapterName,chapterBean.url);
                    chapterBeanList.add(chapterBean);
                }
            }
            menuMap.put(bookId,chapterBeanList);
            return new Message(0, "success", chapterBeanList.size() + "");
        } catch (Exception e) {
            e.printStackTrace();
            return new Message(1, "failed", e.getMessage());
        }
    }

    public Message sculling5(int index,String bookId) {
        try {
            ChapterBean chapContent = menuMap.get(bookId).get(index);
            return new Message(0, "success", chapContent.chapterName + chapContent.content);
        } catch (Exception e) {
            e.printStackTrace();
            return new Message(1, "failed", e.getMessage());
        }
    }
}
