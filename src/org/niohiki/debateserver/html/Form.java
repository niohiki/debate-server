package org.niohiki.debateserver.html;

public class Form extends Tag {

    public Form(String classname, String method, String action, Tag... children) {
        super("form", classname, "action=\"" + action + "\" method=\"" + method + "\"", "", children);
    }
}
