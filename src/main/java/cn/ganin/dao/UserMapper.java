package cn.ganin.dao;

import cn.ganin.pojo.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);
//    检查用户名存不存在的语句
    int checkUsername(String username);
//    检查email
    int checkEmail(String email);
//    检查用户名和密码
    User selectLogin(@Param("username") String username,@Param("password") String password);
//查询忘记密码的问题
    String selectQuestionUsername(String username);
//  检验问题的答案
    int checkAnswer(@Param("username")String username,@Param("question")String question,@Param("answer")String answer);
//    充值密码
    int updatePasswordByUsername(@Param("username")String username,@Param("passwordNew")String passwordNew);
//检验密码与id是否相同，防止横向越权
    int checkPassword(@Param("password") String passsword,@Param("userId")Integer userId);
//检查用户的邮箱
    int checkEmailByUserId(@Param("email")String email,@Param("userId")Integer userId);

}