package com.it.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

/**
 * @author zhuminghang
 * @since 2020/4/14 16:14
 */
@Data
@TableName("gm_collections")
public class Collections {
    /**
     * 自增长主键
     */
    @TableId(value = "id", type = IdType.UUID)
    private String id;
    /**
     * 用户id
     */
    private String userId;
    /**
     * 视频id
     */
    private String videoId;

    @TableField(exist = false)
    private User user;
}
