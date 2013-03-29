package addax.primitive.string;

import addax.UnrecognizableInputException;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Map;

import static addax.primitive.string.StringAction.set;
import static addax.primitive.string.StringAction.setAs;
import static addax.primitive.string.StringState.newState;
import static addax.primitive.string.StringTransition.*;

/**
 * @author Mamad
 * @version 1.0
 * @since 29/03/13
 */
public class StringStateMachineTest {
    @Test
    public void testStateMachine() throws Exception {
        //define states
        StringState start = newState("start"),
                nameSet = newState("name_set"),
                actionSet = newState("action_set"),
                timeSet = newState("time_set");

        //configure transitions
        start.skip(except("Pi"));
        start.move(anyOf("Pi").action(setAs("name")), nameSet);

        nameSet.skip(except("reboot", "restart"));
        nameSet.move(anyOf("reboot", "restart").action(set("action", "reboot")), actionSet);

        actionSet.skip(except("now"));
        actionSet.move(anyOf("now").action(set("when", "now")), timeSet);

        timeSet.skip(any());

        StringStateMachine sm = new StringStateMachine(start);
        try {
            sm.read("Hey", "Pi", "please", "restart", "now");
            Assert.assertEquals(sm.getState(), timeSet);
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
}
