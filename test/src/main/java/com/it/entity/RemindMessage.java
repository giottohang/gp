package com.it.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author zhuminghang
 * @since 2020/4/17 11:36
 */
@TableName("gm_remindMessage")
@Data
public class RemindMessage {
    @TableId
    private String Id;

    private String UpdateId;

    private String VideoId;

    private Integer ReadFlag;

    private String CreateTime;

}
