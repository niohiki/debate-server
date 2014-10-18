package org.niohiki.debateserver.html;

public class Div extends Tag {

    public Div(String classname) {
        super("div");
        attribute("class", classname);
    }
}
