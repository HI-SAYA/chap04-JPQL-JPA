package com.ohgiraffers.section03.projection;

import javax.persistence.Embeddable;

@Embeddable // 엔티티는 아니다. 내장 가능한 타입. Class 설정하지 않아도 됨
public class MenuInfo {

    private String menuName;
    private int menuPrice;

    public MenuInfo() {
    }

    public MenuInfo(String menuName, int menuPrice) {
        this.menuName = menuName;
        this.menuPrice = menuPrice;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public int getMenuPrice() {
        return menuPrice;
    }

    public void setMenuPrice(int menuPrice) {
        this.menuPrice = menuPrice;
    }

    @Override
    public String toString() {
        return "MenuInfo{" +
                "menuName='" + menuName + '\'' +
                ", menuPrice=" + menuPrice +
                '}';
    }
}
