package com.ymall.controller.backend;

import com.ymall.common.Const;
import com.ymall.common.ResponseCode;
import com.ymall.common.ServerResponse;
import com.ymall.pojo.User;
import com.ymall.service.ICategoryService;
import com.ymall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/manage/category")
public class CategoryManagerController {

    @Autowired
    private IUserService userService;

    @Autowired
    private ICategoryService categoryService;


    @RequestMapping("add_category.do")
    @ResponseBody
    public ServerResponse addCategory(HttpSession session, String categoryName,
                                      @RequestParam(value = "parentId", defaultValue = "0") int parentId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录");
        }
        if (userService.checkAdminRole(user).isSuccess()) {
            //成功就说明是管理员的身份
            //直接返回service里的逻辑
            return categoryService.addCategory(categoryName, parentId);
        }

        return ServerResponse.createByErrorMessage("没有权限，请使用管理员账号登录");
    }

    @RequestMapping("update_category.do")
    @ResponseBody
    public ServerResponse setCategoryName(HttpSession session, Integer categoryId, String categoryName) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录");
        }
        if (userService.checkAdminRole(user).isSuccess()) {
            //成功就说明是管理员的身份
            //直接返回service里的逻辑
            return categoryService.updateCategoryName(categoryId, categoryName);

        }
        return ServerResponse.createByErrorMessage("没有权限，请使用管理员账号登录");
    }

    @RequestMapping("get_category.do")
    @ResponseBody
    public ServerResponse getChildrenParallelCategory(HttpSession session,
                                                      @RequestParam(value = "categoryId", defaultValue = "0") Integer categoryId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录");
        }
        if (userService.checkAdminRole(user).isSuccess()) {
            //成功就说明是管理员的身份
            //查询子节点的category信息
            return categoryService.getChildrenParallelCategory(categoryId);

        }
        return ServerResponse.createByErrorMessage("没有权限，请使用管理员账号登录");
    }

    @RequestMapping("get_deep_category.do")
    @ResponseBody
    public ServerResponse getCategoryAndDeepCategory(HttpSession session,
                                                      @RequestParam(value = "categoryId", defaultValue = "0") Integer categoryId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录");
        }
        if (userService.checkAdminRole(user).isSuccess()) {
            //成功就说明是管理员的身份
            //查询子节点的category信息
            return categoryService.selectCategoryAndChildrenById(categoryId);

        }
        return ServerResponse.createByErrorMessage("没有权限，请使用管理员账号登录");
    }
}
