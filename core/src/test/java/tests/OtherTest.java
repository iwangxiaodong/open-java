package tests;

import com.openle.our.core.io.Serializer;
import com.openle.our.core.network.NetCommon;
import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.jupiter.api.*;

public class OtherTest {

    @Test
    public void testSerializer() throws IOException, ClassNotFoundException {
        String eString = toString();
        byte[] bytes = new Serializer().dumpToByteArray(eString);
        String obj = (String) new Serializer().load(bytes);
        Assertions.assertEquals(eString, obj);
    }

//    @Test
//    public void testTupleSerializer1() throws IOException, ClassNotFoundException {
//        Tuple3 t3 = new Tuple3(1, 2, 3);
//        byte[] bytes = new Serializer().dumpToByteArray(t3);
//        Tuple3 newT3 = (Tuple3) new Serializer().load(bytes);
//        Assertions.assertEquals(t3.v2, newT3.v2);
//    }
    @Test
    public void testNetCommon() throws SocketException, UnknownHostException {
        NetCommon.getNetworkInterfacesWithMAC().forEach(i -> {
            try {
                if (i.getHardwareAddress() != null) {
                    System.out.println(i + "|" + NetCommon.macBytesToString(i.getHardwareAddress()));

                }
            } catch (SocketException ex) {
                Logger.getLogger(OtherTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        System.out.println("testNetCommon ");

        System.out.println(NetCommon.firstLocalAddress());

        //        try {
        //
        //            networkInterface =NetworkInterface.getByName("eth7");
        //        } catch (SocketException e) {
        //            System.out.println(e.getMessage());
        //        }
        //
        //        if (networkInterface != null) {
        //            System.out.println(NetCommon.macBytesToString(networkInterface.getHardwareAddress()) + "|" + networkInterface.getInetAddresses().asIterator());
        //
        //            networkInterface.getInetAddresses().asIterator()
        //                    .forEachRemaining(i -> System.out.println("ip - " + i.getHostAddress()));
        //        }
    }

}
