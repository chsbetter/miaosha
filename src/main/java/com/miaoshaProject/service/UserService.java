package com.miaoshaProject.service;


import com.miaoshaProject.error.BussinessException;
import com.miaoshaProject.service.model.UserModel;

public interface UserService {
    UserModel getUserById(Integer id);
    void register(UserModel userModel) throws BussinessException;
    /*
    * telephone：用户注册手机
    * password：用户加密后的密码
    * */
    UserModel validateLogin(String telephone,String encrptPassword) throws BussinessException;
}
