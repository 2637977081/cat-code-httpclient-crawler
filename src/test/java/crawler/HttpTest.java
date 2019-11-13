package crawler;

import com.cat.code.http.client.crawler.ServiceHttpClientCrawlerApplication;
import com.cat.code.http.client.crawler.crawler.CrawlerMetaData;
import com.cat.code.http.client.crawler.crawler.HttpCrawler;
import com.cat.code.http.client.crawler.crawler.parser.PageParser;
import com.cat.code.http.client.crawler.crawler.rundata.RedisRunData;
import com.cat.code.http.client.crawler.service.HttpClientService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author: lvgang
 * @Time: 2019/11/12 13:57
 * @Email: lvgang@golaxy.cn
 * @Description: todo
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ServiceHttpClientCrawlerApplication.class)
public class HttpTest {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private RedisRunData redisRunData;

    private final Logger logger = LoggerFactory.getLogger(HttpTest.class);

    @Autowired
    private HttpClientService httpClientService;

    @Test
    public void test1(){
        ExecutorService executor = Executors.newFixedThreadPool(100);
        for (int i=0;i<100;i++){
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        ResponseEntity<String> responseEntity =  restTemplate.getForEntity("https://www.dapp.review/api/dapp/dapp/11649/?dapp=11649&lang=zh",String.class);
                        System.out.println(responseEntity.getBody());
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
            });
        }
        //等待线程执行完毕
        executor.shutdown();
        while(!executor.isTerminated()) {
        }
    }

    @Test
    public void test2(){
        redisRunData.setTaskQueue("testHttpClient");
        for(int i=1000;i<2000;i++){
            String url = "https://www.dapp.review/api/dapp/dapp/1"+i+"?dapp=11649&lang=zh";
            redisRunData.addUrl(url);
        }

        CrawlerMetaData crawlerMetaData =new CrawlerMetaData.Builder()
                .setRunData(redisRunData)
                .setHeaderMap(null)
                .setIsPost(false)
                .setPauseMillis(2000)
                .setRestTemplate(restTemplate)
                .setThreadCount(5)
                .setPageParser(
                        new PageParser() {
                            @Override
                            public void parse(String url, String body) {
                                logger.info("response code:{},body:{}",url,body);
                            }
                        }
                )
                .build();
        HttpCrawler httpCrawler = new HttpCrawler(crawlerMetaData);
        httpCrawler.start(true);
    }

    @Test
    public void test3(){
        redisRunData.setTaskQueue("testHttpClient");
        for(int i=1;i<100;i++){
            String url = "http://www.51c1c.com/chp//listAllProductByCategory";
            redisRunData.addUrl(url);
        }

        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("category","上上签系列");
        paramMap.put("currentPage",2);
        paramMap.put("pageSize",15);

        Map<String,String> headerMap = new HashMap<>();
        headerMap.put("Accept", "application/json, text/plain, */*");
        headerMap.put("Content-Type", "application/json;charset=UTF-8");
        headerMap.put("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.97 Safari/537.36");

        CrawlerMetaData crawlerMetaData =new CrawlerMetaData.Builder()
                .setRunData(redisRunData)
                .setHeaderMap(null)
                .setIsPost(true)
                .setPauseMillis(2000)
                .setParamMap(paramMap)
                .setHeaderMap(headerMap)
                .setRestTemplate(restTemplate)
                .setThreadCount(5)
                .setPageParser(
                        new PageParser() {
                            @Override
                            public void parse(String url, String body) {
                                logger.info("response code:{},body:{}",url,body);
                            }
                        }
                )
                .build();
        HttpCrawler httpCrawler = new HttpCrawler(crawlerMetaData);
        httpCrawler.start(true);
    }
}
