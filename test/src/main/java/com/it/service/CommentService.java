package com.it.service;

import com.it.entity.Comment;
import com.it.entity.LikeNumber;

import java.util.List;

public interface CommentService {
    /**
     * 新增
     *
     * @param comment
     * @return
     */
    boolean insert(Comment comment);

    boolean update(Comment comment);

    List<Comment> getList(String videoId);

    List<Comment> getListByUserId(String userId);

    /**
     * 删除
     */
    boolean delById(String id);

    Comment getOne(String id);


    Integer getCommentNum(String videoId);


    /**
     * 点赞功能
     *
     * @param likeNumber
     * @return
     */
    String giveALike(LikeNumber likeNumber);

    /**
     * 根据视频id查询该点赞数目
     */
    Integer getLikeNumber(String videoId,String type);

}
