package com.it.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.it.entity.Collect;
import com.it.entity.Collections;
import com.it.entity.RemindMessage;
import com.it.entity.Video;
import com.it.entity.vo.DataAnalysis;
import com.it.entity.vo.RemindMessageVO;

import java.util.List;

public interface VideoService {
    /**
     * 分页查询
     *
     * @param video
     * @param page
     * @param limit
     * @return
     */
    Page<Video> selectPage(Video video, int page, int limit);

    /**
     * 新增
     *
     * @param video
     * @return
     */
    boolean insert(Video video);

    /**
     * 编辑
     *
     * @param video
     * @return
     */
    boolean updataById(Video video);

    /**
     * 删除
     */
    boolean delById(String id);

    Video getOne(String id);

    /**
     * 获取最新上传
     */
    List<Video> selectLatestList(String ptId);
    List<Video> selectListByUser(String userName);
    List<Video> selectCollectionsListByUser(String userId);
    /**
     * 关注
     * @param followId
     * @return
     */
    boolean insertCollect(String followId);

    /**
     * 取消搜藏
     * @param followId
     * @return
     */
    boolean delCollect(String followId);
    /**
     * 判断当前用户是否关注
     */
    boolean isCollect(String followId);
    /**
     * 得到关注集合
     */
    List<Collect> getCollectList();
    /**
     * 收藏
     * @param videoId
     * @return
     */
    boolean insertCollections(String videoId);

    /**
     * 取消搜藏
     * @param videoId
     * @return
     */
    boolean delCollections(String videoId);
    /**
     * 判断当前作品是否收藏
     */
    boolean isCollections(String videoId);
    /**
     * 得到收藏集合
     */
    List<Collections> getCollectionsList();
    /**
     * 获取人气高的作品
     */
    List<Video> selectPopularList();

    List<RemindMessageVO> selectUnreadList();

    List<DataAnalysis> selectDataAnalysis();
}
