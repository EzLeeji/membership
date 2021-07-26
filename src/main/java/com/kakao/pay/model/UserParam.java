package com.kakao.pay.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserParam {
    private String membershipId;
    private String membershipName;
    private Long point;
    private int amount;

    public void setAmount(int amount) {
        this.amount = amount;
    }
}

