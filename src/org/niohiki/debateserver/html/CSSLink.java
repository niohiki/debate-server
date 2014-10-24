package org.niohiki.debateserver.html;

/**
 * @author Santiago Codesido Sanchez
 **/
public class CSSLink extends Tag {

    public CSSLink(String cssFile) {
        super("link");
        attribute("rel", "stylesheet");
        attribute("type", "text/css");
        attribute("href", cssFile);
    }
}
