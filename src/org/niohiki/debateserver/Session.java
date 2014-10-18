package org.niohiki.debateserver;

import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class Session {

    public final Teams teams;

    public Session(Document doc) {
        Element root = (Element) doc.getElementsByTagName("session").item(0);
        teams = new Teams((Element) root.getElementsByTagName("teams").item(0));
    }

    public class Teams {

        private final List<Team> list;
        public final int number;

        public Teams(Element e) {
            NodeList stance_list = e.getElementsByTagName("team");
            list = new ArrayList<>();
            for (int i = 0; i < stance_list.getLength(); i++) {
                Element ee = (Element) stance_list.item(i);
                list.add(new Team(ee.getTextContent()));
            }
            number = list.size();
        }

        public Team get(int i) {
            return list.get(i);
        }

        public class Team {

            public final String name;

            public Team(String name) {
                this.name = name;
            }
        }
    }
}
