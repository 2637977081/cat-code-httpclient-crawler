package com.cat.code.http.client.crawler.HttpUtil;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

/**
 * @Author: lvgang
 * @Time: 2019/8/19 16:12
 * @Email: lvgang@golaxy.cn
 * @Description: Http工具类
 */
public class HttpUtil {

    public static synchronized <T> HttpEntity<T> getRequestBody(T requestStr) {
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        return new HttpEntity<>(requestStr, headers);
    }
}
