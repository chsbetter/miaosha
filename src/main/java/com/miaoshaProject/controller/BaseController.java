package com.miaoshaProject.controller;

import com.miaoshaProject.error.BussinessException;
import com.miaoshaProject.error.EmBussinessError;
import com.miaoshaProject.response.CommonReturnType;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class BaseController {
    public static final String CONTENT_TYPE_FORMED = "application/x-www-form-urlencoded";
    //定义exceptionhandler解决未被controller层吸收的exception
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Object handlerException(HttpServletRequest request, Exception ex){
        Map<String,Object> responseData = new HashMap<>();
        if (ex instanceof BussinessException){
            BussinessException bussinessException = (BussinessException) ex;
            responseData.put("errCode",bussinessException.getErrCode());
            responseData.put("errMsg",bussinessException.getErrMsg());
        }else {
            responseData.put("errCode", EmBussinessError.UNKNOWN_ERRPR.getErrCode());
            responseData.put("errMsg",EmBussinessError.UNKNOWN_ERRPR.getErrMsg());
        }
        return CommonReturnType.create(responseData,"fail");
    }
}
