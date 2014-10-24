package org.niohiki.debateserver.html;

/**
 * @author Santiago Codesido Sanchez
 **/
public class Select extends Tag{
    public Select(String name){
        super("select");
        attribute("name", name);
    }
}
