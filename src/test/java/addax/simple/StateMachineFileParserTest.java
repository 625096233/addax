package addax.simple;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.util.Map;

/**
 * @author Mamad
 * @version 1.0
 * @since 30/03/13
 */
public class StateMachineFileParserTest {
    @Test
    public void testParse() throws Exception {
        StringToActionMapper mapper = new StringToActionMapper();
        mapper.register("skip", null);
        mapper.register("setAs=name", SimpleAction.setAs("name"));
        mapper.register("action=reboot", SimpleAction.set("action", "reboot"));
        mapper.register("push", SimpleAction.push());
        mapper.register("pop-prep=when", SimpleAction.popAndPrepend("when"));
        StateMachineFileParser parser = new StateMachineFileParser(mapper);
        File file = new File("src/test/data/test-parse-state-machine1.txt");
        SimpleStateMachine sm = parser.parse(file);
        Assert.assertNotNull(sm);
        Assert.assertNotNull(sm.getStartState());
        Assert.assertEquals(sm.getStartState().toString(), "start");

        sm.read(Tokeniser.DEFAULT.tokenise("pi, restart after 10 secs"));
        Map<String, String> values = sm.getContext().getValues();
        Assert.assertNotNull(values);
        Assert.assertTrue(values.containsKey("name"));
        Assert.assertTrue(values.containsKey("action"));
        Assert.assertTrue(values.containsKey("when"));
        Assert.assertEquals(values.get("name"), "pi");
        Assert.assertEquals(values.get("action"), "reboot");
        Assert.assertEquals(values.get("when"), "10 secs");

    }
}
