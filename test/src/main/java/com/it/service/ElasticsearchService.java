package com.it.service;

import com.it.entity.Log;
import com.it.entity.vo.LogVO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

/**
 * @author zhuminghang
 * @since 2020/4/22 15:44
 */
public interface ElasticsearchService {
    boolean save(Log log) throws IOException;

    LogVO selectLogByPage(String datetime,String userName, Integer pageNo, Integer pageSize);

    void oprateLogExport(String date,String userName,HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException;
}
