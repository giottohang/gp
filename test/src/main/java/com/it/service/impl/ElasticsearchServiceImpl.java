package com.it.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.it.entity.Log;
import com.it.entity.vo.LogVO;
import com.it.service.ElasticsearchService;
import com.it.util.ExcelUtil;
import com.it.util.LogEsUtil;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static org.elasticsearch.index.query.QueryBuilders.boolQuery;

/**
 * @author zhuminghang
 * @since 2020/4/22 15:46
 */
@Service
@Slf4j
public class ElasticsearchServiceImpl implements ElasticsearchService {
    @Autowired
    private LogEsUtil logEsUtil;

    @Override
    public boolean save(Log log) throws IOException {
        RestHighLevelClient client = logEsUtil.linkEs();
        try {
            logEsUtil.insert(log,"operate_log",client);
            return true;
        }catch (Exception e){
          return false;
        }finally {
            logEsUtil.closeEs(client);
        }
    }

    @Override
    public LogVO selectLogByPage(String datetime,String userName, Integer pageNo, Integer pageSize) {
        LogVO logVO=new LogVO();
        RestHighLevelClient client = null;
        try {
            // 开启连接
            client = logEsUtil.linkEs();
            // 设置查询条件
            SearchRequest searchRequest = new SearchRequest("operate_log");
            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
            BoolQueryBuilder queryBuilder = boolQuery();
            if (userName!=null&&!"".equals(userName)){
                queryBuilder.must(QueryBuilders.termQuery("userName.keyword", userName));
            }
            RangeQueryBuilder rangequerybuilder = QueryBuilders.rangeQuery("time.keyword");
            //获取开始时间和结束时间
            String beginTime=dealDateFormat(DateTime.parse(datetime).minuteOfDay().withMinimumValue().toString());
            String endTime=dealDateFormat(DateTime.parse(datetime).minuteOfDay().withMaximumValue().toString());

            rangequerybuilder.gte(beginTime).lte(endTime);
            queryBuilder.must(rangequerybuilder);
            sourceBuilder.query(queryBuilder);
            searchRequest.source(sourceBuilder);
            SearchResponse searchResponseForTotal = client.search(searchRequest, RequestOptions.DEFAULT);
            long totalHits = searchResponseForTotal.getHits().getTotalHits().value;
            // 分页操作，默认从第一页开始，默认页面大小为50
            if (pageSize == null) {
                pageSize = 30;
            }
            if (pageNo == null) {
                pageNo = 1;
            }
            Integer from = (pageNo - 1) * pageSize;
            if (from <= 0) {
                from = 0;
            }
            // 设置按时间排序
            sourceBuilder.query(queryBuilder).sort("time.keyword", SortOrder.ASC);
            sourceBuilder.from(from);
            sourceBuilder.size(pageSize);
            searchRequest.source(sourceBuilder);
            SearchResponse searchResponse = client.search(searchRequest,RequestOptions.DEFAULT);

            List<Log> list = new ArrayList<>();
            SearchHits hits = searchResponse.getHits();
            for (SearchHit hit : hits) {
                Log log = JSONObject.parseObject(hit.getSourceAsString(), Log.class);
                list.add(log);
            }
            logVO.setLogList(list);
            logVO.setTotalNum(totalHits);
        } catch (Exception e) {
            log.info("es查询操作日志异常");
        } finally {
            // 关闭连接
            logEsUtil.closeEs(client);
        }
        return logVO;
    }

    @Override
    public void oprateLogExport(String date,String userName, HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {
        RestHighLevelClient client = null;
        // 开启连接
        client = logEsUtil.linkEs();
        // 设置查询条件
        SearchRequest searchRequest = new SearchRequest("operate_log");
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder queryBuilder = boolQuery();
        if (userName!=null&&!"".equals(userName)){
            queryBuilder.must(QueryBuilders.termQuery("userName.keyword", userName));
        }
        RangeQueryBuilder rangequerybuilder = QueryBuilders.rangeQuery("time.keyword");
        //获取开始时间和结束时间
        String beginTime=dealDateFormat(DateTime.parse(date).minuteOfDay().withMinimumValue().toString());
        String endTime=dealDateFormat(DateTime.parse(date).minuteOfDay().withMaximumValue().toString());

        rangequerybuilder.gte(beginTime).lte(endTime);
        queryBuilder.must(rangequerybuilder);
        sourceBuilder.query(queryBuilder).sort("time.keyword", SortOrder.ASC);
        searchRequest.source(sourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        List<Log> list = new ArrayList<>();
        SearchHits hits = searchResponse.getHits();
        for (SearchHit hit : hits) {
            Log log = JSONObject.parseObject(hit.getSourceAsString(), Log.class);
            list.add(log);
        }
        try {
//            ExcelUtils.exportXSSFWithTemplate(list, "excel/OprateLog.xlsx", "OprateLog", request, response);
            ExcelUtil.export(response,list,"OprateLog",Log.class);
        } catch (IOException | IllegalAccessException e) {

        }finally {
            // 关闭连接
            logEsUtil.closeEs(client);
        }
    }

    private String dealDateFormat(String oldDateStr) throws ParseException {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");  //yyyy-MM-dd'T'HH:mm:ss.SSSZ
        Date date = df.parse(oldDateStr);
        SimpleDateFormat df1 = new SimpleDateFormat ("EEE MMM dd HH:mm:ss Z yyyy", Locale.UK);
        Date date1 =  df1.parse(date.toString());
        DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df2.format(date1);
    }

}
