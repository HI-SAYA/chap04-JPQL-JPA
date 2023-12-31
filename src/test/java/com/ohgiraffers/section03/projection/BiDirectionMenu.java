package com.ohgiraffers.section03.projection;

import javax.persistence.*;

@Entity(name = "bidirection_menu")
@Table(name = "tbl_menu")
public class BiDirectionMenu {

    @Id
    private int menuCode;
    private String menuName;
    private int menuPrice;

    @ManyToOne
    @JoinColumn(name = "categoryCode")
    private BiDirectionCategory category;

    private String orderableStatus;

    public BiDirectionMenu() {
    }

    public BiDirectionMenu(int menuCode, String menuName, int menuPrice, BiDirectionCategory category, String orderableStatus) {
        menuCode = menuCode;
        this.menuName = menuName;
        this.menuPrice = menuPrice;
        this.category = category;
        this.orderableStatus = orderableStatus;
    }

    public int getMenuCode() {
        return menuCode;
    }

    public void setMenuCode(int menuCode) {
        menuCode = menuCode;
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

    public BiDirectionCategory getCategory() {
        return category;
    }

    public void setCategory(BiDirectionCategory category) {
        this.category = category;
    }

    public String getOrderableStatus() {
        return orderableStatus;
    }

    public void setOrderableStatus(String orderableStatus) {
        this.orderableStatus = orderableStatus;
    }

    @Override
    public String toString() {
        return "BiDirectionMenu{" +
                "MenuCode=" + menuCode +
                ", menuName='" + menuName + '\'' +
                ", menuPrice=" + menuPrice +
                ", category=" + category +
                ", orderableStatus='" + orderableStatus + '\'' +
                '}';
    }
}
