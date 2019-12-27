package com.leus.common.util;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.leus.common.base.Objects;

import java.net.*;
import java.util.List;

/**
 * Created by duan on 2015/9/10.
 */
public class IPv4Util {

    private static final String IP_REGEX = "^(25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9]?[0-9])\\.(25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9]?[0-9])\\.(25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9]?[0-9])\\.(25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9]?[0-9])$";
    // Math.pow(2,32)-1
    private static long maxIPv4Addr = 4294967295l;
    private static long minIPv4Addr = 0;

    private IPv4Util() {
    }

    public static String getLocalIPAddress() throws UnknownHostException {
        return InetAddress.getLocalHost().getHostAddress();
    }

    public static String getMacAddress() throws UnknownHostException, SocketException {
        InetAddress ia = Inet4Address.getLocalHost();
        NetworkInterface intf = NetworkInterface.getByInetAddress(ia);
        byte[] mac = intf.getHardwareAddress();
        List<String> macStr = Lists.newArrayList();
        for (int i = 0; i < mac.length; i++) {
            //mac[i] & 0xFF 是为了把byte转化为正整数
            String s = Integer.toHexString(mac[i] & 0xFF);
            macStr.add(s.length() == 1 ? (0 + s) : s);
        }
        return Joiner.on("-").join(macStr);
    }

    public static boolean isIP(String ip) {
        return Objects.toString(ip).matches(IP_REGEX);
    }

    public static boolean isIP(Long ip) {
        return ip <= maxIPv4Addr && ip >= minIPv4Addr;
    }

    public static long getIPIntValue(String ip) {
        Preconditions.checkArgument(isIP(ip), "Not IP Address");
        Iterable<String> values = Splitter.on(".").split(ip);
        StringBuilder sb = new StringBuilder();
        values.forEach(value -> sb.append(Strings.padStart(Integer.toBinaryString(Integer.valueOf(value)), 8, '0')));
        return Long.valueOf(sb.toString(), 2);
    }

    public static String getIPStringValue(long ip) {
        Preconditions.checkArgument(isIP(ip), "Not IP Address");
        String bitStr = Strings.padStart(Long.toBinaryString(ip), 32, '0');
        String[] values = new String[4];
        for (int i = 0; i < values.length; i++) {
            values[i] = Integer.valueOf(bitStr.substring(i * 8, (i + 1) * 8), 2).toString();
        }
        return Joiner.on(".").join(values);
    }

}
