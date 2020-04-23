package com.it.mapper;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.it.entity.Book;
import org.apache.ibatis.annotations.Mapper;

@Mapper
/**
 * 数据访问层
 */
public interface BookMapper extends BaseMapper<Book> {

}
