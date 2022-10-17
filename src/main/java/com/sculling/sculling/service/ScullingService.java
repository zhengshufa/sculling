package com.sculling.sculling.service;


import com.sculling.sculling.domain.Message;
import org.springframework.stereotype.Service;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ScullingService {

    String baseUrl = "";

    List<String> urlList = new ArrayList<String>();

    public Message list(String url,String bookId)  {
        try{
            baseUrl = url;
            Document d = Jsoup.connect(baseUrl + "/"+bookId+"/").get();
            Elements es = d.getElementById("list").getElementsByTag("a");
            for(Element e : es){
                urlList.add(e.attr("href"));
            }
            return new Message(0,"success",null);
        }catch (IOException e){
            e.printStackTrace();
            return new Message(1,"failed",e.getMessage());
        }

    }

    public Message sculling(int index){
        try{
            Document d = Jsoup.connect(baseUrl + urlList.get(index)).get();
            System.out.println(d.title());
            Element e = d.getElementById("content");
            String text = e.text();
            return new Message(0,"success",text);
        }catch (IOException e){
            e.printStackTrace();
            return new Message(1,"failed",e.getMessage());
        }

    }
}
