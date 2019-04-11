package cn.ganin.controller.backend;

import cn.ganin.common.Const;
import cn.ganin.common.ResponseCode;
import cn.ganin.common.ServerResponses;
import cn.ganin.pojo.Product;
import cn.ganin.pojo.User;
import cn.ganin.service.IFileService;
import cn.ganin.service.IProductService;
import cn.ganin.service.IUserService;
import cn.ganin.util.PropertiesUtil;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @Author agamgn
 * @Date 2018-08-06
 **/
@Controller
@RequestMapping("/manage/product")
public class ProductManageController {

    @Autowired
    private IUserService iUserService;
    @Autowired
    private IProductService iProductService;

    @Autowired
    private IFileService iFileService;

    /**
     * 保存商品
     * @param session
     * @param product
     * @return
     */
    @RequestMapping("save.do")
    @ResponseBody
    public ServerResponses procuctSave(HttpSession session, Product product){
        User user= (User) session.getAttribute(Const.CURRENT_USER);
        if (user==null){
            return ServerResponses.createByErroeCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录");
        }
        if (iUserService.checkAdminRole(user).isSuccess()){
//            填充增加产品的业务逻辑
           return iProductService.saveOrUpdateProduct(product);
        }
        else {
            return ServerResponses.createByErroeMessage("无权限操作");
        }

    }

    /**
     * 更新产品状态，上下架
     * @param session
     * @param productId
     * @param status
     * @return
     */
    @RequestMapping("setSaleStatus.do")
    @ResponseBody
    public ServerResponses setSaleStatus(HttpSession session, Integer productId,Integer status){
        User user= (User) session.getAttribute(Const.CURRENT_USER);
        if (user==null){
            return ServerResponses.createByErroeCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录");
        }
        if (iUserService.checkAdminRole(user).isSuccess()){
            return iProductService.setSaleStatus(productId,status);
        }
        else {
            return ServerResponses.createByErroeMessage("无权限操作");
        }
    }

    /**
     * 获取产品详情
     * @param session
     * @param productId
     * @return
     */
    @RequestMapping("detail.do")
    @ResponseBody
    public ServerResponses getDetail(HttpSession session, Integer productId){
        User user= (User) session.getAttribute(Const.CURRENT_USER);
        if (user==null){
            return ServerResponses.createByErroeCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录");
        }
        if (iUserService.checkAdminRole(user).isSuccess()){

            return iProductService.manageProductDetail(productId);
        }
        else {
            return ServerResponses.createByErroeMessage("无权限操作");
        }
    }


    /**
     * 后台商品列表
     * @param session
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping("list.do")
    @ResponseBody
    public ServerResponses getList(HttpSession session, @RequestParam(value ="pageNum",defaultValue = "1") int pageNum,@RequestParam(value = "pageSize",defaultValue = "10") int pageSize){
        User user= (User) session.getAttribute(Const.CURRENT_USER);
        if (user==null){
            return ServerResponses.createByErroeCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录");
        }
        if (iUserService.checkAdminRole(user).isSuccess()){
            return iProductService.getProductList(pageNum,pageSize);
        }
        else {
            return ServerResponses.createByErroeMessage("无权限操作");
        }
    }

    /**
     * 后台商品搜索
     * @param session
     * @param productName
     * @param productId
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping("search.do")
    @ResponseBody
    public ServerResponses productSerach(HttpSession session,String productName,Integer productId, @RequestParam(value ="pageNum",defaultValue = "1") int pageNum,@RequestParam(value = "pageSize",defaultValue = "10") int pageSize){
        User user= (User) session.getAttribute(Const.CURRENT_USER);
        if (user==null){
            return ServerResponses.createByErroeCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录");
        }
        if (iUserService.checkAdminRole(user).isSuccess()){
            return iProductService.searchProduct(productName,productId,pageNum,pageSize);
        }
        else {
            return ServerResponses.createByErroeMessage("无权限操作");
        }
    }


    /**
     * 文件上传
     * @param file
     * @param request
     * @return
     */
    @RequestMapping("upload.do")
    @ResponseBody
    public ServerResponses upload(HttpSession session,@RequestParam(value = "upload_file",required = false) MultipartFile file, HttpServletRequest request){
        User user= (User) session.getAttribute(Const.CURRENT_USER);
        if (user==null){
            return ServerResponses.createByErroeCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录");
        }
        if (iUserService.checkAdminRole(user).isSuccess()){
            String path=request.getSession().getServletContext().getRealPath("upload");
            String targetFileName=iFileService.upload(file,path);
            String url=PropertiesUtil.getProperty("ftp.server.http.prefix")+targetFileName;
            Map fileMap=Maps.newHashMap();
            fileMap.put("uri",targetFileName);
            fileMap.put("url",url);
            return ServerResponses.createBySuccess(fileMap);
        }
        else {
            return ServerResponses.createByErroeMessage("无权限操作");
        }
    }


    /**
     * 富文本文件上传
     * @param session
     * @param file
     * @param request
     * @return
     */
    @RequestMapping("richtextImgUpload.do")
    @ResponseBody
    public Map richtextImgUpload(HttpSession session, @RequestParam(value = "upload_file",required = false) MultipartFile file, HttpServletRequest request, HttpServletResponse response){
        Map resultMap=Maps.newHashMap();
        User user= (User) session.getAttribute(Const.CURRENT_USER);
        if (user==null){
            resultMap.put("success",false);
            resultMap.put("msg","请登录管理员");
            return resultMap;

        }
        if (iUserService.checkAdminRole(user).isSuccess()){
            String path=request.getSession().getServletContext().getRealPath("upload");
            String targetFileName=iFileService.upload(file,path);
            if (StringUtils.isBlank(targetFileName)){
                resultMap.put("success",false);
                resultMap.put("msg","上传失败");
                return resultMap;
            }

            String url=PropertiesUtil.getProperty("ftp.server.http.prefix")+targetFileName;
            resultMap.put("success",true);
            resultMap.put("msg","上传成功");
            resultMap.put("file_path",url);
            response.addHeader("Access-Control-Allow-Headers","X-File-Name");
            return resultMap;

        }
        else {
            resultMap.put("success",false);
            resultMap.put("msg","无权限操作");
            return resultMap;
        }
    }



}
