package org.niohiki.debateserver.html;

public class Div extends Tag {

    public Div(String classname, String attributes, String contents, Tag... children) {
        super("div", classString(classname,attributes), contents, children);
    }

    private static String classString(String classname,String attributes) {
        if (classname.length() > 0) {
            return "class=\"" + classname + "\" "+attributes;
        } else {
            return attributes;
        }
    }
}
