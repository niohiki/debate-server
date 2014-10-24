package org.niohiki.debateserver;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 *
 * @author Santi
 */
public class Configuration {

    public final String sessionFile;
    public final String optionsFile;
    public final String localeFile;

    public Configuration(File f) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
        HashMap<String, String> keys = new HashMap<>();
        String line = br.readLine();
        while (line != null) {
            String[] splitted = line.split("=");
            keys.put(splitted[0], splitted[1]);
            line = br.readLine();
        }
        sessionFile = Utils.configurationDir + keys.get(Utils.sessionKey);
        optionsFile = Utils.configurationDir + keys.get(Utils.optionsKey);
        localeFile = Utils.configurationDir + keys.get(Utils.localeKey);
    }
}
