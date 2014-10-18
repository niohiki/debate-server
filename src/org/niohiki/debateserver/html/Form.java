package org.niohiki.debateserver.html;

public class Form extends Tag {

    public Form(String method,String action) {
        super("form");
        attribute("method", method);
        attribute("action", action);
    }
}
