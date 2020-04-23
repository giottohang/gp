package com.it.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;

/**
 * 数据分类实体类
 *
 * @author itdragon
 */
@Data
@TableName("gm_book")
public class Book implements Serializable {
    /**
     * 自增长主键
     */
    @TableId(value = "id", type = IdType.UUID)
    private String id;
    /**
     * 子分类名称
     */
    private String name;
    /**
     * 分类分类id
     */
    private String ptId;

}