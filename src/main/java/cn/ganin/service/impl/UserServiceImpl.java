package cn.ganin.service.impl;

import cn.ganin.common.Const;
import cn.ganin.common.ServerResponses;
import cn.ganin.common.TokenCache;
import cn.ganin.dao.UserMapper;
import cn.ganin.pojo.Category;
import cn.ganin.pojo.User;
import cn.ganin.service.IUserService;
import cn.ganin.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @Author agamgn
 * @Date 2018-08-03
 **/
@Service("iUserService")
public class UserServiceImpl implements IUserService {


    @Autowired
    private UserMapper userMapper;

    /**
     * 登录校验
     * @param username
     * @param password
     * @return
     */
    @Override
    public ServerResponses<User> login(String username, String password) {
        int resultCount=userMapper.checkUsername(username);
        if (resultCount==0){
            return ServerResponses.createByErroeMessage("用户名不存在");
        }
//        密码加密解密
        String md5Password=MD5Util.MD5EncodeUtf8(password);
        User user=userMapper.selectLogin(username,md5Password);
        if (user==null){
            return ServerResponses.createByErroeMessage("密码错误");

        }
        user.setPassword(StringUtils.EMPTY);
        return ServerResponses.createBySuccess("登录成功",user);
    }


    /**
     * 注册业务
     * @param user
     * @return
     */
    public ServerResponses<User> register(User user){
       ServerResponses validResponse=this.checkValid(user.getUsername(),Const.USERNAME);
       if (!validResponse.isSuccess()){
           return validResponse;
       }
       validResponse=this.checkValid(user.getEmail(),Const.EMAIL);
       if (!validResponse.isSuccess()){
           return validResponse;
       }
        user.setRole(Const.Role.ROLE_CUSTOMER);
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));
        int resultCount=userMapper.insert(user);
        if (resultCount==0){
            return ServerResponses.createByErroeMessage("注册失败");
        }
        return ServerResponses.createBySuccessMessage("注册成功");
    }

    /**
     * 校验用户
     * @param str
     * @param type
     * @return
     */
    public ServerResponses<User> checkValid(String str,String type){
        if (StringUtils.isNoneBlank(type)){
            int resultCount=userMapper.checkUsername(str);
            if (resultCount>0){
                return ServerResponses.createByErroeMessage("用户已存在");
            }
        }
        if (Const.EMAIL.equals(type)){
            int resultCount=userMapper.checkEmail(str);
            if (resultCount>0){
                return ServerResponses.createByErroeMessage("邮箱已存在");
            }
        }
        else {
            return ServerResponses.createByErroeMessage("参数错误");
        }
        return ServerResponses.createBySuccessMessage("校验成功");
    }


    /**
     * 获取忘记密码的问题
     * @param username
     * @return
     */
    public ServerResponses selectQuestion(String username){
        ServerResponses validResponse=this.checkValid(username,Const.USERNAME);
        if (validResponse.isSuccess()){
            return ServerResponses.createByErroeMessage("用户不存在");
        }
        String question=userMapper.selectQuestionUsername(username);
        if (StringUtils.isNoneBlank(question)){
            return ServerResponses.createBySuccess(question);
        }
        return ServerResponses.createByErroeMessage("找回密码的问题是空的");

    }

    /**
     * 判断忘记密码的答案
     * @param username
     * @param question
     * @param answer
     * @return
     */
    public ServerResponses<String> checkAnswer(String username,String question,String answer){

        int resultCount=userMapper.checkAnswer(username,question,answer);
        if (resultCount>0){
//           设置token防止越权
            String forgetToken= UUID.randomUUID().toString();
            TokenCache.setKey(TokenCache.TOKEN_PREFIX+username,forgetToken);
            return ServerResponses.createBySuccess(forgetToken);

        }
        return ServerResponses.createByErroeMessage("问题的答案错误");
    }


    /**
     * 重置密码
     * @param username
     * @param passwordNew
     * @param forgetToken
     * @return
     */
    public ServerResponses<String> forgetRestPassword(String username,String passwordNew,String forgetToken){
        if (StringUtils.isBlank(forgetToken)){
            return ServerResponses.createByErroeMessage("参数错误,token需要传递");
        }
        ServerResponses validResponse=this.checkValid(username,Const.USERNAME);
        if (validResponse.isSuccess()){
            return ServerResponses.createByErroeMessage("用户不存在");
        }
        String token=TokenCache.getKey(TokenCache.TOKEN_PREFIX+username);
        if (StringUtils.isBlank(token)){
            return ServerResponses.createByErroeMessage("token无效或者过期");
        }
        if (StringUtils.equals(forgetToken,token)){
            String md5Password=MD5Util.MD5EncodeUtf8(passwordNew);
            int rowCount=userMapper.updatePasswordByUsername(username,md5Password);
            if (rowCount>0){
                return ServerResponses.createBySuccessMessage("修改密码成功");
            }
            else {
                return ServerResponses.createByErroeMessage("token错误,请重新获取重置密码的token");
            }
        }
        return ServerResponses.createByErroeMessage("修改密码失败");
    }

    /**
     * 登录状态下的重置密码
     * @param passwordOld
     * @param passwordNew
     * @param user
     * @return
     */
    public ServerResponses<String> resetPassword(String passwordOld,String passwordNew,User user){
        int resultCount=userMapper.checkPassword(MD5Util.MD5EncodeUtf8(passwordOld),user.getId());
        if (resultCount==0){
            return ServerResponses.createByErroeMessage("旧密码错误");
        }
        user.setPassword(MD5Util.MD5EncodeUtf8(passwordNew));
        int updateCount=userMapper.updateByPrimaryKeySelective(user);
        if (updateCount>0){
            return ServerResponses.createBySuccessMessage("密码更新成功");
        }
        return ServerResponses.createByErroeMessage("密码更新失败");
    }

    /**
     * 更新用户信息
     * @param user
     * @return
     */
    public ServerResponses<User> updateInformation(User user){
        int resultConut=userMapper.checkEmailByUserId(user.getEmail(),user.getId());
        if(resultConut>0){
            return ServerResponses.createByErroeMessage("email已存在，请更换emali");
        }
        User updateUser=new User();
        updateUser.setId(user.getId());
        updateUser.setEmail(user.getEmail());
        updateUser.setPhone(user.getPhone());
        updateUser.setQuestion(user.getQuestion());
        updateUser.setAnswer(updateUser.getAnswer());
        int updateCount=userMapper.updateByPrimaryKeySelective(updateUser);
        if (updateCount>0){
            return ServerResponses.createBySuccess("更新个人信息成功",updateUser);
        }
        return ServerResponses.createByErroeMessage("更新个人信息失败");
    }
    /**
     * 获取用户详细信息
     * @param userId
     * @return
     */
    public ServerResponses<User> get_information(Integer userId){
        User user=userMapper.selectByPrimaryKey(userId);
        if (user==null){
            return ServerResponses.createByErroeMessage("找不到当前用户");
        }
        user.setPassword(StringUtils.EMPTY);
        return ServerResponses.createBySuccess(user);
    }

    /**
     * 校验是否为管理员
     * @param user
     * @return
     */
    public ServerResponses checkAdminRole(User user){
        if (user!=null && user.getRole().intValue()==Const.Role.ROLE_ADMIN){
            return ServerResponses.createBySuccess();
        }
        return ServerResponses.createByErroe();
    }






}
