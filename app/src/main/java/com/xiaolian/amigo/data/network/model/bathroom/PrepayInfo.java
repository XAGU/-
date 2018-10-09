package com.xiaolian.amigo.data.network.model.bathroom;

import lombok.Data;


public class PrepayInfo {
    private double prepay ;
    private double minPrepay ;
    private double balance ;

    public double getPrepay() {
        return prepay;
    }

    public void setPrepay(double prepay) {
        this.prepay = prepay;
    }

    public double getMinPrepay() {
        return minPrepay;
    }

    public void setMinPrepay(double minPrepay) {
        this.minPrepay = minPrepay;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
