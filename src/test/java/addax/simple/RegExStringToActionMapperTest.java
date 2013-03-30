package addax.simple;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author Mamad
 * @version 1.0
 * @since 30/03/13
 */
public class RegExStringToActionMapperTest {
    @Test
    public void testRegExPatterns() throws Exception {
        RegExStringToActionMapper mapper = new RegExStringToActionMapper();
        SimpleAction set = SimpleAction.set("action", "sms");
        mapper.register("action=sms", set);
        mapper.register("action=send", null);
        mapper.register("action", null);
        mapper.register("act", null);
        mapper.register("action=", null);

        SimpleAction mapped = mapper.apply("action=sms");
        Assert.assertNotNull(mapped);
        Assert.assertEquals(mapped, set);

        mapper = new RegExStringToActionMapper();
        mapper.register("((\\bactivity)|(\\baction))=sms", set);
        mapped = mapper.apply("action=sms");
        Assert.assertNotNull(mapped);
        Assert.assertEquals(mapped, set);
        mapped = mapper.apply("activity=sms");
        Assert.assertNotNull(mapped);
        Assert.assertEquals(mapped, set);

    }
}
