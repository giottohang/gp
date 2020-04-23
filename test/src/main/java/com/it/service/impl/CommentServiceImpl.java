package com.it.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.it.entity.Comment;
import com.it.entity.LikeNumber;
import com.it.mapper.CommentMapper;
import com.it.mapper.LikeNumberMapper;
import com.it.mapper.VideoMapper;
import com.it.service.CommentService;
import com.it.util.DateUtil;
import com.it.util.ItdragonUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 *
 * @author
 * @create 2019/2/15 17:53
 * @since 1.0.0
 */
@Service
public class CommentServiceImpl implements CommentService {
    @Resource
    private CommentMapper commentMapper;
    @Resource
    private ItdragonUtils itdragonUtils;
    @Resource
    private VideoMapper videoMapper;
    @Resource
    private LikeNumberMapper likeNumberMapper;


    @Override
    public boolean insert(Comment comment) {
        comment.setUserId(itdragonUtils.getSessionUser().getId());
        comment.setTime(DateUtil.getNowDateSS());
        Integer insert = commentMapper.insert(comment);
        if (insert > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean update(Comment comment) {
        Integer update = commentMapper.updateById(comment);
        if (update > 0) {
            return true;
        }
        return false;
    }

    @Override
    public List<Comment> getList(String videoId) {
        EntityWrapper<Comment> wrapper = new EntityWrapper<>();
        wrapper.eq("videoId", videoId);
        return commentMapper.selectList(wrapper);
    }

    @Override
    public List<Comment> getListByUserId(String userId) {
        EntityWrapper<Comment> wrapper = new EntityWrapper<>();
        wrapper.eq("userId", userId);
        List<Comment> commentList = commentMapper.selectList(wrapper);
        if (commentList.isEmpty()) {
            return null;
        }
        return commentList;


    }

    @Override
    public boolean delById(String id) {
        Integer delete = commentMapper.deleteById(id);
        if (delete > 0) {
            return true;
        }
        return false;
    }

    @Override
    public Comment getOne(String id) {
        return commentMapper.selectById(id);
    }

    @Override
    public Integer getCommentNum(String videoId) {
        EntityWrapper<Comment> wrapper = new EntityWrapper<>();
        wrapper.eq("videoId", videoId);
        List<Comment> comments = commentMapper.selectList(wrapper);
        return comments.size();
    }

    @Override
    public String giveALike(LikeNumber likeNumber) {
        //查询当前用户是否对当前视频点过赞
        likeNumber.setUserId(itdragonUtils.getSessionUser().getId());
        String type=likeNumber.getType();
        likeNumber.setType(null);
        LikeNumber selectOne = likeNumberMapper.selectOne(likeNumber);
        likeNumber.setType(type);
        String result = "default";
        if (selectOne != null) {
            if (selectOne.getType().equals(type)){
                //如果点过赞,删除用户的点赞记录
                EntityWrapper<LikeNumber> wrapper = new EntityWrapper<>();
                wrapper.eq("userId", likeNumber.getUserId());
                wrapper.eq("videoId", likeNumber.getVideoId());
                Integer delete = likeNumberMapper.delete(wrapper);
                if (delete > 0) {
                    result = "callOff";
                    return result;
                }
            }else {
                selectOne.setType(type);
                Integer update=likeNumberMapper.updateById(selectOne);
                if (update > 0) {
                    result = "success";
                    return result;
                }
            }

        } else {
            //如果没点过,添加一点点赞记录
            Integer insert = likeNumberMapper.insert(likeNumber);
            if (insert > 0) {
                result = "success";
                return result;
            }
        }
        return result;
    }

    @Override
    public Integer getLikeNumber(String videoId, String type) {
        EntityWrapper<LikeNumber> wrapper = new EntityWrapper<>();
        wrapper.eq("videoId", videoId);
        wrapper.eq("type", type);
        List<LikeNumber> likeNumbers = likeNumberMapper.selectList(wrapper);
        if (likeNumbers.isEmpty()) {
            return 0;
        } else {
            return likeNumbers.size();
        }
    }
}