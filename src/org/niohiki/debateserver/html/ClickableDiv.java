package org.niohiki.debateserver.html;

public class ClickableDiv extends Div {

    public ClickableDiv(String classname, String action, String attributes, String contents, Tag... children) {
        super(classname, actionString(action, attributes), contents, children);
    }

    private static String actionString(String action, String attributes) {
        if (action.length() > 0) {
            return "onClick=\"" + action + "\" " + attributes;
        } else {
            return attributes;
        }
    }
}
