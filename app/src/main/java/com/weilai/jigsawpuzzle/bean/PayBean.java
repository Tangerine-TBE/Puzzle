package com.weilai.jigsawpuzzle.bean;
/**
 * 购买VIP
 * */
public class PayBean {
    private String time;
    private Double money;
    private Double oldMoney;
    private Boolean select;
    private String vipType;
    private String body;
    private String subject;

    public PayBean() {
    }

    public PayBean(String vipType, String name, Double money, Double oldMoney, Boolean select, String body, String subject) {
        this.vipType = vipType;
        this.time = name;
        this.money = money;
        this.oldMoney = oldMoney;
        this.select = select;
        this.body = body;
        this.subject = subject;
    }

    public String getVipType() {
        return vipType;
    }

    public void setVipType(String vipType) {
        this.vipType = vipType;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public Boolean getSelect() {
        return select;
    }

    public void setSelect(Boolean select) {
        this.select = select;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Double getOldMoney() {
        return oldMoney;
    }

    public void setOldMoney(Double oldMoney) {
        this.oldMoney = oldMoney;
    }
}
