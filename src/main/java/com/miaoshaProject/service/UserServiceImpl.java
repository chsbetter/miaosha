package com.miaoshaProject.service;

import com.alibaba.druid.util.StringUtils;
import com.miaoshaProject.dao.UserDOMapper;
import com.miaoshaProject.dao.UserPasswordDOMapper;
import com.miaoshaProject.dataobject.UserDO;
import com.miaoshaProject.dataobject.UserPasswordDO;
import com.miaoshaProject.error.BussinessException;
import com.miaoshaProject.error.EmBussinessError;
import com.miaoshaProject.service.model.UserModel;
import com.miaoshaProject.validator.ValidationResult;
import com.miaoshaProject.validator.ValidatorImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private UserDOMapper userDOMapper;
    @Autowired
    private UserPasswordDOMapper userPasswordDOMapper;
    @Autowired
    private ValidatorImpl validator;
    @Override
    public UserModel getUserById(Integer id) {
        //调用userDOMapper获取到对应的用户dataobject
        UserDO userDO = userDOMapper.selectByPrimaryKey(id);
        if (userDO == null){
            return null;
        }
        UserPasswordDO userPasswordDO = userPasswordDOMapper.selectByUserId(userDO.getId());
        return convertFromDataObject(userDO,userPasswordDO);

    }

    @Override
    @Transactional
    public void register(UserModel userModel) throws BussinessException {
        if (userModel == null){
            throw new BussinessException(EmBussinessError.PARAMETER_VALIDATION_ERROR);
        }
/*        if (StringUtils.isEmpty(userModel.getName())
                || (userModel.getGender() == null)
                || (userModel.getAge() == null)
                || StringUtils.isEmpty(userModel.getTelephone())){
            throw new BussinessException(EmBussinessError.PARAMETER_VALIDATION_ERROR);
        }*/
        ValidationResult result = validator.validate(userModel);
        if (result.isHasErrors()){
            throw new BussinessException(EmBussinessError.PARAMETER_VALIDATION_ERROR,result.getErrMsg());
        }

        //实现model->dataobject方法
        UserDO userDO = convertFromModel(userModel);
        try{
            userDOMapper.insertSelective(userDO);
        }catch (DuplicateKeyException ex){
            throw new BussinessException(EmBussinessError.PARAMETER_VALIDATION_ERROR,"该手机号已注册");
        }

        UserPasswordDO userPasswordDO = convertPasswordFromModel(userModel);
        userPasswordDO.setUserId(userDO.getId());
        userPasswordDOMapper.insertSelective(userPasswordDO);

        return;
    }

    @Override
    public UserModel validateLogin(String telephone, String encrptPassword) throws BussinessException {
        //通过用户的手机获取用户的信息
        UserDO userDO = userDOMapper.selectByTelephone(telephone);
        if(userDO == null){
            throw new BussinessException(EmBussinessError.USER_LOGIN_FAIL);
        }
        UserPasswordDO userPasswordDO = userPasswordDOMapper.selectByUserId(userDO.getId());
        UserModel userModel = convertFromDataObject(userDO,userPasswordDO);

        //比对用户的加密的密码是否和传输进来的密码相匹配
        if (!StringUtils.equals(encrptPassword,userModel.getEncrptPassword())){
            throw new BussinessException(EmBussinessError.USER_LOGIN_FAIL);
        }
        return userModel;
    }

    private UserPasswordDO convertPasswordFromModel(UserModel userModel){
        if(userModel == null){
            return null;
        }
        UserPasswordDO userPasswordDO = new UserPasswordDO();
        userPasswordDO.setEncrptPassword(userModel.getEncrptPassword());
        userPasswordDO.setUserId(userModel.getId());
        return userPasswordDO;
    }
    private UserDO convertFromModel(UserModel userModel){
        if(userModel == null){
            return null;
        }
        UserDO userDO = new UserDO();
        BeanUtils.copyProperties(userModel,userDO);
        return userDO;
    }

    private UserModel convertFromDataObject(UserDO userDO, UserPasswordDO userPasswordDO){
        if (userDO == null){
            return null;
        }
        UserModel userModel = new UserModel();
        BeanUtils.copyProperties(userDO,userModel);
        if(userPasswordDO != null){
            userModel.setEncrptPassword(userPasswordDO.getEncrptPassword());
        }
        return userModel;
    }
}
