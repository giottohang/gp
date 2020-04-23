package com.it.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.it.entity.Book;
import com.it.entity.Course;

import java.util.List;

public interface CourseService {
    /**
     * 分页查询
     *
     * @param course
     * @param page
     * @param limit
     * @return
     */
    Page<Course> selectPage(Course course, int page, int limit);


    /**
     * 新增
     *
     * @param course
     * @return
     */
    boolean insert(Course course);


    /**
     * 删除
     */
    boolean delById(String id);


    /**
     * 得到分类分类集合
     *
     * @return
     */
    List<Course> selectList();


    /**
     * 新增子分类
     *
     * @param book
     * @return
     */
    boolean insert(Book book);

    /**
     * 删除子分类
     */
    boolean deBookById(String id);

    /**
     * 得到子分类集合
     *
     * @return
     */
    List<Book> selectBookList(String ptId);

    List<Book> selectBookList();

    Course getCourse(String id);

    Book getBook(String id);
}
