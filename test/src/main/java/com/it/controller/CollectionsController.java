package com.it.controller;

import com.it.service.LogService;
import com.it.service.VideoService;
import com.it.util.Result;
import com.it.util.ResultResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

/**
 * @author zhuminghang
 * @since 2020/4/15 11:52
 */
@Controller
@RequestMapping("/Collections")
public class CollectionsController {
    @Autowired
    private VideoService videoService;
    @Autowired
    private LogService logService;

    /**
     * 删除
     *
     * @param videoId
     * @return
     */
    @ResponseBody
    @DeleteMapping("/delCollections.do")
    public ResultResponse delClassify(String videoId) throws IOException {
        boolean result = videoService.delCollections(videoId);
        if (!result) {
            return Result.resuleError("删除失败");
        }
        logService.insert("取消收藏");
        return Result.resuleSuccess();
    }
}
