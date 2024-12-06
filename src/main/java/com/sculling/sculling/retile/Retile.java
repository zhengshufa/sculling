package com.sculling.sculling.retile;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Node;

import java.io.*;
import java.util.List;
import java.util.Scanner;

/**
 * @description Created by jwing on 2018/2/5.
 */
public class Retile {

    public static void main(String[] args) {
        IChapterInterImpl ChapterInterImpl = new IChapterInterImpl();
        //https://www.dmzshipin.com/book/7164.html  105
        List<Chapter> chapterList = ChapterInterImpl.getChapter("https://www.biqusa.com/0_116823/");
        while (true) {
            System.out.println(chapterList.size());
            Scanner scanner = new Scanner(System.in);
            Chapter chapter;
            int index;
            index = Integer.valueOf(scanner.nextLine());
            index = index + 1000;
            chapter = chapterList.get(index);
            System.out.println(chapter.getTitle());
            String url = chapter.getUrl();
            System.out.println(url);
            Get_Url(url);
            System.out.println(index);

        }
    }

    public static void Get_Url(String url) {
        if (url.startsWith("/")) {
            try {
                Document doc = Jsoup.connect("https://www.biqusa.com" + url)
                        //.data("query", "Java")
                        //.userAgent("头部")
                        //.cookie("auth", "token")
                        //.timeout(3000)
                        //.post()
                        .get();
                //得到html的所有东西

                Node node = doc.childNode(1);
                node = node.childNode(2);
                node = node.childNode(1);
                node = node.childNode(9);
                node = node.childNode(1);
                node = node.childNode(1);
                node = node.childNode(7);
                //分离出html下<a>...</a>之间的所有东西
                List<Node> links = node.childNodes();
                // class等于masthead的div标签
                for (Node link : links) {
                    //得到<a>...</a>里面的网址
                    String value = link.toString();
                    value = value.replaceAll("<p>","");
                    value = value.replaceAll("</p>","");
                    value = value.replaceAll("<br>","");
                    value = value.replaceAll("&nbsp;"," ");
                    if("".equals(value.trim())) {
                        continue;
                    }
                    if(value.length()>90){
                        for(int i=0;i<value.length()/90;i++) {
                            System.out.println(value.substring(i*90,90*(i+1)));
                        }
                        System.out.println(value.substring((value.length()/90)*90));
                    }else{
                        System.out.println(value);
                    }
                    if (value.contains("href")) {
                        Get_Url(value.substring(value.indexOf("href")+6,value.indexOf("href")+22));
                    }
                }
            } catch (IOException e) {
                Get_Url(url);
                e.printStackTrace();
            }
        }
    }

}
