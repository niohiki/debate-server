package org.niohiki.debateserver.html;

public class Input extends Tag {

    public Input(String classname, String name, String value, String type) {
        super("input", classname, "value=\"" + value + "\" name=\""
                + name + "\" type=\"" + type + "\"", "");
    }
}
