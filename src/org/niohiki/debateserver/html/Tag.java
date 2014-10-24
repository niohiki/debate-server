package org.niohiki.debateserver.html;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Santiago Codesido Sanchez
 **/
public class Tag {

    private final String name;
    private String contents;
    private final ArrayList<Tag> children;
    private final ArrayList<Attribute> attributes;

    public Tag(String name) {
        this.name = name;
        this.contents = "";
        this.children = new ArrayList<>();
        this.attributes = new ArrayList<>();
    }

    public Tag attribute(String name, String value) {
        attributes.add(new Attribute(name, value));
        return this;
    }

    public Tag child(Tag... child) {
        children.addAll(Arrays.asList(child));
        return this;
    }

    public Tag content(String content) {
        this.contents = content;
        return this;
    }

    public final String toHTML() {
        return toHTML(0);
    }

    @Override
    public String toString() {
        return toHTML();
    }

    private String toHTML(int level) {
        StringBuilder makeContents = new StringBuilder(""), makeAttributes = new StringBuilder("");
        if (contents.length() > 0) {
            makeContents.append(tabs(level + 1)).append(contents).append("\n");
        }
        attributes.forEach((a) -> {
            makeAttributes.append(" ").append(a.name).append("=\"").append(a.value).append("\"");
        });
        return new StringBuilder(tabs(level)).append("<").append(name).append(makeAttributes).append(">\n").
                append(makeContents).
                append(childrenHTML(level)).
                append(tabs(level)).append("</").append(name).append(">\n").toString();
    }

    private String childrenHTML(int level) {
        StringBuilder r = new StringBuilder("");
        children.forEach((child) -> {
            r.append(child.toHTML(level + 1));
        });
        return r.toString();
    }

    private String tabs(int level) {
        StringBuilder t = new StringBuilder("");
        for (int i = 0; i < level; i++) {
            t.append("\t");
        }
        return t.toString();
    }

    public class Attribute {

        public final String name;
        public final String value;

        public Attribute(String name, String value) {
            this.name = name;
            this.value = value;
        }
    }
}
