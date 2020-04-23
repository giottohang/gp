package com.it.util;

import com.alibaba.fastjson.JSON;
import com.it.entity.Log;
import org.apache.http.HttpHost;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author 朱明航
 * @since 2020/3/27 10:50
 */
@Component
public class LogEsUtil {

    //打开连接
    public RestHighLevelClient linkEs() {
        String HOST = "127.0.0.1";
        int port = 9200;
        String indexName = "operate_log";
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost(HOST, port, "http")
                ));
        return client;
    }

    //关闭连接
    public void closeEs(RestHighLevelClient client) {
        try {
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void delete(String id, String indexName, RestHighLevelClient client) throws IOException {
        DeleteRequest request = new DeleteRequest(indexName, String.valueOf(id));
        DeleteResponse deleteResponse = client.delete(request, RequestOptions.DEFAULT);
    }

    public void insert(Log log, String indexName, RestHighLevelClient client) throws IOException {
        IndexRequest request = new IndexRequest(indexName);
        request.id(log.getId().toString());
        request.source(JSON.toJSONString(log), XContentType.JSON);
        client.index(request, RequestOptions.DEFAULT);
    }

    public String get(String id, String indexName, RestHighLevelClient client) throws IOException {
        GetRequest getRequest = new GetRequest(indexName, id);
        GetResponse getResponse = client.get(getRequest, RequestOptions.DEFAULT);
        String str = getResponse.getSourceAsString();
        System.out.println(str);
        client.close();
        return str;
    }
}
