package com.miaoshaProject.controller;

import com.alibaba.druid.util.StringUtils;
import com.miaoshaProject.controller.viewObject.UserVO;
import com.miaoshaProject.error.BussinessException;
import com.miaoshaProject.error.EmBussinessError;
import com.miaoshaProject.response.CommonReturnType;
import com.miaoshaProject.service.UserService;
import com.miaoshaProject.service.model.UserModel;
import org.apache.tomcat.util.security.MD5Encoder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Controller("user")
@RequestMapping("/user")
@CrossOrigin(allowCredentials = "true",allowedHeaders = "*")
public class UserController extends BaseController{

    @Autowired
    private UserService userService;

    @Autowired
    private HttpServletRequest httpServletRequest;

    //用户登录接口
    @RequestMapping(value = "/login",method = {RequestMethod.POST},consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType login(@RequestParam(name = "telephone") String telephone,
                                  @RequestParam(name = "password") String password) throws BussinessException, UnsupportedEncodingException, NoSuchAlgorithmException {
        //入参校验
        if(org.apache.commons.lang3.StringUtils.isEmpty(telephone) ||
            StringUtils.isEmpty(password)){
            throw new BussinessException(EmBussinessError.PARAMETER_VALIDATION_ERROR);
        }
        //用户登陆服务，用来校验用户登录是否合法
        UserModel userModel =  userService.validateLogin(telephone,this.EncodeByMd5(password));

        //将登录凭证加入到用户登录成功的session内
        this.httpServletRequest.getSession().setAttribute("IS_LOGIN",true);
        this.httpServletRequest.getSession().setAttribute("LOGIN_USER",userModel);
        return CommonReturnType.create(null);
    }

    //用户注册接口
    @RequestMapping(value = "/register",method = {RequestMethod.POST},consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType register(@RequestParam(name = "telephone") String telephone,
                                     @RequestParam(name = "otpCode") String otpCode,
                                     @RequestParam(name = "name") String name,
                                     @RequestParam(name = "gender") Integer gender,
                                     @RequestParam(name = "age") String age,
                                     @RequestParam(name = "password") String password
                                     ) throws BussinessException, UnsupportedEncodingException, NoSuchAlgorithmException {
        //验证手机号和对应的otpCode相符合
        String inSessionOtpCode = (String) this.httpServletRequest.getSession().getAttribute(telephone);
        if(!com.alibaba.druid.util.StringUtils.equals(otpCode,inSessionOtpCode)){
            throw new BussinessException(EmBussinessError.PARAMETER_VALIDATION_ERROR,"短信验证码不符合");
        }
        //用户的注册流程
        UserModel userModel = new UserModel();
        userModel.setName(name);
        userModel.setGender(new Byte(String.valueOf(gender.intValue())));
        userModel.setAge(age);
        userModel.setTelephone(telephone);
        userModel.setRegisterMode("byphone");
        userModel.setEncrptPassword(this.EncodeByMd5(password));

        userService.register(userModel);
        return CommonReturnType.create(null);


    }
    public String EncodeByMd5(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        //确定计算方法
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        BASE64Encoder base64en = new BASE64Encoder();
        //加密字符串
        String newstr = base64en.encode(md5.digest(str.getBytes("utf-8")));
        return newstr;
    }

    //用户获取otp短信接口
    @RequestMapping(value = "/getotp",method = {RequestMethod.POST},consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType getOtp(@RequestParam(name = "telephone") String telephone){
        //按照一定规则生成OTP验证码
        Random random  = new Random();
        int randomInt = random.nextInt(99999);
        randomInt += 10000;
        String otpCode = String.valueOf(randomInt);
        //将OTP验证码同对应用户的手机号关联,使用httpsession的方式绑定它的手机号与OTPCODE
        httpServletRequest.getSession().setAttribute(telephone,otpCode);
        //将OTP验证码通过短信通道发送给用户，省略
        System.out.println("Tele: "+telephone+" & otpCode: "+otpCode);
        return CommonReturnType.create(null);
    }

    @RequestMapping("/get")
    @ResponseBody
    public CommonReturnType getUser(@RequestParam(name = "id") Integer id) throws BussinessException {
        //调用service服务获取对应的id的用户对象并返回给前端
        UserModel userModel = userService.getUserById(id);
        if (userModel == null){
            //userModel.setEncrptPassword("123");
            throw new BussinessException(EmBussinessError.USER_NOT_EXIST);
        }
        UserVO userVO =  convertFromModel(userModel);
        //返回通用对象
        return CommonReturnType.create(userVO);

    }
    private UserVO convertFromModel(UserModel userModel){
        if (userModel == null){
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(userModel,userVO);
        return userVO;
    }

}
