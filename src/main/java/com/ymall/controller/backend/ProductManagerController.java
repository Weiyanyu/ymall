package com.ymall.controller.backend;

import com.google.common.collect.Maps;
import com.ymall.common.Const;
import com.ymall.common.ResponseCode;
import com.ymall.common.ServerResponse;
import com.ymall.pojo.Product;
import com.ymall.pojo.User;
import com.ymall.service.IFileService;
import com.ymall.service.IProduceService;
import com.ymall.service.IUserService;
import com.ymall.util.PropertiesUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

@Controller
@RequestMapping("/manage/product")
public class ProductManagerController {

    @Autowired
    private IUserService userService;
    @Autowired
    private IProduceService produceService;

    @Autowired
    private IFileService fileService;


    @RequestMapping("save.do")
    @ResponseBody
    public ServerResponse saveProduct(HttpSession session, Product product) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录管理员账户");
        } else if (userService.checkAdminRole(user).isSuccess()) {
            //业务逻辑
            return produceService.saveOrUpdateProduce(product);
        }
        return ServerResponse.createByErrorMessage("无权限操作");
    }

    @RequestMapping("set_sale_status.do")
    @ResponseBody
    public ServerResponse setSaleStatus(HttpSession session, Integer productId, Integer status) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录管理员账户");
        } else if (userService.checkAdminRole(user).isSuccess()) {
            //业务逻辑
            return produceService.setSaleStatus(productId, status);
        }
        return ServerResponse.createByErrorMessage("无权限操作");
    }

    @RequestMapping("detail.do")
    @ResponseBody
    public ServerResponse getDetail(HttpSession session, Integer productId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录管理员账户");
        } else if (userService.checkAdminRole(user).isSuccess()) {
            return produceService.manageProductDetail(productId);
        }
        return ServerResponse.createByErrorMessage("无权限操作");
    }

    @RequestMapping("list.do")
    @ResponseBody
    public ServerResponse getList(HttpSession session,
                                  @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                  @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录管理员账户");
        } else if (userService.checkAdminRole(user).isSuccess()) {
            return produceService.getList(pageNum, pageSize);
        }
        return ServerResponse.createByErrorMessage("无权限操作");
    }

    @RequestMapping("search.do")
    @ResponseBody
    public ServerResponse productSearch(HttpSession session,
                                  String productName, Integer productId,
                                  @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                  @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录管理员账户");
        } else if (userService.checkAdminRole(user).isSuccess()) {
            return produceService.searchProduct(productName, productId, pageNum, pageSize);
        }
        return ServerResponse.createByErrorMessage("无权限操作");
    }

    @RequestMapping("upload.do")
    @ResponseBody
    public ServerResponse upload(HttpSession session,@RequestParam(value = "upload_file", required = false) MultipartFile file, HttpServletRequest request) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录管理员账户");
        } else if (userService.checkAdminRole(user).isSuccess()) {
            String path = request.getSession().getServletContext().getRealPath("upload");
            String targetFileName = fileService.upload(file, path);
            String url = PropertiesUtil.getStringProperty("ftp.server.http.prefix") + targetFileName;

            Map<String, String> fileMap = Maps.newHashMap();
            fileMap.put("uri",targetFileName);
            fileMap.put("url", url);
            return ServerResponse.createBySuccess(fileMap);
        }
        return ServerResponse.createByErrorMessage("无权限操作");
    }

    @RequestMapping("richtext_img_upload.do")
    @ResponseBody
    public Map richTextImageUpload(HttpSession session, @RequestParam(value = "upload_file", required = false) MultipartFile file, HttpServletRequest request, HttpServletResponse response) {
        Map map = Maps.newHashMap();
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            map.put("success", false);
            map.put("msg", "用户未登录，请登录（管理员）");
        } else if (userService.checkAdminRole(user).isSuccess()) {
            String path = request.getSession().getServletContext().getRealPath("upload");
            String targetFileName = fileService.upload(file, path);
            if (StringUtils.isBlank(targetFileName)) {
                map.put("success", false);
                map.put("msg", "上传失败");
                return map;
            }
            String url = PropertiesUtil.getStringProperty("ftp.server.http.prefix") + targetFileName;

            map.put("success", true);
            map.put("msg", "富文本上传成功");
            map.put("file_path", url);
            response.addHeader("Access-Control-Allow-Headers","X-File-Name");
            return map;
        }
        map.put("success", false);
        map.put("msg", "无权限");
        return map;
    }
}
