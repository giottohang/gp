package com.it.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("gm_likeNumber")
public class LikeNumber implements Serializable {
    /**
     * 自增长主键
     */
    @TableId(value = "id", type = IdType.UUID)
    private String id;
    /**
     * 点赞用户id
     */
    private String userId;
    /**
     * 点赞视频id
     */
    private String videoId;
    /**
     * 类型
     */
    private String type;

}