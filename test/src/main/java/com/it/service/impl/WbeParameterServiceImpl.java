package com.it.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.it.entity.PackUpInfo;
import com.it.entity.WbeParameter;
import com.it.mapper.PackUpInfoMapper;
import com.it.mapper.WbeParameterMapper;
import com.it.service.WbeParameterService;
import com.it.util.DateUtil;
import com.it.util.ItdragonUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 网站参数设置接口
 */
@Service
public class WbeParameterServiceImpl implements WbeParameterService {
    @Resource
    private WbeParameterMapper wbeParameterMapper;
    @Resource
    private PackUpInfoMapper packUpInfoMapper;
    @Resource
    private ItdragonUtils itdragonUtils;

    @Override
    public boolean editById(WbeParameter wbeParameter) {
        Integer integer = wbeParameterMapper.updateById(wbeParameter);
        if (integer > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean insert(PackUpInfo packUpInfo) {
        packUpInfo.setUserName(itdragonUtils.getSessionUser().getUserName());
        packUpInfo.setTime(DateUtil.getNowDateSS());
        Integer integer = packUpInfoMapper.insert(packUpInfo);
        if (integer > 0) {
            return true;
        }
        return false;
    }

    @Override
    public Page<PackUpInfo> selectPage(PackUpInfo packUpInfo, int page, int limit) {
        EntityWrapper<PackUpInfo> searchInfo = new EntityWrapper<>();
        Page<PackUpInfo> pageInfo = new Page<>(page, limit);
        searchInfo.orderBy("time desc");
        List<PackUpInfo> resultList = packUpInfoMapper.selectPage(pageInfo, searchInfo);
        if (!resultList.isEmpty()) {
            pageInfo.setRecords(resultList);
        }
        return pageInfo;
    }

    @Override
    public WbeParameter getWbeParameter() {
        List<WbeParameter> wbeParameterList = wbeParameterMapper.selectList(null);
        if (wbeParameterList.isEmpty()) {
            return new WbeParameter();
        }
        return wbeParameterList.get(0);
    }
}
