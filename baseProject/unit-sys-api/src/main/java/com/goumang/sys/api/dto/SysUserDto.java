package com.goumang.sys.api.dto;

/**
 * By huang.rb on 2019/8/8
 */
public class SysUserDto {
    /** 登录名 */
    private String loginName;

    /** 密码 */
    private String password;

    /** 验证码 */
    private String captcha;

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }
}
