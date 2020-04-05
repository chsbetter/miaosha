package com.miaoshaProject.controller;

import com.miaoshaProject.error.BussinessException;
import com.miaoshaProject.error.EmBussinessError;
import com.miaoshaProject.response.CommonReturnType;
import com.miaoshaProject.service.OrderService;
import com.miaoshaProject.service.model.OrderModel;
import com.miaoshaProject.service.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller("/order")
@RequestMapping("/order")
@CrossOrigin(origins = {"*"},allowCredentials = "true")
public class OrderController extends BaseController{
    @Autowired
    private OrderService orderService;
    @Autowired
    private HttpServletRequest httpServletRequest;
    //封装下单请求
    @RequestMapping(value = "/createorder",method = {RequestMethod.POST},consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType createOrder(@RequestParam(name = "itemId")Integer itemId,
                                        @RequestParam(name = "amount")Integer amount,
                                        @RequestParam(name = "promoId",required = false)Integer promoId) throws BussinessException {
        Boolean isLogin = (Boolean) httpServletRequest.getSession().getAttribute("IS_LOGIN");
        if (isLogin == null || !isLogin.booleanValue()){
            throw new BussinessException(EmBussinessError.USER_NOT_LOGIN,"用户还未登录，无法下单");
        }
        //获取用户的登录信息
        UserModel userModel = (UserModel) httpServletRequest.getSession().getAttribute("LOGIN_USER");
        OrderModel orderModel = orderService.createOrder(userModel.getId(),itemId,promoId,amount);
        return CommonReturnType.create(null);
    }
}
