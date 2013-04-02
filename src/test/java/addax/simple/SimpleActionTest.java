package addax.simple;

import addax.Context;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author Mamad
 * @version 1.0
 * @since 02/04/13
 */
public class SimpleActionTest {
    @Test
    public void testPopAll() throws Exception {

        SimpleAction popAll = SimpleAction.popAll("popAll");
        Context<String> context = new SimpleStateMachine(null);
        context.push("word1");
        context.push("word2");
        context.push("word3 ");
        popAll.execute("token", context);

        Assert.assertNotNull(context.getValues());
        Assert.assertTrue(context.getValues().containsKey("popAll"));
        Assert.assertEquals(context.getValues().get("popAll"), "word1 word2 word3 ");

        context.push(" word 2 ");
        popAll.execute("token", context);
        Assert.assertEquals(context.getValues().get("popAll"), " word 2 ");
        //nothing
        popAll.execute("token", context);
        Assert.assertEquals(context.getValues().get("popAll"), "");

    }
}
