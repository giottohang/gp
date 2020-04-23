package com.it.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.it.entity.Permission;
import com.it.entity.RemindMessage;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author zhuminghang
 * @since 2020/4/17 14:21
 */
@Mapper
public interface RemindMessageMapper extends BaseMapper<RemindMessage> {
    List<RemindMessage> selectUnreadList(RemindMessage remindMessage);
}
