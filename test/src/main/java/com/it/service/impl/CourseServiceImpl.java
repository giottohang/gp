package com.it.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.it.entity.Book;
import com.it.entity.Course;
import com.it.mapper.BookMapper;
import com.it.mapper.CourseMapper;
import com.it.service.CourseService;
import com.it.util.ItdragonUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 〈商品分类实现接口〉<br>
 *
 * @author
 * @create2020/2/15 17:53
 * @since 1.0.0
 */
@Service
public class CourseServiceImpl implements CourseService {
    @Resource
    private CourseMapper courseMapper;
    @Resource
    private BookMapper bookMapper;
    @Resource
    private ItdragonUtils itdragonUtils;

    @Override
    public Page<Course> selectPage(Course course, int page, int limit) {
        EntityWrapper<Course> searchInfo = new EntityWrapper<>();
        Page<Course> pageInfo = new Page<>(page, limit);
        List<Course> resultList = courseMapper.selectPage(pageInfo, searchInfo);
        if (!resultList.isEmpty()) {
            pageInfo.setRecords(resultList);
        }
        return pageInfo;
    }


    @Override
    public boolean insert(Course course) {
        Integer insert = courseMapper.insert(course);
        if (insert > 0) {
            return true;
        }
        return false;
    }


    @Override
    public boolean delById(String id) {
        //删除分类前先要删除该分类下所有的子分类
        EntityWrapper<Book> wrapper = new EntityWrapper<>();
        wrapper.eq("ptId", id);
        bookMapper.delete(wrapper);
        Integer delete = courseMapper.deleteById(id);
        if (delete > 0) {
            return true;
        }
        return false;
    }

    @Override
    public List<Course> selectList() {
        List<Course> classifyList = courseMapper.selectList(null);
        if (classifyList.isEmpty()) {
            return new ArrayList<>();
        }
        return classifyList;
    }

    @Override
    public boolean insert(Book book) {
        Integer insert = bookMapper.insert(book);
        if (insert > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean deBookById(String id) {
        Integer delete = bookMapper.deleteById(id);
        if (delete > 0) {
            return true;
        }
        return false;
    }

    @Override
    public List<Book> selectBookList(String ptId) {
        EntityWrapper<Book> wrapper = new EntityWrapper<>();
        wrapper.eq("ptId", ptId);
        List<Book> chdClassifyList = bookMapper.selectList(wrapper);
        if (chdClassifyList.isEmpty()) {
            return new ArrayList<>();
        }
        return chdClassifyList;
    }

    @Override
    public List<Book> selectBookList() {
        return bookMapper.selectList(null);
    }

    @Override
    public Course getCourse(String id) {
        return courseMapper.selectById(id);
    }

    @Override
    public Book getBook(String id) {
        return bookMapper.selectById(id);
    }
}