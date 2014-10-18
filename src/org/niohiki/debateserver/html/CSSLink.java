package org.niohiki.debateserver.html;

public class CSSLink extends Tag {

    public CSSLink(String cssFile) {
        super("link", "", "rel=\"stylesheet\" type=\"text/css\" href=\"" + cssFile + "\"", "");
    }
}
