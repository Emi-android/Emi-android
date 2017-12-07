package com.proj.emi.model.login;

import com.proj.emi.model.base.OutBody;

public class LoginOutBody extends OutBody {
    // 密码	是	字符串	密码由前端生成。32位md5码。
    // 生成规则：md5(当前时间+用户手机+密钥)。
    // 密钥为不变的6位字符串（vcar11） 示例：md5(2015-07-13 15:44:0013705185091vcar11)
    private String password;
    private String code;//	验证码	是	字符串	6位纯数字
    private String phone;//	电话号码	是	字符串	11位纯数字

    public String getPassword() {
        return password;
    }

    public LoginOutBody setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getCode() {
        return code;
    }

    public LoginOutBody setCode(String code) {
        this.code = code;
        return this;
    }

    public String getPhone() {
        return phone;
    }

    public LoginOutBody setPhone(String phone) {
        this.phone = phone;
        return this;
    }
}
