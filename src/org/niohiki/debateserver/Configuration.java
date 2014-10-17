package org.niohiki.debateserver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class Configuration {

    public final Stances stances;

    public Configuration(Document doc) {
        Element root = (Element) doc.getElementsByTagName("configuration").item(0);
        stances = new Stances((Element) root.getElementsByTagName("stances").item(0));
    }

    public class Stances {

        private final List<Stance> list;
        public final int number;

        public Stances(Element e) {
            NodeList stance_list = e.getElementsByTagName("stance");
            list = new ArrayList<>();
            for (int i = 0; i < stance_list.getLength(); i++) {
                Element ee = (Element) stance_list.item(i);
                int time = Integer.parseInt(ee.getAttribute("time"));
                list.add(new Stance(ee.getTextContent(), time));
            }
            number = list.size();
        }

        public Stance get(int i) {
            return list.get(i);
        }

        public class Stance {

            public final String name;
            public final int time;

            public Stance(String name, int time) {
                this.name = name;
                this.time = time;
            }
        }
    }
}
