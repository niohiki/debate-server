package org.niohiki.debateserver;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author Santiago Codesido Sanchez
 **/
public class Utils {

    public static final String staticResourcePath = "./static/";
    public static final String staticResourceContext = "/static/";

    public static final String chronoContext = "/chrono";

    public static final String localeFile = "etc/locale.xml";
    public static final String configurationFile = "etc/configuration.xml";
    public static final String sessionFile = "etc/session.xml";

    public static String safeMD5(char[] psw) throws NoSuchAlgorithmException {
        byte[] b = new byte[psw.length << 1];
        for (int i = 0; i < psw.length; i++) {
            int bpos = i << 1;
            b[bpos] = (byte) ((psw[i] & 0xFF00) >> 8);
            b[bpos + 1] = (byte) (psw[i] & 0x00FF);
        }
        for (int i = 0; i < psw.length; i++) {
            psw[i] = ' ';
        }
        String md5 = new String(MessageDigest.getInstance("MD5").digest(b));
        for (int i = 0; i < b.length; i++) {
            b[i] = 0;
        }
        return md5;
    }
}
