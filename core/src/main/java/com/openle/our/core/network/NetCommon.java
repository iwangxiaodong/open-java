package com.openle.our.core.network;

import com.openle.our.core.os.OS;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

public class NetCommon {

    //  虚拟网卡厂商前缀 - https://github.com/oshi/oshi/blob/master/oshi-core/src/main/resources/oshi.vmmacaddr.properties
    public final static Map<String, String> macMap = Map.ofEntries(Map.entry("00:50:56", "VMware ESX 3"),
            Map.entry("00:0C:29", "VMware ESX 3"),
            Map.entry("00:05:69", "VMware ESX 3"),
            Map.entry("00:03:FF", "Microsoft Hyper-V"),
            Map.entry("00:1C:42", "Parallels Desktop"),
            Map.entry("00:0F:4B", "Virtual Iron 4"),
            Map.entry("00:16:3E", "Xen or Oracle VM"),
            Map.entry("08:00:27", "VirtualBox"),
            Map.entry("02:42:AC", "Docker Container"));

    //  优先eth0、wlan0，排除虚拟网卡等；应考虑只开启wlan0,而未开eth0的情况
    //  参考 - https://github.com/oshi/oshi/blob/master/oshi-demo/src/main/java/oshi/demo/DetectVM.java#L75
    public static String firstLocalAddress() {
        var nis = NetCommon.physicalAdapterNetworkInterfaces();
        System.out.println("nis - " + nis);
        if (nis != null) {
            for (var ni : nis) {
                // Android 30+起getHardwareAddress全部返回null值 - if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.R) { }
                if (OS.isAndroid()) {
                    if ("wlan0".equalsIgnoreCase(ni.getName())) {
                        // 只关心IPv4（Inet4Address）地址，后续再考虑IPv6
                        var ipList = NetCommon.niAddresses(ni);
                        if (ipList != null && !ipList.isEmpty()) {
                            return ipList.get(0).getHostAddress();
                        }
                    }
                } else {
                    try {
                        var mac = NetCommon.macBytesToString(ni.getHardwareAddress());
                        System.out.println(mac); //
                        if (mac != null) {
                            mac = mac.replaceAll("-", ":").toUpperCase();
                            var oui = mac.length() > 7 ? mac.substring(0, 8) : mac;
                            System.out.println("oui - " + oui);
                            if (!macMap.containsKey(oui)) {
                                // 只关心IPv4（Inet4Address）地址，后续再考虑IPv6
                                var ipList = NetCommon.niAddresses(ni);
                                if (ipList != null && !ipList.isEmpty()) {
                                    return ipList.get(0).getHostAddress();
                                }
                            }
                        }
                    } catch (SocketException ex) {
                        ex.printStackTrace(System.err);
                    }
                }
            }
        }
        return null;
    }

    //  todo - 是否通过WMIC查出Windows物理网卡状态PhysicalAdapter值=True？
    //  https://github.com/profesorfalken/WMI4Java
    public static List<NetworkInterface> physicalAdapterNetworkInterfaces() {
        return allNetworkInterfaces().stream().filter(ni -> {
            try {
                return !(ni.isVirtual() || ni.isLoopback()) || "eth0".equals(ni.getName()) || "wlan0".equals(ni.getName());
            } catch (SocketException ex) {
                ex.printStackTrace(System.err);
            }
            return false;
        }).collect(Collectors.toList()); //  jdk17的android库依然未实现.toList();
    }

    //  todo - 地址不能在内网IP范围，即拿取首个外网IP
    public static String firstInternetAddress() {
        return null;
    }

    //  获取所有网络接口
    public static List<NetworkInterface> allNetworkInterfaces() {
        try {
            return Collections.list(NetworkInterface.getNetworkInterfaces());
        } catch (SocketException ex) {
            ex.printStackTrace(System.err);
        }
        return null;
    }

    public static List<InetAddress> niAddresses(NetworkInterface ni) {
        var ipList = Collections.list(ni.getInetAddresses()).stream()
                //  排除特殊网络地址
                .filter(ia -> !ia.isLoopbackAddress() && !ia.isLinkLocalAddress()
                //  暂只允许IPv4（Inet4Address）地址，后续再考虑IPv6
                && ia instanceof Inet4Address)
                .collect(Collectors.toList()); //  jdk17的android库依然未实现.toList();
        return ipList;
    }

    public static String macBytesToString(byte[] mac) {
        // 获得网络接口对象（即网卡），并得到mac地址，mac地址存在于一个byte数组中。
        StringBuffer sb = null;

        if (mac != null) {
            sb = new StringBuffer();
            // 把mac地址拼装成String
            for (int i = 0; i < mac.length; i++) {
                if (i != 0) {
                    sb.append("-");
                }
                // mac[i] & 0xFF 是为了把byte转化为正整数
                String s = Integer.toHexString(mac[i] & 0xFF);
                sb.append(s.length() == 1 ? 0 + s : s);
            }
        }

        // 把字符串所有小写字母改为大写成为正规的mac地址并返回
        return sb != null ? sb.toString().toUpperCase() : null;
    }

    //  netId入参格式为：192.168.1 用法：scanLAN(gatewayToNetworkID(DhcpInfo.gateway));
    //  扫描后可用 ip neigh 命令列出所有IP；Android 10(31)+ 限制了对该arp列表的访问？必须PACKAGE_USAGE_STATS权限？
    public static void scanLAN(String netId) {
        var dp = new DatagramPacket(new byte[0], 0, 0);
        try (var socket = new DatagramSocket()) {
            int position = 2; // 192.168.1.1 通常为网关
            while (position < 255) {
                var host = netId + "." + String.valueOf(position);
                System.err.println("UDP scan LAN - " + host);
                dp.setAddress(InetAddress.getByName(host));
                socket.send(dp);
                position++;
                //if (position == 125) { socket.close(); socket = new DatagramSocket(); } // 或分批？
            }
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
        System.out.println("Scan LAN over.");
    }

    //  android.net.DhcpInfo.gateway to 192.168.1
    public static String gatewayToNetworkID(int gateway) {

        // 通过di.netmask计算则可能范围过大，故写死256个主机号的情况：192.168.1
        return String.format(Locale.getDefault(),
                "%d.%d.%d",
                (gateway & 0xff),
                (gateway >> 8 & 0xff),
                (gateway >> 16 & 0xff));
    }

    //  android.net.DhcpInfo.gateway to 192.168.1.1
    public static String gatewayToIPString(int gateway) {
        try {
            String ip = InetAddress.getByAddress(
                    ByteBuffer.allocate(Integer.BYTES)
                            .order(ByteOrder.LITTLE_ENDIAN)
                            .putInt(gateway).array()
            ).getHostAddress();
            System.out.println(ip);
            return ip;
        } catch (UnknownHostException ex) {
            ex.printStackTrace(System.err);
        }
        return null;
    }


    /*
        以下方法均为老式写法：    
     */
//    //  android高版本方兼容stream()等方法
//    public static String getIP() {
//
//        // 只关心IPv4（Inet4Address）地址，后续再考虑IPv6
//        List<NetworkInterface> ipList = NetCommon.allNetworkInterfaces().stream()
//                .filter(ni -> getIP(ni) != null
//                ).collect(Collectors.toList());
//        if (!ipList.isEmpty()) {
//            return getIP(ipList.get(0));
//        }
//
//        return null;
//    }
//    public static String getIP(NetworkInterface ni) {
//        String ip = null;
//        if (ni != null) {
//            // 只关心IPv4（Inet4Address）地址，后续再考虑IPv6
//            List<InetAddress> ipList = Collections.list(ni.getInetAddresses()).stream()
//                    .filter(ia -> !ia.isLoopbackAddress() && !ia.isLinkLocalAddress() && ia instanceof Inet4Address)
//                    .collect(Collectors.toList());
//            if (!ipList.isEmpty()) {
//                return ipList.get(0).getHostAddress();
//            }
//        }
//        return ip;
//    }
    //  获取具备MAC地址的网络接口
    public static List<NetworkInterface> getNetworkInterfacesWithMAC() {

        List<NetworkInterface> niList = NetCommon.allNetworkInterfaces().stream().filter(ni -> {
            try {
                return ni.getHardwareAddress() != null;
            } catch (SocketException ex) {
                ex.printStackTrace(System.err);
            }
            return false;
        }).collect(Collectors.toList());

        return niList;
    }

    //  获取默认以太网接口
    //  InetAddress.getLocalHost() windows中正常获取，linux中则要确保/etc/hosts中主机名对应IP不能是127.x.x.x网段
    //  windows Output：DESKTOP-6AFR096/192.168.1.106    //  Debian Output： debian/65.49.201.245
    //  getHostAddress(getEthernetNetworkInterface())
    public static NetworkInterface getEthernetNetworkInterface() {
        NetworkInterface ni = null;

        try {
            ni = NetworkInterface.getByInetAddress(InetAddress.getLocalHost());
            if (ni != null && ni.isLoopback() && !OS.isWindows()) {
                System.out.println("NetworkInterface.getByName(eth0)");
                // 临时写死，后续考虑systemd网卡名ens3等
                ni = NetworkInterface.getByName("eth0");
            }
        } catch (UnknownHostException | SocketException ex) {
            ex.printStackTrace(System.err);
        }
        return ni;
    }

    public static String getEthernetMAC() {
        String mac = null;
        NetworkInterface ni = getEthernetNetworkInterface();
        try {
            if (ni != null) {
                mac = macBytesToString(ni.getHardwareAddress());
            }
        } catch (SocketException ex) {
            ex.printStackTrace(System.err);
        }
        return mac;
    }
//    public static String getWifiMAC() {
//        return getMAC(getWifiIP());
//    }
//
//    public static String getEthernetIP() {
//        // 注意windows的以太网卡可能是eth7等等
//        return getHostAddressByName("eth0");
//    }
//
//    public static String getWifiIP() {
//        return getHostAddressByName("wlan0");
//    }
//
//    private static String getHostAddressByName(String networkCardName) {
//        NetworkInterface networkInterface = null;
//        try {
//            networkInterface = NetworkInterface.getByName(networkCardName);
//        } catch (SocketException e) {
//            System.out.println(e.getMessage());
//        }
//
//        if (networkInterface != null) {
//            for (Enumeration<InetAddress> enumIPAddress = networkInterface.getInetAddresses(); enumIPAddress.hasMoreElements();) {
//                InetAddress inetAddress = enumIPAddress.nextElement();
//                if (!inetAddress.isLoopbackAddress()) {
//                    String ipAddress = inetAddress.getHostAddress();
//                    if (!ipAddress.contains("::")) {//ipV6的地址
//                        return ipAddress;
//                    }
//                }
//            }
//        }
//        return null;
//    }
//    public static InetAddress getLocalHost() {
//        Enumeration<NetworkInterface> netInterfaces = null;
//        try {
//            netInterfaces = NetworkInterface.getNetworkInterfaces();
//            while (netInterfaces.hasMoreElements()) {
//                NetworkInterface ni = netInterfaces.nextElement();
//                Enumeration<InetAddress> ips = ni.getInetAddresses();
//                while (ips.hasMoreElements()) {
//                    InetAddress ip = ips.nextElement();
//                    if (ip.isSiteLocalAddress()) {
//                        return ip;
//                    }
//                }
//            }
//        } catch (Exception e) {
//            System.err.println(e.getMessage());
//        }
//        return null;
//    }
//    public static String getMAC() {
//        String mac = null;
//        try {
//            //InetAddress.getLocalHost() on linux is error value 127.0.0.1
//            mac = getMAC(OS.isWindows() ? InetAddress.getLocalHost().getHostAddress() : getLocalHost().getHostAddress());
//        } catch (UnknownHostException | NullPointerException e) {
//            System.err.println(e);
//        }
//
//        if (mac == null && OS.isWindows()) {
//            List<String> macList = getWindowsMacList();
//            if (macList.size() > 0) {
//                mac = macList.get(0);
//                System.out.println("通过getmac命令随机获取mac地址：" + mac);
//            }
//        }
//
//        return mac;
//    }
//    private static String getMAC(String ipAddress) {
//        // 获得网络接口对象（即网卡），并得到mac地址，mac地址存在于一个byte数组中。
//        byte[] mac;
//        try {
//            mac = NetworkInterface.getByInetAddress(InetAddress.getByName(ipAddress)).getHardwareAddress();
//            if (mac != null) {
//                return macBytesToString(mac).toUpperCase();
//            }
//        } catch (SocketException | UnknownHostException e) {
//            System.out.println(e);
//        }
//        // 把字符串所有小写字母改为大写成为正规的mac地址并返回
//        return null;
//    }
//    public static List<String> getWindowsMacList() {
//        String result = Shell.runCommandAndReturn("getmac").v2;
//        return getMacListByResultString(result);
//    }
//
//    public static List<String> getMacListByResultString(String resultString) {
//        List<String> macs = new ArrayList<String>();
//        String reg = "([0-9A-Fa-f]{2}" + (OS.isWindows() ? "-" : ":") + "){5}[0-9A-Fa-f]{2}";
//        Matcher matcher = Pattern.compile(reg).matcher(resultString);
//        while (matcher.find()) {
//            macs.add(matcher.group());
//        }
//        return macs;
//    }
//
//    //for linux and android 禁用网口依然可以获取mac地址 (参数eth0 or wlan0)
//    public static String getLinuxMACEvenDisable(String networkCardName) {
//        BufferedReader reader = null;
//        String addressPath = "/sys/class/net/" + networkCardName + "/address";
//        try {
//
//            reader = new BufferedReader(new FileReader(
//                    addressPath));
//            String ethernetMac = reader.readLine();
//            System.out.println(networkCardName + " Mac: " + ethernetMac);
//            if (ethernetMac != null && ethernetMac.trim().length() > 0) {
//                return ethernetMac;
//            }
//        } catch (IOException e) {
//            System.out.println("open " + addressPath + " : " + e);
//        } finally {
//            try {
//                if (reader != null) {
//                    reader.close();
//                }
//            } catch (IOException e) {
//                System.out.println("close " + addressPath + " : " + e);
//            }
//        }
//        return null;
//    }
//    public static String getLinuxMac(String networkCardName) {
//        String macSerial = null;
//        Tuple3<Integer, String, String> r = Shell.runCommandAndReturn("cat /sys/class/net/" + networkCardName + "/address ");
//        System.out.println("getLinuxMacNew:" + r);
//        if (r.v1 == 0) {
//            macSerial = r.v2;
//        }
//        return macSerial;
//    }
}
