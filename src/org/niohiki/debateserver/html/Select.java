package org.niohiki.debateserver.html;

public class Select extends Tag{
    public Select(String name){
        super("select");
        attribute("name", name);
    }
}
