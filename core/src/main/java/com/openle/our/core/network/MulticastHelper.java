package com.openle.our.core.network;

import java.io.Closeable;
import java.io.IOException;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

// from - https://github.com/cubesky/MulticastHelper
public class MulticastHelper implements Closeable {

    public interface MulticastCallback {
        /**
         * Multicast Callback
         *
         * @param address multicast source host
         * @param port    multicast source port
         * @param array   Arrived Multicast Data
         * @param dp    ...
         */
        void received(String address, int port, byte[] array, DatagramPacket dp);

        //void receivedString(String address, int port, String s);
    }

    private final String multicastHost;
    private final int multicastPort;
    private InetAddress receiveInetAddress;
    private MulticastSocket receiveMulticastSocket;
    private MulticastCallback callback;
    private final ExecutorService service = Executors.newFixedThreadPool(1);
    private Future future;

    /**
     * Generate a multicast helper
     *
     * @param multicastHost Multicast Host, Must between 224.0.0.0 to 239.255.255.255
     * @param multicastPort Multicast Port
     */
    public MulticastHelper(String multicastHost, int multicastPort) {
        this.multicastHost = multicastHost;
        this.multicastPort = multicastPort;
    }

    /**
     * Send Multicast without new a MulticastHelper Object
     *
     * @param multicastHost Multicast Host, Must between 224.0.0.0 to 239.255.255.255
     * @param multicastPort Multicast Port
     * @param multicastData Multicast Data you want to send
     * @throws IOException Exception on IO
     */
    public static void sendMulticast(String multicastHost, int multicastPort, byte[] multicastData) throws IOException {
        InetAddress group = InetAddress.getByName(multicastHost);
        try (MulticastSocket socket = new MulticastSocket()) {
            socket.send(new DatagramPacket(multicastData, multicastData.length, group, multicastPort));
        }
    }

    /**
     * Send Multicast use MulticastHelper object
     *
     * @param multicastData Multicast Data you want to send
     * @throws IOException Exception on IO
     */
    public void sendMulticast(byte[] multicastData) throws IOException {
        MulticastHelper.sendMulticast(this.multicastHost, this.multicastPort, multicastData);
    }

    /**
     * Set multicast receive callback
     *
     * @param callback Multicast Callback
     */
    public void setCallback(MulticastCallback callback) {
        this.callback = callback;
    }

    /**
     * Start to receive. Only one Receiver can active
     *
     * @throws IOException Exception on IO
     */
    public void receiveMulticast() throws IOException {
        if (future != null) {
            future.cancel(true);
            future = null;
            close();
        }
        receiveInetAddress = InetAddress.getByName(multicastHost);
        receiveMulticastSocket = new MulticastSocket(multicastPort);
        receiveMulticastSocket.joinGroup(receiveInetAddress);
        future = service.submit(() -> {
            byte[] buf = new byte[2048];
            while (true) {
                DatagramPacket msgPacket = new DatagramPacket(buf, buf.length);
                receiveMulticastSocket.receive(msgPacket);
                //System.err.println("msg - " + new String(buf, 0, msgPacket.getLength()));
                if (callback != null)
                    callback.received(msgPacket.getAddress().getHostAddress(), msgPacket.getPort(), msgPacket.getData(), msgPacket);
            }
        });
    }

    /**
     * Start to receive on specified interface. Only one Receiver can active
     *
     * @param networkInterface specified interface
     * @throws IOException Exception on IO
     */
    public void receiveMulticast(NetworkInterface networkInterface) throws IOException {
        if (future != null) {
            future.cancel(true);
            future = null;
            close();
        }
        receiveInetAddress = InetAddress.getByName(multicastHost);
        receiveMulticastSocket = new MulticastSocket(multicastPort);
        receiveMulticastSocket.joinGroup(new InetSocketAddress(receiveInetAddress, multicastPort), networkInterface);
        future = service.submit(() -> {
            byte[] buf = new byte[10240];
            while (true) {
                DatagramPacket msgPacket = new DatagramPacket(buf, buf.length);
                receiveMulticastSocket.receive(msgPacket);
                if (callback != null)
                    callback.received(msgPacket.getAddress().getHostAddress(), msgPacket.getPort(), msgPacket.getData(),msgPacket);
            }
        });
    }

    /**
     * Close all.
     * Also support try..resource
     *
     * @throws IOException Exception on IO
     */
    @Override
    public void close() throws IOException {
        if (receiveMulticastSocket != null && receiveInetAddress != null) {
            receiveMulticastSocket.leaveGroup(receiveInetAddress);
        }
    }
}