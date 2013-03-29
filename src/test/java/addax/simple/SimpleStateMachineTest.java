package addax.simple;

import addax.UnrecognizableInputException;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

import static addax.simple.SimpleAction.set;
import static addax.simple.SimpleAction.setAs;
import static addax.simple.SimpleState.newState;
import static addax.simple.SimpleTransition.*;

/**
 * @author Mamad
 * @version 1.0
 * @since 29/03/13
 */
public class SimpleStateMachineTest {
    //define states
    SimpleState start, nameSet, actionSet, timeSet;

    @BeforeMethod
    public void setUp() throws Exception {
        start = newState("start");
        nameSet = newState("name_set");
        actionSet = newState("action_set");
        timeSet = newState("time_set");

        //configure transitions
        start.skip(except("Pi"));
        start.move(anyOf("Pi").action(setAs("name")), nameSet);

        nameSet.skip(except("reboot", "restart"));
        nameSet.move(anyOf("reboot", "restart").action(set("action", "reboot")), actionSet);

        actionSet.skip(except("now"));
        actionSet.move(anyOf("now").action(set("when", "now")), timeSet);

        timeSet.skip(any());
    }

    @Test
    public void testStateMachine() {
        SimpleStateMachine sm = new SimpleStateMachine(start);
        try {
            sm.read("Hey", "Pi", "please", "restart", "now");
            Assert.assertEquals(sm.getStartState(), timeSet);
            Map<String, String> values = sm.getContext().getValues();
            Assert.assertNotNull(values);
            Assert.assertTrue(values.containsKey("name"));
            Assert.assertTrue(values.containsKey("action"));
            Assert.assertTrue(values.containsKey("when"));
            Assert.assertEquals(values.get("name"), "Pi");
            Assert.assertEquals(values.get("action"), "reboot");
            Assert.assertEquals(values.get("when"), "now");
        } catch (UnrecognizableInputException e) {
            Assert.fail("Error", e);
        }
    }

    @Test
    public void testSaveLoad() {
        SimpleStateMachine sm = new SimpleStateMachine(start);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            sm.save(baos);
            baos.close();
        } catch (IOException e) {
            Assert.fail();
        }

        Assert.assertTrue(baos.size() > 0);
        //load it
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        SimpleStateMachine loaded = new SimpleStateMachine(null);
        try {
            loaded.load(bais);
        } catch (IOException e) {
            Assert.fail();
        }

        Assert.assertNull(loaded.getStartState());
        Assert.assertEquals(loaded.getAllStates(), sm.getAllStates());
    }
}
