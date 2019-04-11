package cn.ganin.controller.backend;

import cn.ganin.common.Const;
import cn.ganin.common.ServerResponses;
import cn.ganin.pojo.User;
import cn.ganin.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * @Author agamgn
 * @Date 2018-08-04
 **/
@Controller
@RequestMapping("/manage/user")
public class UserManageController {
    @Autowired
    private IUserService iUserService;

    /**
     * 管理员登录
     * @param username
     * @param password
     * @param session
     * @return
     */
    @RequestMapping(value = "login.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponses<User> login(String username, String password, HttpSession session){
        ServerResponses<User> response=iUserService.login(username,password);
        if (response.isSuccess()){
            User user=response.getData();
            if (user.getRole()==Const.Role.ROLE_ADMIN){
//                说明是管理员
                session.setAttribute(Const.CURRENT_USER,user);
                return response;
            }
            else {
                return ServerResponses.createByErroeMessage("不是管理园，无法登录");
            }
        }
        return response;
    }

}
