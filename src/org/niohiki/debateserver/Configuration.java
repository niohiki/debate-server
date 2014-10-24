package org.niohiki.debateserver;

import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * @author Santiago Codesido Sanchez
 **/
public class Configuration {

    public final Sides sides;
    public final Stances stances;

    public Configuration(Document doc) {
        Element root = (Element) doc.getElementsByTagName("configuration").item(0);
        sides = new Sides((Element) root.getElementsByTagName("sides").item(0));
        stances = new Stances((Element) root.getElementsByTagName("stances").item(0));
    }

    public class Sides {

        public final String yes;
        public final String no;
        public final String yesShort;
        public final String noShort;

        public Sides(Element e) {
            yes = e.getElementsByTagName("yes").item(0).getTextContent();
            no = e.getElementsByTagName("no").item(0).getTextContent();
            yesShort = e.getElementsByTagName("yesshort").item(0).getTextContent();
            noShort = e.getElementsByTagName("noshort").item(0).getTextContent();
        }
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
                String side = ee.getAttribute("side");
                boolean yes = "yes".equals(side);
                list.add(new Stance(ee.getTextContent(), yes, time));
            }
            number = list.size();
        }

        public Stance get(int i) {
            return list.get(i);
        }

        public class Stance {

            public final String fullName;
            public final String name;
            public final int time;
            public final boolean yesSide;

            public Stance(String name, boolean yes, int time) {
                this.name = name;
                this.fullName = name + " - " + (yes ? sides.yesShort : sides.noShort);
                this.time = time;
                this.yesSide = yes;
            }
        }
    }
}
