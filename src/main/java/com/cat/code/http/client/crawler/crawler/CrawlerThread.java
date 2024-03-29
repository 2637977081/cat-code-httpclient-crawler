package com.cat.code.http.client.crawler.crawler;

import com.cat.code.http.client.crawler.HttpUtil.HttpUtil;
import com.cat.code.http.client.crawler.crawler.parser.PageParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Author: lvgang
 * @Time: 2019/11/12 15:05
 * @Email: lvgang@golaxy.cn
 * @Description: todo
 */
public class CrawlerThread implements Runnable {

    private final Logger logger = LoggerFactory.getLogger(CrawlerThread.class);

    private CrawlerMetaData crawlerMetaData;

    private boolean running;
    private boolean toStop;

    public CrawlerThread(CrawlerMetaData crawlerMetaData){
        this.crawlerMetaData = crawlerMetaData;
        this.running = true;
        this.toStop = false;
    }

    @Override
    public void run() {
        while (!this.toStop){
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                if(crawlerMetaData.getRestTemplate()!=null){
                    RestTemplate restTemplate = crawlerMetaData.getRestTemplate();
                    //获取url
                    String url = crawlerMetaData.getRunData().getUrl();
                    if(StringUtils.isEmpty(url)){
                        break;
                    }
                    //获取请求参数
                    Map<String,Object> params = crawlerMetaData.getParamMap();
                    HttpEntity<String> requestEntity = null;
                    //设置请求头
                    HttpHeaders requestHeaders = new HttpHeaders();
                    if(crawlerMetaData.getHeaderMap()!=null){
                        for(Map.Entry<String,String> entry:crawlerMetaData.getHeaderMap().entrySet()){
                             requestHeaders.add(entry.getKey(),entry.getValue());
                        }
                    }
                    String body = null;
                    if(crawlerMetaData.getParamMap()!=null){
                        body = objectMapper.writeValueAsString(crawlerMetaData.getParamMap());
                    }
                    requestEntity = new HttpEntity<String>(body, requestHeaders);
                    ResponseEntity<String> response = null;

                     try {
                         //请求类别
                         if (crawlerMetaData.isPost()) {
//                             response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class, params);
                             response = restTemplate.postForEntity(url,requestEntity,String.class);
                         } else {
                             response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
//                             response = restTemplate.getForEntity(url,requestEntity, String.class);
                         }
                        }catch (Exception e){
                            Thread.sleep(2000);
                            e.printStackTrace();
                        }
                    PageParser pageParser = crawlerMetaData.getPageParser();
                    pageParser.parse(url,response.getBody());

                    if (this.crawlerMetaData.getPauseMillis() > 0) {
                        try {
                            TimeUnit.MILLISECONDS.sleep((long)this.crawlerMetaData.getPauseMillis());
                        } catch (InterruptedException var5) {
                            logger.info(">>>>>>>>>>> xxl com.cat.code.http.client.crawler thread is interrupted. 2{}", var5.getMessage());
                        }
                    }
                }else {
                    throw new RuntimeException("restTemplate is null");
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
