package com.it.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;

/**
 * 实体类
 *
 * @author itdragon
 */
@Data
@TableName("gm_log")
public class Log implements Serializable {
    /**
     * 自增长主键
     */
    @Excel(name = "Id", orderNum = "1")
    @TableId(value = "id", type = IdType.UUID)
    private String id;
    /**
     * 用户
     */
    @Excel(name = "用户名", orderNum = "2")
    private String userName;
    /**
     * 操作
     */
    @Excel(name = "操作", orderNum = "3")
    private String operation;
    /**
     * 时间
     */
    @Excel(name = "时间", orderNum = "4")
    private String time;
}