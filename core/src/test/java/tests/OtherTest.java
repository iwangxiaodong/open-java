package tests;

import com.openle.module.core.lambda.*;
import java.util.function.Function;
import org.junit.jupiter.api.*;

public class OtherTest {

    @Test
    public void testLambdaGetter() {
        Function f = LambdaFactory.newSerializedMethodReferences("fieldName");
        String s = LambdaFactory.getMethodReferencesName(f);
        //System.out.println(s);
        Assertions.assertEquals(s, "fieldName");
    }
}
