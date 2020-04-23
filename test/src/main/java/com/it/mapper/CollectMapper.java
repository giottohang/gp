package com.it.mapper;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.it.entity.Collect;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
/**
 * 数据访问层
 */
@Repository
public interface CollectMapper extends BaseMapper<Collect> {

}
