package com.proj.emi.bean;

import com.alibaba.fastjson.annotation.JSONField;
import com.proj.emi.model.base.InBody;

public class UserBasicVO extends InBody {
    //缺少  性别
    private String nickname;//	昵称	返回详细信息
    private String avatar;//	头像	用户头像url
    private String height;//	身高	用户的身高
    private String weight;//	体重	用户的体重
    private String age;//	    年龄	用户的年龄
    @JSONField(name = "bg_image")
    private String bgImage;//   背景图	用户的背景url
    private String gender;
    private String birthday;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getBgImage() {
        return bgImage;
    }

    public void setBgImage(String bgImage) {
        this.bgImage = bgImage;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }
}
