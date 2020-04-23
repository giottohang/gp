package com.it.mapper;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.it.entity.Video;
import com.it.entity.vo.DataAnalysis;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
/**
 * 数据访问层
 */
public interface VideoMapper extends BaseMapper<Video> {
    /**
     * 获取人气高的作品
     */
    List<Video> selectPopularList();

    List<DataAnalysis> selectDataAnalysis();

}
