package com.it.entity.vo;

import com.it.entity.Log;
import lombok.Data;

import java.util.List;

/**
 * @author zhuminghang
 * @since 2020/4/22 18:08
 */
@Data
public class LogVO {
    private List<Log> logList;

    private Long totalNum;
}
