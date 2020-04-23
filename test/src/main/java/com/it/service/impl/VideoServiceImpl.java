package com.it.service.impl;

import com.alibaba.druid.sql.visitor.functions.Now;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.it.entity.Collect;
import com.it.entity.Collections;
import com.it.entity.RemindMessage;
import com.it.entity.User;
import com.it.entity.Video;
import com.it.entity.vo.DataAnalysis;
import com.it.entity.vo.RemindMessageVO;
import com.it.mapper.CollectMapper;
import com.it.mapper.CollectionsMapper;
import com.it.mapper.RemindMessageMapper;
import com.it.mapper.UserMapper;
import com.it.mapper.VideoMapper;
import com.it.service.VideoService;
import com.it.util.DateUtil;
import com.it.util.ItdragonUtils;
import com.mysql.jdbc.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 〈视频接口〉<br>
 *
 * @author
 * @create2020/2/15 17:53
 * @since 1.0.0
 */
@Service
public class VideoServiceImpl implements VideoService {
    @Resource
    private VideoMapper videoMapper;
    @Resource
    private ItdragonUtils itdragonUtils;
    @Resource
    private CollectMapper collectMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private CollectionsMapper collectionsMapper;
    @Resource
    private RemindMessageMapper remindMessageMapper;


    @Override
    public Page<Video> selectPage(Video video, int page, int limit) {
        EntityWrapper<Video> searchInfo = new EntityWrapper<>();
        if (ItdragonUtils.stringIsNotBlack(video.getTitle())) {
            searchInfo.like("title", video.getTitle());
        }
        if (ItdragonUtils.stringIsNotBlack(video.getUserName())) {
            searchInfo.like("userName", video.getUserName());
        }
        searchInfo.orderBy("time desc");
        Page<Video> pageInfo = new Page<>(page, limit);
        List<Video> resultList = videoMapper.selectPage(pageInfo, searchInfo);
        if (!resultList.isEmpty()) {
            pageInfo.setRecords(resultList);
        }
        return pageInfo;
    }


    @Override
    public boolean insert(Video video) {
        video.setTime(DateUtil.getNowDateSS());
        video.setUserName(itdragonUtils.getSessionUser().getUserName());
        video.setState("1");
        Integer insert = videoMapper.insert(video);
        if (insert > 0) {
            RemindMessage remindMessage=new RemindMessage();
            remindMessage.setReadFlag(0);
            User user1=new User();
            user1.setUserName(video.getUserName());
            User user =userMapper.selectOne(user1);
            remindMessage.setUpdateId(user.getId());
            remindMessage.setVideoId(video.getId());
            remindMessage.setCreateTime(DateUtil.getNowDateSS());
            //新增更新信息
            remindMessageMapper.insert(remindMessage);
            return true;
        }
        return false;
    }

    @Override
    public boolean updataById(Video video) {
        Integer insert = videoMapper.updateById(video);
        if (insert > 0) {
            return true;
        }
        return false;
    }


    @Override
    public boolean delById(String id) {
        Integer delete = videoMapper.deleteById(id);
        if (delete > 0) {
            return true;
        }
        return false;
    }

    @Override
    public Video getOne(String id) {
        return videoMapper.selectById(id);
    }

    @Override
    public List<Video> selectLatestList(String ptId) {
        EntityWrapper<Video> searchInfo = new EntityWrapper<>();
        searchInfo.orderBy("time desc");
        if (ItdragonUtils.stringIsNotBlack(ptId)) {
            searchInfo.eq("ptId", ptId);
        }
        List<Video> resultList = videoMapper.selectList(searchInfo);
        if (resultList.isEmpty()) {
            return new ArrayList<>();
        }
        return resultList;
    }

    @Override
    public List<Video> selectListByUser(String userName) {

        EntityWrapper<Video> searchInfo = new EntityWrapper<>();
        searchInfo.orderBy("time desc");
        if (ItdragonUtils.stringIsNotBlack(userName)) {
            searchInfo.eq("userName", userName);
        }
        List<Video> resultList = videoMapper.selectList(searchInfo);
        if (resultList.isEmpty()) {
            return new ArrayList<>();
        }
        return resultList;
    }

    @Override
    public List<Video> selectCollectionsListByUser(String userId) {
        EntityWrapper<Collections> wrapper = new EntityWrapper<>();
        wrapper.eq("userId",userId);
        List<Collections> collectionsList=collectionsMapper.selectList(wrapper);
        List<Video> resultList = new ArrayList<>();
        collectionsList.stream().forEach(x->{
            Video videoSearch=new Video();
            videoSearch.setId(x.getVideoId());
           Video video= videoMapper.selectOne(videoSearch);
            if (video!=null){
                resultList.add(video);
            }
        });
        resultList.stream().sorted(Comparator.comparing(Video::getTime)).collect(Collectors.toList());
        if (resultList.isEmpty()) {
            return new ArrayList<>();
        }
        return resultList;
    }

    @Override
    public boolean insertCollect(String followId) {
        Collect collect = new Collect();
        collect.setUserId(itdragonUtils.getSessionUser().getId());
        //查询该作品的作者
        collect.setFollowId(followId);
        Integer insert = collectMapper.insert(collect);
        if (insert > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean delCollect(String followId) {
        EntityWrapper<Collect> wrapper = new EntityWrapper<>();
        wrapper.eq("followId", followId);
        wrapper.eq("userId", itdragonUtils.getSessionUser().getId());
        Integer delete = collectMapper.delete(wrapper);
        if (delete > 0) {
            return false;
        }
        return false;
    }

    @Override
    public boolean isCollect(String followId) {
        Collect collect = new Collect();
        collect.setFollowId(followId);
        collect.setUserId(itdragonUtils.getSessionUser().getId());
        Collect one = collectMapper.selectOne(collect);
        if (one != null) {
            return true;
        }
        return false;
    }

    @Override
    public List<Collect> getCollectList() {
        EntityWrapper<Collect> wrapper = new EntityWrapper<>();
        wrapper.eq("userId", itdragonUtils.getSessionUser().getId());
        List<Collect> collectList = collectMapper.selectList(wrapper);
        for (Collect collect : collectList) {
            User user = userMapper.selectById(collect.getFollowId());
            collect.setUser(userMapper.selectById(user));
        }
        return collectList;
    }

    @Override
    public boolean insertCollections(String videoId) {
        Collections collection = new Collections();
        collection.setUserId(itdragonUtils.getSessionUser().getId());
        //查询该作品的作者
        collection.setVideoId(videoId);
        Integer insert = collectionsMapper.insert(collection);
        if (insert > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean delCollections(String videoId) {
        EntityWrapper<Collections> wrapper = new EntityWrapper<>();
        wrapper.eq("videoId", videoId);
        wrapper.eq("userId", itdragonUtils.getSessionUser().getId());
        Integer delete = collectionsMapper.delete(wrapper);
        if (delete > 0) {
            return false;
        }
        return false;
    }

    @Override
    public boolean isCollections(String videoId) {
        Collections collection = new Collections();
        collection.setVideoId(videoId);
        collection.setUserId(itdragonUtils.getSessionUser().getId());
        Collections one = collectionsMapper.selectOne(collection);
        if (one != null) {
            return true;
        }
        return false;
    }

    @Override
    public List<Collections> getCollectionsList() {
        EntityWrapper<Collections> wrapper = new EntityWrapper<>();
        wrapper.eq("userId", itdragonUtils.getSessionUser().getId());
        List<Collections> collectList = collectionsMapper.selectList(wrapper);
        for (Collections collection : collectList) {
            User user = userMapper.selectById(collection.getVideoId());
            collection.setUser(userMapper.selectById(user));
        }
        return collectList;
    }

    @Override
    public List<Video> selectPopularList() {
        List<Video> resultList = videoMapper.selectPopularList();
        if (resultList.isEmpty()) {
            return new ArrayList<>();
        }
        return resultList;
    }

    @Override
    public List<RemindMessageVO> selectUnreadList() {
        List<RemindMessageVO> resultList=new ArrayList<>();
        EntityWrapper<Collect> wrapper = new EntityWrapper<>();
        String userId=itdragonUtils.getSessionUser().getId();
        wrapper.eq("userId",userId);
        List<Collect> collectionsList=collectMapper.selectList(wrapper);
        for (Collect collect : collectionsList){
            EntityWrapper<RemindMessage> wrapper1 = new EntityWrapper<>();
            wrapper1.eq("updateId",collect.getFollowId());
            wrapper1.eq("readFlag",0);
            List<RemindMessage> remindMessages= remindMessageMapper.selectList(wrapper1);
            RemindMessageVO remindMessageVO=new RemindMessageVO();
            remindMessages.stream().forEach(x->{
                BeanUtils.copyProperties(x,remindMessageVO);
                remindMessageVO.setUpdateName(userMapper.selectById(x.getUpdateId()).getUserName());
                x.setReadFlag(1);
                if (x!=null){
                    remindMessageMapper.updateById(x);
                }
            });
            if (remindMessageVO!=null){
                resultList.add(remindMessageVO);
            }
        }
        resultList=resultList.stream().sorted(Comparator.comparing(RemindMessageVO::getCreateTime)).collect(Collectors.toList());
        return resultList;
    }

    @Override
    public List<DataAnalysis> selectDataAnalysis() {
        List<DataAnalysis> dataAnalyses=videoMapper.selectDataAnalysis();
        if (dataAnalyses.isEmpty()){
            return null;
        }
        return dataAnalyses;
    }
}