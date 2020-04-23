package com.it.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 视频实体类
 *
 * @author itdragon
 */
@Data
@TableName("gm_video")
public class Video implements Serializable {
    /**
     * 自增长主键
     */
    @TableId(value = "id", type = IdType.UUID)
    private String id;
    /**
     * 标题
     */
    private String title;
    /**
     * 文件
     */
    private String url;
    /**
     * 封面图
     */
    private String img;
    /**
     * 上传时间
     */
    private String time;
    /**
     * 上传人
     */
    private String userName;
    /**
     * 资料介绍
     */
    private String info;

    @TableField(exist = false)
    private Integer likeNum;
    @TableField(exist = false)
    private Integer noLikeNum;
    /**
     * 分类id
     */
    private String ptId;
    /**
     * 子分类
     */
    private String chId;
    /**
     * 状态
     */
    private String state;
    private String type;
    @TableField(exist = false)
    private List<String> imgList;
    @TableField(exist = false)
    private String userId;
}