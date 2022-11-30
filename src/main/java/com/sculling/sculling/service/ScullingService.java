package com.sculling.sculling.service;


import com.sculling.sculling.domain.Message;
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
import java.util.List;

@Service
@Slf4j
public class ScullingService {

    String baseUrl = "";


    List<String> urlList = new ArrayList<String>();

    public Message list(String data)  {
        try{
            if(!data.isEmpty()){
                baseUrl = "https://www.biqusa.org"+ URLDecoder.decode(data);
                Document d = Jsoup.connect(baseUrl).get();
                Elements es = d.getElementById("list").getElementsByTag("a");
                urlList.clear();
                for(Element e : es){
                    urlList.add(e.attr("href"));
                }
                log.info("size:{}",urlList.size());
            }
            return new Message(0,"success",null);
        }catch (IOException e){
            e.printStackTrace();
            return new Message(1,"failed",e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


    public Message list2(String data)  {
        try{
            if(!data.isEmpty()){
                baseUrl = "http://m.xitongliu.cn";
                Document d = Jsoup.connect(baseUrl + URLDecoder.decode(data)).get();
                Elements es = d.getElementById("chapterlist").getElementsByTag("a");
                urlList.clear();
                for(Element e : es){
                    urlList.add(e.attr("href"));
                }
                log.info("size:{}",urlList.size());
            }
            return new Message(0,"success",null);
        }catch (IOException e){
            e.printStackTrace();
            return new Message(1,"failed",e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public Message list3(String data)  {
        try{
            if(!data.isEmpty()){
                baseUrl = "https://www.ebookbao.org";
                Document d = Jsoup.connect(baseUrl + URLDecoder.decode(data)).get();
                Elements es = d.getElementById("list").getElementsByTag("a");
                urlList.clear();
                for(Element e : es){
                    urlList.add(e.attr("href"));
                }
                log.info("size:{}",urlList.size());
            }
            return new Message(0,"success",null);
        }catch (IOException e){
            e.printStackTrace();
            return new Message(1,"failed",e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public Message list4(String data)  {
        try{
            if(!data.isEmpty()){
                baseUrl = "https://www.nchdzx.com";
                log.info(baseUrl + URLDecoder.decode(data));
                Connection c = Jsoup.connect(baseUrl + URLDecoder.decode(data));
                Document d = c.get();

                Elements es = d.getElementsByTag("dd").tagName("a");
                urlList.clear();
                for(Element e : es){
                    urlList.add(e.tagName("a").attr("href"));
                    log.info(e.tagName("a").attr("href"));
                }
                log.info("size:{}",urlList.size());
            }
            return new Message(0,"success",null);
        }catch (IOException e){
            e.printStackTrace();
            return new Message(1,"failed",e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


    public Message sculling(int index){
        try{
            log.info(baseUrl + urlList.get(index));
            Document d = Jsoup.connect(baseUrl + urlList.get(index).split("/")[2]).get();
            log.info("title:{},index:{}",d.title(),index);
            Element e = d.getElementById("content");
            String text = e.text();
            return new Message(0,"success:" + d.title(),text);
        }catch (IOException e){
            e.printStackTrace();
            return new Message(1,"failed",e.getMessage());
        }

    }

    public Message sculling2(int index){
        try{
            log.info(baseUrl + urlList.get(index));
            Document d = Jsoup.connect(baseUrl + urlList.get(index)).get();
            log.info("title:{},index:{}",d.title(),index);
            Element e = d.getElementById("content");
            String text = e.text();
            return new Message(0,"success:" + d.title(),text);
        }catch (IOException e){
            e.printStackTrace();
            return new Message(1,"failed",e.getMessage());
        }

    }

    public Message sculling3(int index){
        try{
            log.info(baseUrl + urlList.get(index));
            Document d = Jsoup.connect(baseUrl + urlList.get(index)).get();
            log.info("title:{},index:{}",d.title(),index);
            Element e = d.getElementById("content");
            String text = e.text();
            return new Message(0,"success:" + d.title(),text);
        }catch (IOException e){
            e.printStackTrace();
            return new Message(1,"failed",e.getMessage());
        }

    }

    public Message sculling4(int index){
        try{
            log.info(baseUrl + urlList.get(index));
            Document d = Jsoup.connect(baseUrl + urlList.get(index)).get();
            log.info("title:{},index:{}",d.title(),index);
            Element e = d.getElementById("content");
            String text = e.text();
            return new Message(0,"success:" + d.title(),text);
        }catch (IOException e){
            e.printStackTrace();
            return new Message(1,"failed",e.getMessage());
        }

    }
}
