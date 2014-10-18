package org.niohiki.debateserver.html;

public class Tag {

    private final String name;
    private final String contents;
    private final String attributes;
    private final Tag[] children;

    public Tag(String name, String attributes, String contents, Tag... children) {
        this.name = name;
        this.contents = contents;
        this.attributes = attributes;
        this.children = children;
    }

    public final String toHTML() {
        return toHTML(0);
    }

    private String toHTML(int level) {
        String makeContents, makeAttributes;
        if (contents.length() > 0) {
            makeContents = new StringBuilder(tabs(level + 1)).
                    append(contents).append("\n").toString();
        } else {
            makeContents = "";
        }
        if (attributes.length() > 0) {
            makeAttributes = new StringBuilder(" ").append(attributes).toString();
        } else {
            makeAttributes = "";
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
