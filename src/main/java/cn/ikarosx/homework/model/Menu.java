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
}
