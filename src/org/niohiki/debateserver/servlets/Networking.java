package org.niohiki.debateserver.servlets;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class Networking {

    public static String enumerateIPs() throws SocketException {
        Enumeration<NetworkInterface> nis = NetworkInterface.getNetworkInterfaces();
        StringBuilder result = new StringBuilder("");
        while (nis.hasMoreElements()) {
            NetworkInterface ni = nis.nextElement();
            Enumeration<InetAddress> as = ni.getInetAddresses();
            StringBuilder ips = new StringBuilder("");
            int ip_count = 0;
            while (as.hasMoreElements()) {
                ips.append("\n\t").append(as.nextElement().getHostAddress());
                ip_count++;
            }
            if (ip_count > 0) {
                result.append(ni.getDisplayName()).append(ips).append("\n");
            }
        }
        return result.toString();
    }
}
