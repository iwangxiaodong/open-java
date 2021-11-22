package tests;

import com.openle.our.core.os.SystemShell;
import org.junit.jupiter.api.Test;

/**
 *
 * @author xiaodong
 */
public class ShellTest {

    @Test
    public void shellTest() {
        String r = SystemShell.executeReturn(new String[]{"cmd","/C","echo","111"});
        System.out.println(r);
        
        r=SystemShell.executeReturn("echo 222");
        System.out.println(r);
    }

}
