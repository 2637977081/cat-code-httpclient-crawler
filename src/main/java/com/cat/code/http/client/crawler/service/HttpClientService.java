package com.cat.code.http.client.crawler.service;

import com.cat.code.http.client.crawler.crawler.CrawlerMetaData;
import com.cat.code.http.client.crawler.crawler.HttpCrawler;
import com.cat.code.http.client.crawler.crawler.parser.PageParser;
import com.cat.code.http.client.crawler.crawler.rundata.RedisRunData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @Author: lvgang
 * @Time: 2019/11/12 14:46
 * @Email: lvgang@golaxy.cn
 * @Description: todo
 */
@Service
public class HttpClientService {

    private final Logger logger = LoggerFactory.getLogger(HttpClientService.class);

    @Autowired
    private RedisRunData redisRunData;

    @Autowired
    RestTemplate restTemplate;

    public void test(){


    }

    public void test2(){

    }

}
