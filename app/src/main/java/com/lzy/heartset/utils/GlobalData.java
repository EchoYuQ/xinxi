package com.lzy.heartset.utils;

/*
 * 该类用于存储关于用户登录信息的全局变量
 * 如 userid，sid
 */
public class GlobalData {

    private static String userid="2";
    private static String sid;
    public static final String URL_HEAD = "http://101.200.89.170";
    private static String tel;
    // 昵称
    private static String username;
    // 性别
    private static int sex;
    // 生日
    private static String birthday;
    // 邮箱
    private static String email;

    public static String getUserid() {
        return userid;
    }

    public static void setUserid(String userid) {
        GlobalData.userid = userid;
    }

    public static String getSid() {
        return sid;
    }

    public static void setSid(String sid) {
        GlobalData.sid = sid;
    }

    public static void clearUserInfo() {
        userid = null;
        sid = null;
    }

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        GlobalData.username = username;
    }

    public static int getSex() {
        return sex;
    }

    public static void setSex(int sex) {
        GlobalData.sex = sex;
    }

    public static String getBirthday() {
        return birthday;
    }

    public static void setBirthday(String birthday) {
        GlobalData.birthday = birthday;
    }

    public static String getEmail() {
        return email;
    }

    public static void setEmail(String email) {
        GlobalData.email = email;
    }

    public static String getTel() {
        return tel;
    }

    public static void setTel(String tel) {
        GlobalData.tel = tel;
    }
}
