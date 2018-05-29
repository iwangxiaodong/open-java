package com.openle.module.core.network;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Locale;
import java.util.Map;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class NetCommon {

    //  获取所有网络接口
    public static List<NetworkInterface> getAllNetworkInterfaces() {
        try {
            return Collections.list(NetworkInterface.getNetworkInterfaces());
            // or return NetworkInterface.getNetworkInterfaces().asIterator();
        } catch (SocketException ex) {
            Logger.getLogger(NetCommon.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    //  获取具备MAC地址的网络接口
    public static List<NetworkInterface> getNetworkInterfacesWithMAC() {

        List<NetworkInterface> niList = NetCommon.getAllNetworkInterfaces().stream().filter(ni -> {
            try {
                return ni.getHardwareAddress() != null;
            } catch (SocketException ex) {
                Logger.getLogger(NetCommon.class.getName()).log(Level.SEVERE, null, ex);
            }
            return false;
        }).collect(Collectors.toList());

        return niList;
    }

    //  获取默认以太网接口
    //  InetAddress.getLocalHost() windows中正常获取，linux中则要确保/etc/hosts中主机名对应IP不能是127.x.x.x网段
    //  windows Output：DESKTOP-6AFR096/192.168.1.106    //  Debian Output： debian/65.49.201.245
    public static NetworkInterface getEthernetNetworkInterface() {
        NetworkInterface ni = null;
        try {
            ni = NetworkInterface.getByInetAddress(InetAddress.getLocalHost());
        } catch (UnknownHostException | SocketException ex) {
            Logger.getLogger(NetCommon.class.getName()).log(Level.SEVERE, null, ex);
        }
        //System.out.println(ni.getName());
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
            Logger.getLogger(NetCommon.class.getName()).log(Level.SEVERE, null, ex);
        }
        return mac;
    }

    public static String getHostAddress(NetworkInterface ni) {
        String ip = null;
        if (ni != null) {
            // 只关心IPv4（Inet4Address）地址，后续再考虑IPv6
            List<InetAddress> ipList = Collections.list(ni.getInetAddresses()).stream()
                    .filter(ia -> !ia.isLoopbackAddress() && ia instanceof Inet4Address)
                    .collect(Collectors.toList());
            if (ipList.size() > 0) {
                return ipList.get(0).getHostAddress();
            }
        }
        return ip;
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
    //
    //part.getHeader(contentDisposition)
    // Header information
    //public static String contentDisposition = "Content-Disposition";
    // from org.apache.catalina.core > ApplicationPart.java
    @SuppressWarnings("unchecked")
    public static String getFileName(String cd) {
        String fileName = null;
        // String cd = getHeader("Content-Disposition");
        if (cd != null) {
            String cdl = cd.toLowerCase(Locale.ENGLISH);
            if (cdl.startsWith("form-data") || cdl.startsWith("attachment")) {
                ParameterParser paramParser = new ParameterParser();
                paramParser.setLowerCaseNames(true);
                // Parameter parser can handle null input
                Map<String, String> params = paramParser.parse(cd, ';');
                if (params.containsKey("filename")) {
                    fileName = params.get("filename");
                    if (fileName != null) {
                        fileName = fileName.trim();
                    } else {
                        // Even if there is no value, the parameter is present,
                        // so we return an empty file name rather than no file
                        // name.
                        fileName = "";
                    }
                }
            }
        }
        return fileName;
    }

    public static String httpGet(String url) {
        String content = null;
        ByteArrayOutputStream baoStream = null;
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, new TrustManager[]{new MyTrustManager()},
                    new SecureRandom());
            HttpsURLConnection
                    .setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection
                    .setDefaultHostnameVerifier(new MyHostnameVerifier());

            URL urlObj = new URL(url);
            HttpURLConnection httpConn = (HttpURLConnection) urlObj.openConnection();
            httpConn.setDoInput(true);
            httpConn.setDoOutput(true);

            // 此方法在正式链接之前设置才有效。
            httpConn.setRequestMethod("GET");
            httpConn.setUseCaches(false);
            // 正式创建链接
            httpConn.connect();

            // 开始GET数据
            String encoding = httpConn.getContentEncoding();
            InputStream is = httpConn.getInputStream();
            int read = -1;
            baoStream = new ByteArrayOutputStream();
            while ((read = is.read()) != -1) {
                baoStream.write(read);
            }
            byte[] data = baoStream.toByteArray();
            baoStream.close();

            if (encoding != null) {
                content = new String(data, encoding);
            } else {
                content = new String(data);
            }

        } catch (IOException e) {
            System.out.println("发送GET请求出现异常！" + e);
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            System.out.println(e);
        } finally {
            try {
                if (baoStream != null) {
                    baoStream.close();
                }
            } catch (IOException ex) {
                System.out.println("发送GET请求finally出现异常！" + ex);
            }
        }

        return content;
    }

    public static ReturnStringAndError requestWithoutCA(String urlString) {
        ReturnStringAndError r = new ReturnStringAndError();
        String html = "";
        try {

            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, new TrustManager[]{new MyTrustManager()},
                    new SecureRandom());
            HttpsURLConnection
                    .setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection
                    .setDefaultHostnameVerifier(new MyHostnameVerifier());

            URL url = new URL(urlString);
            HttpURLConnection urlConnection = (HttpURLConnection) url
                    .openConnection();

            InputStream in = urlConnection.getInputStream();
            // 取得输入流，并使用Reader读取
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(in));

            System.out.println("Contents of get request");
            String lines;

            while ((lines = reader.readLine()) != null) {
                html += lines;
            }
            reader.close();
            // 断开连接
            urlConnection.disconnect();

            r.setString(html);

            System.out.println("Contents of get request ends");
        } catch (MalformedURLException e) {
            r.setError(e.getMessage());
        } catch (IOException e) {
            r.setError(e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            r.setError(e.getMessage());
        } catch (KeyManagementException e) {
            r.setError(e.getMessage());
        }

        return r;
    }

    public static class ReturnStringAndError {

        private String string;
        private String error;

        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }

        public String getString() {
            return string;
        }

        public void setString(String string) {
            this.string = string;
        }

    }

    private static class MyHostnameVerifier implements HostnameVerifier {

        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }

    }

    private static class MyTrustManager implements X509TrustManager {

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    }

    public static String httpPost(String url, String params) {
        String content = null;
        ByteArrayOutputStream baoStream = null;
        DataOutputStream dos = null;
        try {
            URL urlObj = new URL(url);
            HttpURLConnection httpConn = (HttpURLConnection) urlObj.openConnection();
            httpConn.setDoInput(true);
            httpConn.setDoOutput(true);

            // 此方法在正式链接之前设置才有效。
            httpConn.setRequestMethod("POST");
            httpConn.setUseCaches(false);
            // 正式创建链接
            httpConn.connect();

            dos = new DataOutputStream(httpConn.getOutputStream());
            dos.write(params.getBytes());
            dos.flush();

            String encoding = httpConn.getContentEncoding();
            InputStream is = httpConn.getInputStream();
            int read = -1;
            baoStream = new ByteArrayOutputStream();
            while ((read = is.read()) != -1) {
                baoStream.write(read);
            }
            byte[] data = baoStream.toByteArray();

            //baoStream.close();
            //dos.close();
            if (encoding != null) {
                content = new String(data, encoding);
            } else {
                content = new String(data);
            }

        } catch (IOException e) {
            System.out.println("发送POST请求出现异常！" + e);
        } finally {
            try {
                if (dos != null) {
                    dos.close();
                }
                if (baoStream != null) {
                    baoStream.close();
                }
            } catch (IOException ex) {
                System.out.println("发送POST请求finally出现异常！" + ex);
            }
        }

        return content;
    }
}
