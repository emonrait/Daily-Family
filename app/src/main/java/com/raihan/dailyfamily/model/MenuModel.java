package com.raihan.dailyfamily.model;

public class MenuModel {
    private String menuName;
    private int icon;
    private String softcode;

    public MenuModel(String menuName, int icon, String softcode) {
        this.menuName = menuName;
        this.icon = icon;
        this.softcode = softcode;

    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }



    public String getSoftcode() {
        return softcode;
    }

    public void setSoftcode(String softcode) {
        this.softcode = softcode;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
