package cn.ganin.controller.portal;

import cn.ganin.common.Const;
import cn.ganin.common.ResponseCode;
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
 * @Date 2018-08-03
 **/
@Controller
@RequestMapping("/user/")
public class UserController {

    @Autowired
    private IUserService iUserService;

    /**
     * 用户登录
     * @param username
     * @param password
     * @param session
     * @return
     */
    @RequestMapping(value = "login.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponses<User> login(String username, String password, HttpSession session){
        ServerResponses<User> responses=iUserService.login(username, password);
        if(responses.isSuccess()){
            session.setAttribute(Const.CURRENT_USER,responses.getData());
        }
        return responses;
    }

    /**
     * 用户退出
     * @param session
     * @return
     */
    @RequestMapping(value = "logout.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponses<String> logout(HttpSession session){
        session.removeAttribute(Const.CURRENT_USER);
        return ServerResponses.createBySuccess();
    }

    /**
     *用户注册
     * @param user
     * @return
     */
    @RequestMapping(value = "register.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponses<User> register(User user){
        return iUserService.register(user);
    }

    /**
     * 判断用户是否存在
     * @param str
     * @param type
     * @return
     */
    @RequestMapping(value = "checkUser.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponses<User> checkValid(String str,String type){
        return iUserService.checkValid(str,type);
    }

    /**
     * 获取用户信息
     * @param session
     * @return
     */
    @RequestMapping(value = "getUserInfo.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponses<User> getUserInfo(HttpSession session){
        User user= (User) session.getAttribute(Const.CURRENT_USER);
        if (user!=null){
            return ServerResponses.createBySuccess(user);
        }
        return ServerResponses.createByErroeMessage("用户未登陆,无法获取用户信息");
    }

    /**
     * 获取忘记密码的问题
     * @param username
     * @return
     */
    @RequestMapping(value = "forgetGetQuestion.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponses<String> forgetGetQuestion(String username){
        return iUserService.selectQuestion(username);
    }

    /**
     * 校验问题是否正确
     * @param username
     * @param question
     * @param answer
     * @return
     */
    @RequestMapping(value = "forgetCheckAnswer.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponses<String> forgetCheckAnswer(String username,String question,String answer){
        return iUserService.checkAnswer(username,question,answer);
    }

    /**
     * 重置密码
     * @param username
     * @param passwordNew
     * @param forgetToken
     * @return
     */
    @RequestMapping(value = "forgetResetPassword.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponses<String> forgetRestPassword(String username,String passwordNew,String forgetToken){
            return iUserService.forgetRestPassword(username,passwordNew,forgetToken);
    }

    /**
     * 登录状态下的重置密码
     * @param session
     * @param passwordOld
     * @param passwordNew
     * @return
     */
    @RequestMapping(value = "resetPassword.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponses<String> resetPassword(HttpSession session,String passwordOld,String passwordNew){
        User user= (User) session.getAttribute(Const.CURRENT_USER);
        if (user==null){
            return ServerResponses.createByErroeMessage("用户未登录");
        }
        return iUserService.resetPassword(passwordOld,passwordNew,user);
    }

    /**
     * 更新用户信息
     * @param session
     * @param user
     * @return
     */
    @RequestMapping(value = "updateInformation.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponses<User> update_information(HttpSession session,User user){
        User currentUser= (User) session.getAttribute(Const.CURRENT_USER);
        if (currentUser==null){
            return ServerResponses.createByErroeMessage("用户未登录");
        }
//        防止越权问题
        user.setId(currentUser.getId());
        user.setUsername(currentUser.getUsername());
        ServerResponses<User> response=iUserService.updateInformation(user);
        if (response.isSuccess()){
            session.setAttribute(Const.CURRENT_USER,response.getData());
        }
        return response;
    }

    /**
     * 获取用户详细信息
     * @param session
     * @return
     */
    @RequestMapping(value = "getInformation.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponses<User> get_information(HttpSession session){
        User currentUser= (User) session.getAttribute(Const.CURRENT_USER);
        if (currentUser==null){
            return ServerResponses.createByErroeCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"未登录，需要强制登录status=10");
        }
        return iUserService.get_information(currentUser.getId());
    }
}
