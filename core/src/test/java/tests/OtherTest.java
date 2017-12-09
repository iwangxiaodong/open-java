package tests;

import com.openle.module.core.lambda.LambdaFactory;
import java.util.function.Function;
import org.junit.Test;

public class OtherTest {

    @Test
    public void testLambdaGetter() {
        Function f = LambdaFactory.newSerializedMethodReferences("fieldName");
        System.out.println(f);
        //assertEquals("fieldName", new Utils().getSelectName(LambdaFactory.class, f));
    }
}
