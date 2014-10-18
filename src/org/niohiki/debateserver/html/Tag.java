package org.niohiki.debateserver.html;

public class Tag {

    private final String name;
    private final String classname;
    private final String contents;
    private final String attributes;
    private final Tag[] children;

    public Tag(String name, String classname, String attributes, String contents, Tag... children) {
        this.name = name;
        this.classname = classname;
        this.contents = contents;
        this.attributes = attributes;
        this.children = children;
    }

    public final String toHTML() {
        return toHTML(0);
    }

    private String toHTML(int level) {
        StringBuilder makeContents = new StringBuilder(""), makeAttributes = new StringBuilder("");
        if (contents.length() > 0) {
            makeContents = new StringBuilder(tabs(level + 1)).
                    append(contents).append("\n");
        } else {
            makeContents = new StringBuilder("");
        }
        if (classname.length() > 0) {
            makeAttributes.append(" class=\"").append(classname).append("\"");
        }
        if (attributes.length() > 0) {
            makeAttributes.append(" ").append(attributes);
        }
        return new StringBuilder(tabs(level)).append("<").append(name).append(makeAttributes).append(">\n").
                append(makeContents).
                append(childrenHTML(level)).
                append(tabs(level)).append("</").append(name).append(">\n").toString();
    }

    private String childrenHTML(int level) {
        StringBuilder r = new StringBuilder("");
        for (Tag child : children) {
            r.append(child.toHTML(level + 1));
        }
        return r.toString();
    }

    private String tabs(int level) {
        StringBuilder t = new StringBuilder("");
        for (int i = 0; i < level; i++) {
            t.append("\t");
        }
        return t.toString();
    }
}
