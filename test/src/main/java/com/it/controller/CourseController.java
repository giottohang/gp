package com.it.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.it.entity.Book;
import com.it.entity.Course;
import com.it.service.CourseService;
import com.it.util.Result;
import com.it.util.ResultResponse;
import com.it.util.TableResultResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 〈分类分类实现接口〉<br>
 *
 * @author
 * @create2020/1/16 16:00
 * @since 1.0.0
 */
@Controller
@RequestMapping("/course")
public class CourseController {
    @Autowired
    private CourseService courseService;

    /**
     * 分类分类管理界面跳转
     *
     * @param mv
     * @return
     */
    @RequestMapping("/index.do")
    public ModelAndView enIndex(ModelAndView mv) {
        mv.setViewName("course/index");
        return mv;
    }

    /**
     * 章节管理界面跳转
     *
     * @param mv
     * @return
     */
    @RequestMapping("/sectionIndex.do")
    public ModelAndView sectionIndex(ModelAndView mv) {
        mv.setViewName("course/sectionIndex");
        return mv;
    }

    /**
     * 分类分类列表加载接口
     *
     * @param course
     * @param page
     * @param limit
     * @return
     */
    @ResponseBody
    @GetMapping("course.do")
    public TableResultResponse classifyTable(Course course, int page, int limit) {
        List<Map<String, Object>> infoList = new ArrayList<>();
        Page<Course> pageInfo = courseService.selectPage(course, page, limit);
        for (Course record : pageInfo.getRecords()) {
            Map<String, Object> resultMap = new HashMap<>(16);
            resultMap.put("id", record.getId());
            String chdName = "";
            //得到所有子分类
            List<Book> bookList = courseService.selectBookList(record.getId());
            for (Book book : bookList) {
                //拿到子分类名称循环添加到chdName,用空格隔开
                chdName += "&nbsp;&nbsp;" + book.getName();
            }
            resultMap.put("chdName", chdName);
            resultMap.put("color", record.getColor());
            resultMap.put("english", record.getEnglish());
            resultMap.put("name", record.getName());
            infoList.add(resultMap);
        }
        return Result.tableResule(pageInfo.getTotal(), infoList);
    }


    /**
     * 删除分类
     *
     * @param id
     * @return
     */
    @ResponseBody
    @DeleteMapping("/book.do")
    public ResultResponse delClassify(String id) {
        boolean result = courseService.delById(id);
        if (!result) {
            return Result.resuleError("删除失败");
        }
        return Result.resuleSuccess();
    }


    /**
     * 删除课程
     *
     * @param id
     * @return
     */
    @ResponseBody
    @DeleteMapping("/course.do")
    public ResultResponse delCourse(String id) {
        boolean result = courseService.delById(id);
        if (!result) {
            return Result.resuleError("删除失败");
        }
        return Result.resuleSuccess();
    }

    /**
     * 新增分类界面跳转接口
     *
     * @param mv
     * @return
     */
    @RequestMapping("/addCourse.do")
    public ModelAndView addCourse(ModelAndView mv) {
        mv.setViewName("course/addCourse");
        return mv;
    }

    /**
     * 新增章节界面跳转接口
     *
     * @param mv
     * @return
     */
    @RequestMapping("/addSection.do")
    public ModelAndView addSection(ModelAndView mv) {
        List<Course> courseList = courseService.selectList();
        mv.addObject("courseList", courseList);
        mv.setViewName("course/addSection");
        return mv;
    }

    /**
     * 新增分类
     *
     * @param course
     * @return
     */
    @ResponseBody
    @PostMapping("/course.do")
    public ResultResponse insertCourse(Course course) {
        boolean result = courseService.insert(course);
        if (!result) {
            return Result.resuleError("新增失败");
        }
        return Result.resuleSuccess();
    }


    /**
     * 新增子分类界面跳转接口
     *
     * @param mv
     * @return
     */
    @RequestMapping("/addBook.do")
    public ModelAndView addBook(ModelAndView mv, String ptId) {
        mv.addObject("ptId", ptId);
        mv.setViewName("course/addBook");
        return mv;
    }

    /**
     * 新增子分类
     *
     * @param book
     * @return
     */
    @ResponseBody
    @PostMapping("/book.do")
    public ResultResponse insertBook(Book book) {
        boolean result = courseService.insert(book);
        if (!result) {
            return Result.resuleError("新增失败");
        }
        return Result.resuleSuccess();
    }

    /**
     * 查看子分类跳转接口
     *
     * @param mv
     * @return
     */
    @RequestMapping("/lookBook.do")
    public ModelAndView lookBook(ModelAndView mv, String ptId) {
        List<Book> bookList = courseService.selectBookList(ptId);
        mv.addObject("bookList", bookList);
        mv.setViewName("course/lookBook");
        return mv;
    }

    /**
     * 删除子分类分类
     *
     * @param id
     * @return
     */
    @ResponseBody
    @DeleteMapping("/delBook.do")
    public ResultResponse deBookById(String id) {
        boolean result = courseService.deBookById(id);
        if (!result) {
            return Result.resuleError("删除失败");
        }
        return Result.resuleSuccess();
    }

    /**
     * 得到列表
     *
     * @param ptId
     * @return
     */
    @ResponseBody
    @PostMapping("/getBookList.do")
    public List<Book> getBookList(String ptId) {
        List<Book> chdClassifieList = courseService.selectBookList(ptId);
        return chdClassifieList;
    }
}