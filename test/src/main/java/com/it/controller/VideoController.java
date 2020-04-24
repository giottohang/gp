package com.it.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.it.entity.Course;
import com.it.entity.Log;
import com.it.entity.RemindMessage;
import com.it.entity.Video;
import com.it.entity.vo.DataAnalysis;
import com.it.entity.vo.LogVO;
import com.it.entity.vo.RemindMessageVO;
import com.it.service.CourseService;
import com.it.service.ElasticsearchService;
import com.it.service.LogService;
import com.it.service.VideoService;
import com.it.util.Result;
import com.it.util.ResultResponse;
import com.it.util.TableResultResponse;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 〈视频接口〉<br>
 *
 * @author
 * @create2020/1/16 16:00
 * @since 1.0.0
 */
@Controller
@RequestMapping("/video")
public class VideoController {
    @Autowired
    private VideoService videoService;
    @Autowired
    private CourseService courseService;
    @Autowired
    private ElasticsearchService elasticsearchService;
    @Autowired
    private LogService logService;

    /**
     * 管理界面跳转
     *
     * @param mv
     * @return
     */
    @RequestMapping("/index.do")
    public ModelAndView enIndex(ModelAndView mv) {
        mv.setViewName("video/index");
        return mv;
    }

    /**
     * 列表加载接口
     *
     * @param video
     * @param page
     * @param limit
     * @return
     */
    @ResponseBody
    @GetMapping("video.do")
    public TableResultResponse classifyTable(Video video, int page, int limit) {
        List<Map<String, Object>> infoList = new ArrayList<>();
        Page<Video> pageInfo = videoService.selectPage(video, page, limit);
        for (Video record : pageInfo.getRecords()) {
            Map<String, Object> resultMap = new HashMap<>(16);
            resultMap.put("id", record.getId());
            resultMap.put("title", record.getTitle());
            resultMap.put("info", record.getInfo());
            resultMap.put("state", record.getState());
            resultMap.put("url", record.getUrl());
            resultMap.put("img", "<a href=\"" + record.getImg() + "\">查看图片</a>");
            resultMap.put("userName", record.getUserName());
            resultMap.put("time", record.getTime() == null ? "" : record.getTime().substring(0, 11));
            infoList.add(resultMap);
        }
        return Result.tableResule(pageInfo.getTotal(), infoList);
    }


    /**
     * 删除
     *
     * @param id
     * @return
     */
    @ResponseBody
    @DeleteMapping("/video.do")
    public ResultResponse delClassify(String id) throws IOException {
        boolean result = videoService.delById(id);
        if (!result) {
            return Result.resuleError("删除失败");
        }
        logService.insert("删除作品");
        return Result.resuleSuccess();
    }


    /**
     * 新增跳转接口
     *
     * @param mv
     * @return
     */
    @RequestMapping("/addPage.do")
    public ModelAndView addPage(ModelAndView mv) {
        List<Course> courses = courseService.selectList();
        mv.addObject("courseList", courses);
        mv.setViewName("video/addPage");
        return mv;
    }

    /**
     * 编辑跳转接口
     *
     * @param mv
     * @return
     */
    @RequestMapping("/editPage.do")
    public ModelAndView editPage(ModelAndView mv, String id) {
        Video video = videoService.getOne(id);
        mv.addObject("video", video);
        mv.setViewName("video/editPage");
        return mv;
    }


    /**
     * 新增
     *
     * @param video
     * @return
     */
    @ResponseBody
    @PostMapping("/video.do")
    public ResultResponse insert(Video video) throws IOException {
        boolean result = videoService.insert(video);
        if (!result) {
            return Result.resuleError("新增失败");
        }
        logService.insert("上传作品");
        return Result.resuleSuccess();
    }

    /**
     * 编辑
     *
     * @param video
     * @return
     */
    @ResponseBody
    @PutMapping("/video.do")
    public ResultResponse editQuestion(Video video) {
        boolean result = videoService.updataById(video);
        if (!result) {
            return Result.resuleError("编辑失败");
        }
        return Result.resuleSuccess();
    }

    @ResponseBody
    @GetMapping("/remindMessage.do")
    public ResultResponse getRemindMessage() {
        try {
            List<RemindMessageVO> list=videoService.selectUnreadList();
            return Result.resuleSuccess(list);
        }catch (Exception e){
            return Result.resuleError("获取更新提醒失败");
        }

    }

    @ResponseBody
    @GetMapping("/selectDataAnalysis.do")
    public ResultResponse selectDataAnalysis() {
        try {
            List<DataAnalysis> list=videoService.selectDataAnalysis();
            return Result.resuleSuccess(list);
        }catch (Exception e){
            return Result.resuleError("获取分析数据失败");
        }
    }
    //业务分析跳转接口
    @ResponseBody
    @GetMapping("/dataAnalysis.do")
    public ModelAndView DataAnalysis(ModelAndView mv) {
        mv.setViewName("video/dataAnalysis");
        return mv;
    }
    //操作日志跳转接口
    @ResponseBody
    @GetMapping("/oprateLog.do")
    public ModelAndView oprateLog(ModelAndView mv) {
        mv.setViewName("video/oprateLog");
        return mv;
    }

    @ResponseBody
    @GetMapping("/selectOprateLog.do")
    public TableResultResponse selectOprateLog(Log log, int page, int limit) {
        try {
            String datetime=log.getTime();
            String userName=log.getUserName();
            LogVO logVO = elasticsearchService.selectLogByPage(datetime,userName,page, limit);
            List<Map<String, Object>> infoList = new ArrayList<>();
            for (Log log1 : logVO.getLogList()) {
                Map<String, Object> resultMap = new HashMap<>(16);
                resultMap.put("id", log1.getId());
                resultMap.put("userName", log1.getUserName());
                resultMap.put("operation", log1.getOperation());
                resultMap.put("time", log1.getTime() == null ? "" : log1.getTime());
                infoList.add(resultMap);
            }
            return Result.tableResule(logVO.getTotalNum(), infoList);
        }catch (Exception e){
            return null;
        }
    }

    @ResponseBody
    @GetMapping("/oprateLogExport.do")
    public ResultResponse oprateLogExport(@RequestParam(name = "date") String date,@RequestParam(name = "userName") String userName, HttpServletRequest request, HttpServletResponse response) {
        try {
            if (date==null||"".equals(date)){
                return Result.resuleError("日期为空");
            }
           elasticsearchService.oprateLogExport(date,userName,request,response);
            return Result.resuleSuccess("导出操作日志数据成功");
        }catch (Exception e){
            return Result.resuleError("导出操作日志数据失败");
        }
    }
}