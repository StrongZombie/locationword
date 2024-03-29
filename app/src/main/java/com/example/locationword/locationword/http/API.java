package com.example.locationword.locationword.http;

/**
 * Created by Administrator on 2017/10/16.
 */

public class API {
    /**
     * 基本路径
     */
    public static final String BASEURL = "http://172.17.146.156:8082/locationword";
    //检查登录
    public static final String checkLogin = BASEURL+"/Login/checklogin";
    //注册用户
    public static final String registerUser = BASEURL+"/Register/addRegister";
    //重置密码
    public static final String resetPassword = BASEURL+"/Login/resetPassword";
    //获取当前群组的用户信息
    public static final String getGroupUser = BASEURL+"/User/getGroupUser";
    //获取邀请进入群的用户列表
    public static final String getSearchMan = BASEURL+"/User/getSearchUsers";
    //模糊搜索用户列表
    public static final String SearchMantoInvite = BASEURL+"/User/selectUser";
    //新增用户定位
    public static final String addUserLocation = BASEURL+"/Location/addUserLocation";
    //更改用户定位
    public static final String updateUserLocation = BASEURL+"/Location/updateUserLocation";
    //根据Id获取用户定位
    public static final String getUserLocation = BASEURL+"/Location/getUserLocation";
    //根据Id获取用户定位(数组)
    public static final String getUserLocationA = BASEURL+"/Location/getUserLocationA";
    //改变用户定位状态
    public static final String changeLocationState = BASEURL+"/Location/changeLocationState";
    //获取昵称或者头像
    public static final  String getUserDetail = BASEURL+"/User/getUserDetail";
    //申请加人群
    public static final String addGroupPush = BASEURL+"/JPush/addGroupPush";
    //多用推送
    public static final String userJPush = BASEURL+"/JPush/userJPush";


    //重置手机号码
    public static final String reChangePhone = BASEURL+"/User/changePhone";
    //重置我的头像
    public static final String reAvarl = BASEURL+"/User/changeAvarl";
    //重置用户昵称
    public static final String reNickname =  BASEURL+"/User/changeNickname";
    //重置用户真实姓名
    public static final String reRealname=  BASEURL+"/User/changeRealname";
    //重置用户性别
    public static final String reSex=  BASEURL+"/User/changeSex";
    //添加群组
    public static final String addGroup = BASEURL+"/Group/addGroup";

}


















































