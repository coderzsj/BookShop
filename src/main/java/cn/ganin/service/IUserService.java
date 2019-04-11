package cn.ganin.service;

import cn.ganin.common.ServerResponses;
import cn.ganin.pojo.User;

/**
 * @Author agamgn
 * @Date 2018-08-03
 **/
public interface IUserService {
    ServerResponses<User> login(String username, String password);
    ServerResponses<User> register(User user);
    ServerResponses<User> checkValid(String str,String type);
    ServerResponses selectQuestion(String username);
    ServerResponses<String> checkAnswer(String username,String question,String answer);
    ServerResponses<String> forgetRestPassword(String username,String passwordNew,String forgetToken);
    ServerResponses<String> resetPassword(String passwordOld,String passwordNew,User user);
    ServerResponses<User> updateInformation(User user);
    ServerResponses<User> get_information(Integer userId);
    ServerResponses checkAdminRole(User user);

}
