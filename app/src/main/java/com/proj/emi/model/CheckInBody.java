package com.proj.emi.model;

import com.proj.emi.model.base.InBody;

/**
 * Created by Administrator on 2017/12/5 0005.
 */

public class CheckInBody extends InBody {
    private String is_correct;
    private String correct_sign;

    public String getIs_correct() {
        return is_correct;
    }

    public void setIs_correct(String is_correct) {
        this.is_correct = is_correct;
    }

    public String getCorrect_sign() {
        return correct_sign;
    }

    public void setCorrect_sign(String correct_sign) {
        this.correct_sign = correct_sign;
    }
}
