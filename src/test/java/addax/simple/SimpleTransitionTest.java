package addax.simple;

import addax.Context;
import junit.framework.Assert;
import org.mockito.Mockito;
import org.testng.annotations.Test;

/**
 * @author Mamad
 * @version 1.0
 * @since 31/03/13
 */
public class SimpleTransitionTest {
    @Test
    public void testExcept() throws Exception {
        SimpleTransition tr = SimpleTransition.except("(number|contact|detail)");
        Context<String> context = Mockito.mock(Context.class);
        Assert.assertTrue(tr.accept("david", context));
        Assert.assertTrue(tr.accept("alex", context));
        Assert.assertTrue(tr.accept("send", context));
        Assert.assertFalse(tr.accept("number", context));
        Assert.assertFalse(tr.accept("contact", context));
        Assert.assertFalse(tr.accept("detail", context));

    }
}
