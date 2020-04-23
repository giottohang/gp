package com.it.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.it.entity.PackUpInfo;
import com.it.entity.WbeParameter;

public interface WbeParameterService {


    /**
     * 修改
     *
     * @param wbeParameter
     * @return
     */
    boolean editById(WbeParameter wbeParameter);

    /**
     * 插入数据量备份信息
     *
     * @param packUpInfo
     * @return
     */
    boolean insert(PackUpInfo packUpInfo);

    /**
     * 数据量备份信息分页查询
     *
     * @param Log
     * @param page
     * @param limit
     * @return
     */
    Page<PackUpInfo> selectPage(PackUpInfo packUpInfo, int page, int limit);

    /**
     * 得到网站设置数据
     *
     * @return
     */
    WbeParameter getWbeParameter();


}
