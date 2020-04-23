package com.it.controller;


import com.it.entity.*;
import com.it.entity.vo.VideoVO;
import com.it.service.*;
import com.it.util.ItdragonUtils;
import com.it.util.Result;
import com.it.util.ResultResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @version:
 * @Description: 前台页面相关内容
 * @author:
 * @date: 2019年8月16日 上午10:09:18
 */
@RequestMapping("/front")
@Controller
public class FrontController {
    private static final transient Logger log = LoggerFactory.getLogger(FrontController.class);


    @Autowired
    private WbeParameterService wbeParameterService;
    @Autowired
    private ItdragonUtils itdragonUtils;
    @Autowired
    private UserService userService;
    @Autowired
    private LogService logService;
    @Autowired
    private VideoService videoService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private CourseService courseService;

    /**
     * 前台首页跳转接口
     *
     * @param mv
     * @return
     */
    @RequestMapping("/index.do")
    public ModelAndView index(ModelAndView mv) {
        CommonMethods("index", mv);
        List<Video> latestVideoList1 = videoService.selectLatestList("");
        List<VideoVO> latestVideoList =new ArrayList();
        for (Video video : latestVideoList1) {
            VideoVO videoVO=new VideoVO();
            video.setUserId(userService.getUserByUserName(video.getUserName()).getId());
            BeanUtils.copyProperties(video,videoVO);
            videoVO.setCollectFlag(videoService.isCollect(userService.getUserByUserName(video.getUserName()).getId()));
            if (itdragonUtils.getSessionUser().getId().equals(userService.getUserByUserName(video.getUserName()).getId())){
                videoVO.setUserSelf(false);
            }else {
                videoVO.setUserSelf(true);
            }
            latestVideoList.add(videoVO);
        }
        mv.addObject("latestVideoList", latestVideoList);

        List<Video> popularVideoList1 = videoService.selectPopularList();
        List<VideoVO> popularVideoList =new ArrayList();
        for (Video video : popularVideoList1) {
            VideoVO videoVO=new VideoVO();
            video.setUserId(userService.getUserByUserName(video.getUserName()).getId());
            BeanUtils.copyProperties(video,videoVO);
            videoVO.setCollectFlag(videoService.isCollect(userService.getUserByUserName(video.getUserName()).getId()));
            if (itdragonUtils.getSessionUser().getId().equals(userService.getUserByUserName(video.getUserName()).getId())){
                videoVO.setUserSelf(false);
            }else {
                videoVO.setUserSelf(true);
            }
            popularVideoList.add(videoVO);
        }
        mv.addObject("popularVideoList", popularVideoList);
        mv.setViewName("/front/index");
        return mv;
    }


    /**
     * 视频页面跳转接口
     *
     * @param mv
     * @return
     */
    @RequestMapping("/video.do")
    public ModelAndView video(ModelAndView mv, String ptId) {
        CommonMethods("video", mv);
        List<Video> videoList = videoService.selectLatestList(ptId);
        mv.addObject("videoList", videoList);
        mv.setViewName("/front/video");
        return mv;
    }

    /**
     * 视频详情页面跳转接口
     *
     * @param mv
     * @return
     */
    @RequestMapping("/videoDetail.do")
    public ModelAndView videoDetail(ModelAndView mv, String id) {
        Video video = videoService.getOne(id);
        mv.addObject("video", video);
        CommonMethods("video", mv);
        List<Comment> commentList = commentService.getList(id);
        for (Comment comment : commentList) {
            if (comment.getUserId().equals(itdragonUtils.getSessionUser().getId())) {
                comment.setIsMe("1");
            }
            comment.setUser(userService.selectByPrimaryKey(comment.getUserId()));
        }
        video.setLikeNum(commentService.getLikeNumber(id, "赞"));
        video.setNoLikeNum(commentService.getLikeNumber(id, "踩"));
        mv.addObject("commentList", commentList);
        //判断是否关注当前作者
        mv.addObject("isCollect", videoService.isCollect(userService.getUserByUserName(video.getUserName()).getId()));
        mv.addObject("isCollection",videoService.isCollections(video.getId()));
        if (itdragonUtils.getSessionUser().getId().equals(userService.getUserByUserName(video.getUserName()).getId())){
            mv.addObject("isUserSelf",false);
        }else {
            mv.addObject("isUserSelf",true);
        }
        if ("图片".equals(video.getType())) {
            ArrayList<String> arrayList = new ArrayList<>();
            String[] split = video.getUrl().split(",");
            for (String s : split) {
                if (!"".equals(s)) {
                    arrayList.add(s);
                }
            }
            video.setImgList(arrayList);
        }
        mv.addObject("userId", userService.getUserByUserName(video.getUserName()).getId());
        mv.setViewName("/front/videoDetail");
        return mv;
    }

    /**
     * 添加评论
     *
     * @param
     * @return
     */
    @ResponseBody
    @PostMapping("/addComment.do")
    public ResultResponse addComment(Comment comment) {
        boolean insert = commentService.insert(comment);
        if (!insert) {
            return Result.resuleError("失败");
        }
        return Result.resuleSuccess();
    }

    /**
     * 删除评论
     *
     * @param
     * @return
     */
    @ResponseBody
    @PostMapping("/delComment.do")
    public ResultResponse delComment(String id) {
        boolean insert = commentService.delById(id);
        if (!insert) {
            return Result.resuleError("失败");
        }
        return Result.resuleSuccess();
    }

    /**
     * 点赞功能接口
     *
     * @param likeNumber
     * @return
     */
    @ResponseBody
    @PostMapping("/getALike.do")
    public ResultResponse getALike(LikeNumber likeNumber) {
        String result = commentService.giveALike(likeNumber);
        if ("default".equals(result)) {
            return Result.resuleError("系统故障!");
        } else if ("callOff".equals(result)) {
            return Result.resuleSuccess(null, "操作成功");
        }
        return Result.resuleSuccess(null, "操作成功");
    }

    /**
     * 关注功能接口
     *
     * @param followId
     * @return
     */
    @ResponseBody
    @PostMapping("/collect.do")
    public ResultResponse collect(String followId) {
        if (followId.equals(itdragonUtils.getSessionUser().getId())){
            return Result.resuleSuccess(null, "无法关注自己");
        }
        boolean result = videoService.isCollect(followId);
        if (result) {
            videoService.delCollect(followId);
            return Result.resuleSuccess(null, "取消关注成功");
        } else {
            videoService.insertCollect(followId);
            return Result.resuleSuccess(null, "关注成功");
        }
    }


    /**
     * 个人中心跳转接口
     *
     * @param mv
     * @return
     */
    @RequestMapping("/userCenter.do")
    public ModelAndView userCenter(ModelAndView mv) {
        CommonMethods("userCenter", mv);
        List<Collect> collectList = videoService.getCollectList();
        mv.addObject("collectList", collectList);
        mv.setViewName("/front/userCenter");
        return mv;
    }

    /**
     * 个人信息跳转接口
     *
     * @param mv
     * @return
     */
    @RequestMapping("/userInfo.do")
    public ModelAndView userInfo(ModelAndView mv) {
        CommonMethods("userCenter", mv);
        User user = userService.selectByPrimaryKey(itdragonUtils.getSessionUser().getId());
        mv.addObject("user", user);
        mv.setViewName("/front/userInfo");
        return mv;
    }

    /**
     * 个人信息跳转接口
     *
     * @param mv
     * @return
     */
    @RequestMapping("/upToVideo.do")
    public ModelAndView upToVideo(ModelAndView mv) {
        CommonMethods("userCenter", mv);
        List<Course> courses = courseService.selectList();
        mv.addObject("courseList", courses);
        mv.setViewName("/front/upToVideo");
        return mv;
    }

    /**
     * 我的作品跳转接口
     *
     * @param mv
     * @return
     */
    @RequestMapping("/myVideo.do")
    public ModelAndView myVideo(ModelAndView mv) {
        CommonMethods("userCenter", mv);
        List<Video> videoList = videoService.selectListByUser(itdragonUtils.getSessionUser().getUserName());
        mv.addObject("videoList", videoList);
        mv.setViewName("/front/myVideo");
        return mv;
    }

    /**
     * 我的收藏作品跳转接口
     *
     * @param mv
     * @return
     */
    @RequestMapping("/myCollectionsVideo.do")
    public ModelAndView myCollectionsVideo(ModelAndView mv) {
        CommonMethods("userCenter", mv);
        List<Video> videoList = videoService.selectCollectionsListByUser(itdragonUtils.getSessionUser().getId());
        mv.addObject("videoList", videoList);
        mv.setViewName("/front/myCollectionsVideo");
        return mv;
    }

    /**
     * 用户作品跳转接口
     *
     * @param mv
     * @return
     */
    @RequestMapping("/userDetail.do")
    public ModelAndView userDetail(ModelAndView mv, String userName) {
        CommonMethods("userCenter", mv);
        List<Video> videoList = videoService.selectListByUser(userName);
        mv.addObject("videoList", videoList);
        mv.setViewName("/front/userDetail");
        return mv;
    }

    /**
     * 修改密码跳转接口
     *
     * @param mv
     * @return
     */
    @RequestMapping("/updataPass.do")
    public ModelAndView updataPass(ModelAndView mv) {
        CommonMethods("userCenter", mv);
        User user = userService.selectByPrimaryKey(itdragonUtils.getSessionUser().getId());
        mv.addObject("user", user);
        mv.setViewName("/front/updataPass");
        return mv;
    }

    /**
     * 前端页面有一些公用需求在这里抽取出来
     *
     * @param which 是选中哪一个页面顶部标签参数
     * @param mv
     */
    public void CommonMethods(String which, ModelAndView mv) {
        //判断用户是否登录
        boolean gogin = itdragonUtils.isGogin();
        boolean admin = false;
        if (gogin) {
            //如果登录将用户信息放入前台
            mv.addObject("userInfo", userService.selectByPrimaryKey(itdragonUtils.getSessionUser().getId()));
            //平且判断是否是管理员
            admin = userService.isAdmin(itdragonUtils.getSessionUser().getId());

        }
        List<Course> courseList = courseService.selectList();
        mv.addObject("courseList", courseList);
        mv.addObject("go_in", gogin);
        mv.addObject("admin", admin);
        //网站参数
        WbeParameter wbeParameter = wbeParameterService.getWbeParameter();
        if (wbeParameter.getAboutMe().length() > 135) {
            wbeParameter.setAboutMe(wbeParameter.getAboutMe().substring(0, 135) + "...");
        }
        mv.addObject("wbeParameter", wbeParameter);
        //页面顶部标签高亮判断
        switch (which) {
            case "index":
                mv.addObject("index", "layui-nav-item layui-this");
                mv.addObject("news", "layui-nav-item");
                break;
            case "news":
                mv.addObject("index", "layui-nav-item");
                mv.addObject("news", "layui-nav-item layui-this");
                break;
            case "no":
                mv.addObject("index", "layui-nav-item");
                mv.addObject("news", "layui-nav-item");
                break;
            default:
        }
    }

    /**
     * 收藏功能接口
     *
     * @param videoId
     * @return
     */
    @ResponseBody
    @PostMapping("/collections.do")
    public ResultResponse collections(String videoId) throws IOException {
        boolean result = videoService.isCollections(videoId);
        if (result) {
            videoService.delCollections(videoId);
            logService.insert("取消收藏");
            return Result.resuleSuccess(null, "取消收藏成功");
        } else {
            videoService.insertCollections(videoId);
            logService.insert("收藏作品");
            return Result.resuleSuccess(null, "收藏成功");
        }
    }
}
