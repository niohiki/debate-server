package org.niohiki.debateserver.html;

/**
 * @author Santiago Codesido Sanchez
 **/
public class Option extends Tag{
    public Option(String value,String text){
        super("option");
        attribute("value", value);
        content(text);
    }
}
