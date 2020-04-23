package com.it.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;

/**
 * 数据库备份记录
 *
 * @author itdragon
 */
@Data
@TableName("gm_packUpInfo")
public class PackUpInfo implements Serializable {
    /**
     * 自增长主键
     */
    @TableId(value = "id", type = IdType.UUID)
    private String id;
    /**
     * 文件名
     */
    private String name;
    /**
     * 日期
     */
    private String time;
    /**
     * 地址
     */
    private String url;
    /**
     * 备份人
     */
    private String userName;
}