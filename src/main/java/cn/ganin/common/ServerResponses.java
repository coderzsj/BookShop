package cn.ganin.common;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.Serializable;

/**
 * 统一响应对象
 * @Author agamgn
 * @Date 2018-08-03
 **/
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ServerResponses<T> implements Serializable {

    private int status;
    private String msg;
    private T data;

    private ServerResponses(int status){
        this.status=status;
    }
    private ServerResponses(int status,T data){
        this.status=status;
        this.data=data;
    }
    private ServerResponses(int status,String msg,T data){
        this.status=status;
        this.msg=msg;
        this.data=data;
    }
    private ServerResponses(int status,String msg){
        this.status=status;
        this.msg=msg;
    }


    @JsonIgnore
    public boolean isSuccess(){
        return this.status==ResponseCode.SUCCESS.getCode();
    }
    public int getStatus(){
        return status;
    }
    public T getData(){
        return data;
    }
    public String getMsg(){
        return msg;
    }

//开放成功的的接口
    public static <T> ServerResponses<T> createBySuccess(){
        return new ServerResponses<T>(ResponseCode.SUCCESS.getCode());
    }
    public static <T> ServerResponses<T> createBySuccessMessage(String msg){
        return new ServerResponses<T>(ResponseCode.SUCCESS.getCode(),msg);
    }
    public static <T> ServerResponses<T> createBySuccess(T data){
        return new ServerResponses<T>(ResponseCode.SUCCESS.getCode(),data);
    }
    public static <T> ServerResponses<T> createBySuccess(String msg,T data){
        return new ServerResponses<T>(ResponseCode.SUCCESS.getCode(),msg,data);
    }

    public static <T> ServerResponses<T> createByErroe(){
        return new ServerResponses<T>(ResponseCode.ERROR.getCode(),ResponseCode.ERROR.getDesc());
    }

    public static <T> ServerResponses<T> createByErroeMessage(String errorMessage){
        return new ServerResponses<T>(ResponseCode.ERROR.getCode(),errorMessage);
    }
    public static <T> ServerResponses<T> createByErroeCodeMessage(int errorcode,String errorMessage){
        return new ServerResponses<T>(errorcode,errorMessage);
    }

}
