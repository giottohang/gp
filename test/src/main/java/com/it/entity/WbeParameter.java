package com.it.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;

/**
 * 网站参数实体类
 *
 * @author itdragon
 */
@Data
@TableName("gm_wbeParameter")
public class WbeParameter implements Serializable {
    /**
     * 自增长主键
     */
    @TableId(value = "id", type = IdType.UUID)
    private String id;
    /**
     * 系统名称设置
     */
    private String name;
    /**
     * 版本号
     */
    private String code;
    /**
     * 域名
     */
    private String yu;
    /**
     * 版权
     */
    private String copyright;
    /**
     * 联系方式
     */
    private String iphone;
    /**
     * 版权年限
     */
    private String year;
    /**
     * 关于我们
     */
    private String aboutMe;
    /**
     * 关于我们图片
     */
    private String aboutImg;
}