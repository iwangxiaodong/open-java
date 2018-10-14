package tests;

import com.openle.our.core.lambda.LambdaFactory;
import com.openle.our.core.network.NetCommon;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.jupiter.api.*;

public class OtherTest {

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

        NetworkInterface ni = null;
        ni = NetCommon.getEthernetNetworkInterface();
        if (ni != null) {
            System.out.println(ni.getName());
            System.out.println(NetCommon.getHostAddress(ni));
        }

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

    @Test
    //@Disabled
    public void testLambdaGetter() {
        Function f = LambdaFactory.newSerializedMethodReferences("fieldName");
        String s = LambdaFactory.getMethodReferencesName(f);
        System.out.println(s);
        Assertions.assertEquals(s, "fieldName");
    }
}
