package cn.ikarosx.homework.model;

import lombok.Data;

/**
 * @author Ikarosx
 * @date 2020/8/16 20:59
 */
@Data
public class Menu {
    private String name;
    private Menu children;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Menu getChildren() {
        return children;
    }

    public void setChildren(Menu children) {
        this.children = children;
    }
}
