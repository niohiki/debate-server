package org.niohiki.debateserver.html;

public class Option extends Tag{
    public Option(String value,String text){
        super("option");
        attribute("value", value);
        content(text);
    }
}
