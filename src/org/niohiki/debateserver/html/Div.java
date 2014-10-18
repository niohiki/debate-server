package org.niohiki.debateserver.html;

public class Div extends Tag {

    public Div(String classname, String attributes, String contents, Tag... children) {
        super("div", classname, attributes, contents, children);
    }
}
