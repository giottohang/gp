package com.it.controller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;
import com.it.entity.*;
import com.it.service.LogService;
import com.it.service.PermissionService;
import com.it.service.UserService;
import com.it.service.WbeParameterService;
import com.it.util.ItdragonUtils;
import com.it.util.Result;
import com.it.util.ResultResponse;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * @version:
 * @Description: 内容
 * @author: zhao shi jie
 * @date:2020年8月16日 上午10:09:18
 */
@Controller
public class IndexController {
    private static final transient Logger log = LoggerFactory.getLogger(IndexController.class);
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private ItdragonUtils itdragonUtils;
    @Autowired
    private UserService userService;
    @Autowired
    private WbeParameterService wbeParameterService;
    @Autowired
    private LogService logService;

    @GetMapping("/indexWbe")
    public ModelAndView index(ModelAndView mv, Model model) {
        User user = userService.selectByPrimaryKey(itdragonUtils.getSessionUser().getId());
        List<Menu> menuList = permissionService.getMenuInfoByUserId(user.getId());
        JSONArray jsonArray = new JSONArray();
        for (Menu menu : menuList) {
            JSONObject menuMap = new JSONObject();
            menuMap.put("F_ModuleId", menu.getMenuId());
            menuMap.put("F_ParentId", menu.getPerentMenuId());
            menuMap.put("F_FullName", menu.getMenuName());
            menuMap.put("F_UrlAddress", menu.getMenuURL());
            jsonArray.add(menuMap);
        }
        model.addAttribute("menuInfo", jsonArray);
        User userByUserName = userService.findUserByUserName(user.getUserName());
        List<Role> roleList = userByUserName.getRoleList();
        if (roleList.isEmpty()) {
            model.addAttribute("role", "暂无身份");
        } else {
            model.addAttribute("role", roleList.get(0).getRole());
        }
        WbeParameter wbeParameter = wbeParameterService.getWbeParameter();
        model.addAttribute("wbeParameter", wbeParameter);
        model.addAttribute("user", user);
        mv.setViewName("content/index");
        return mv;
    }

    /**
     * 登录页面
     *
     * @param mv
     * @return
     */
    @RequestMapping("/loginWbe")
    public ModelAndView login(ModelAndView mv) {
        WbeParameter wbeParameter = wbeParameterService.getWbeParameter();
        mv.addObject("wbeParameter", wbeParameter);
        mv.setViewName("/login");
        return mv;
    }

    /**
     * Shiro登录跳转地址,重定向到登录页面
     *
     * @param mv
     * @return
     */
    @RequestMapping("/loginShiro")
    public String loginShiro(ModelAndView mv) {
        Subject currentUser = SecurityUtils.getSubject();
        if (currentUser.isAuthenticated()) {
            return "forward:/indexWbe";
        } else {
            return "forward:/loginWbe";
        }
    }

    /**
     * Shiro登录成功后index跳转地址,重定向到indexWbe页面
     *
     * @param mv
     * @return
     */
    @RequestMapping("/indexShiro")
    public String indexShiro(ModelAndView mv) {
        if (userService.isUser(itdragonUtils.getSessionUser().getId())) {
            return "forward:/front/index.do";
        } else {
            return "forward:/indexWbe";
        }

    }

    /**
     * 修改密码页面跳转
     *
     * @param mv
     * @return
     */
    @GetMapping("/changePwd")
    public ModelAndView changePwd(ModelAndView mv) {
        User user = (User) itdragonUtils.getShiroSession().getAttribute("loginUserInfo");
        mv.addObject("user", user);
        mv.setViewName("content/changePwd");
        return mv;
    }

    /**
     * 用户修改密码
     *
     * @param newPas
     * @param oldPas
     * @param userName
     * @return
     */
    @ResponseBody
    @PostMapping("/updatePas")
    public ResultResponse updatePassword(String newPas, String oldPas, String userName) {
        User user = (User) itdragonUtils.getShiroSession().getAttribute("loginUserInfo");
        if (oldPas.equals(user.getPlainPassword())) {
            boolean result = userService.changePass(newPas, userName);
            if (result) {
                user.setPlainPassword(newPas);
                return Result.resuleSuccess();
            } else {
                return Result.resuleError("修改失败");
            }

        }
        return Result.resuleError("原密码错误,请重新输入");


    }

    /**
     * 用户中心
     *
     * @param mv
     * @return
     */
    @GetMapping("/userInfo")
    public ModelAndView userInfo(ModelAndView mv) {
        User user = userService.selectByPrimaryKey(itdragonUtils.getSessionUser().getId());
        if (!ItdragonUtils.stringIsNotBlack(user.getImgUrl())) {
            user.setImgUrl("/resource/image/default.png");
        }
        mv.addObject("user", user);
        mv.setViewName("content/userInfo");
        return mv;
    }

    /**
     * 首页
     *
     * @param mv
     * @return
     */
    @GetMapping("/main")
    public ModelAndView main(ModelAndView mv) {
        WbeParameter wbeParameter = wbeParameterService.getWbeParameter();
        mv.addObject("wbeParameter", wbeParameter);
        Log log = new Log();
        log.setOperation("登录");
        Page<Log> logPage = logService.selectPage(log, 1, 10);
        mv.addObject("logList", logPage.getRecords());
        User user = userService.selectByPrimaryKey(itdragonUtils.getSessionUser().getId());
        User userByUserName = userService.findUserByUserName(user.getUserName());
        List<Role> roleList = userByUserName.getRoleList();
        if (roleList.isEmpty()) {
            mv.addObject("role", "暂无身份");
        } else {
            mv.addObject("role", roleList.get(0).getRole());
        }
        mv.addObject("user", user);
        mv.setViewName("content/main");
        return mv;
    }

    /**
     * 404页面
     *
     * @param mv
     * @return
     */
    @GetMapping("/404")
    public ModelAndView silingsi(ModelAndView mv) {
        mv.setViewName("content/404");
        return mv;
    }


    /**
     * 注册页面跳转
     */
    @RequestMapping("/reg")
    public ModelAndView reg(ModelAndView mv) {
        WbeParameter wbeParameter = wbeParameterService.getWbeParameter();
        mv.addObject("wbeParameter", wbeParameter);
        mv.setViewName("reg");
        return mv;
    }

    /**
     * 忘记密码页面跳转
     */
    @RequestMapping("/forget")
    public ModelAndView forget(ModelAndView mv) {
        WbeParameter wbeParameter = wbeParameterService.getWbeParameter();
        mv.addObject("wbeParameter", wbeParameter);
        mv.setViewName("forget");
        return mv;
    }

    /**
     * 忘记密码页面跳转
     */
    @RequestMapping("/getQuestion")
    public ModelAndView getQuestion(ModelAndView mv, String userName) {
        WbeParameter wbeParameter = wbeParameterService.getWbeParameter();
        User user = userService.getUserByUserName(userName);
        mv.addObject("wbeParameter", wbeParameter);
        mv.addObject("user", user);
        mv.setViewName("getQuestion");
        return mv;
    }


    /**
     * 用户修改密码
     *
     * @param answer
     * @param userName
     * @return
     */
    @ResponseBody
    @PostMapping("/answer")
    public ResultResponse answer(String userName, String answer) {
        User user = userService.getUserByUserName(userName);
        if (answer.equals(user.getAnswer())) {
            return Result.resuleSuccess(null, user.getPassword());
        }
        return Result.resuleError("答案输入错误");


    }

    /**
     * 登录页面跳转
     */
    @RequestMapping("/login")
    public ModelAndView login1(ModelAndView mv) {
        WbeParameter wbeParameter = wbeParameterService.getWbeParameter();
        mv.addObject("wbeParameter", wbeParameter);
        mv.setViewName("login");
        return mv;
    }


}
