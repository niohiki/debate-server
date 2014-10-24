package org.niohiki.debateserver.html;

/**
 * @author Santiago Codesido Sanchez
 **/
public class Div extends Tag {

    public Div(String classname) {
        super("div");
        attribute("class", classname);
    }
}
