package com.it.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;

/**
 * 分类分类实体类
 *
 * @author itdragon
 */
@Data
@TableName("gm_course")
public class Course implements Serializable {
    /**
     * 自增长主键
     */
    @TableId(value = "id", type = IdType.UUID)
    private String id;
    /**
     * 分类名称
     */
    private String name;
    /**
     * 英文
     */
    private String english;
    /**
     * 颜色
     */
    private String color;

}