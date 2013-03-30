package addax.simple;

import com.google.common.base.Charsets;
import com.google.common.base.Function;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.io.Files;

import java.io.*;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Mamad
 * @version 1.0
 * @since 30/03/13
 */
public class StateMachineFileParser {
    private Function<String, SimpleAction> stringToActionFunction;

    public StateMachineFileParser(Function<String, SimpleAction> stringToActionFunction) {
        this.stringToActionFunction = stringToActionFunction;
    }


    /**
     * <p>Utility method to covert input sentences into column style.</p>
     * a b c  will be converted to:
     * <ul>
     * <li>a</li>
     * <li>b</li>
     * <li>c</li>
     * </ul>
     *
     * @param sentences input sentences
     * @param out       output stream
     * @throws IOException on any io error
     */
    public static void convertLinesToColumn(List<String> sentences, Tokeniser tokeniser, OutputStream out) throws IOException {
        BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(out, Charsets.UTF_8));
        wr.write("#input data in column, first is input token, second is associated action and last is the state");
        wr.newLine();
        for (String line : sentences) {
            if (!Strings.isNullOrEmpty(line) && !line.startsWith("#")) {
                wr.write('#');
                wr.write(line);
                wr.newLine();
                for (String token : tokeniser.tokenise(line)) {
                    wr.write(token);
                    wr.newLine();
                }
            }
        }
    }

    /**
     * Parse the input file and generate a new state machine.
     * <p>
     * Sample input file:
     * <code>
     * <br/>
     * #pi, restart after 10 secs
     * <br/>
     * !pi     skip                start
     * <br/>
     * pi      setAs=device        dev-set
     * <br/>
     * restart action=reboot       action-set
     * <br/>
     * after   skip                object-p1
     * <br/>
     * 10    push                  object-p1
     * <br/>
     * secs  pop-prep=object       object-set
     * <br/>
     * # $end is sign of end of sequence
     * <br/>
     * $end
     * </code>
     * </p>
     *
     * @param inputFile input file
     * @return generated state machine
     * @throws IOException
     */
    public SimpleStateMachine parse(File inputFile) throws IOException {
        List<String> sentences = Files.readLines(inputFile, Charsets.UTF_8);

        Splitter splitter = Splitter.on(" ").trimResults().omitEmptyStrings();

        //need to know the order of states has been added
        Map<String, SimpleState> stateMap = Maps.newLinkedHashMap();
        Map<SimpleState, Map<String, Object[]>> stateTrMap = Maps.newHashMap();

        //last sentence state is used for backing to start-state when building state machine later
        SimpleState prevState = null, startState = null;
        for (String line : sentences) {
            if (!Strings.isNullOrEmpty(line) && !line.startsWith("#")) {
                if (line.equalsIgnoreCase("$end")) {
                    //end of sentence sequence, reset the state
                    prevState = startState;
                    continue;
                }

                Iterator<String> tokens = splitter.split(line).iterator();
                String word = tokens.next();
                String action = tokens.next();
                String st = tokens.next();

                SimpleState state = stateMap.get(st);
                if (state == null) {
                    state = new SimpleState(st);
                    stateMap.put(st, state);
                }

                if (startState == null) {
                    prevState = startState = state;//only once, the first state consider as start state, whatever it called
                }

                Map<String, Object[]> trMap = stateTrMap.get(state);
                if (trMap == null) {
                    trMap = Maps.newHashMap();
                    stateTrMap.put(state, trMap);
                }

                Object[] pair;
                if (word.startsWith("!")) {
                    pair = new Object[2];
                    pair[0] = SimpleTransition.except(word.substring(1)).action(stringToActionFunction.apply(action));
                    Set<String> words = Sets.newHashSet(word);
                    pair[1] = words;
                    trMap.put(action, pair);
                } else {
                    //find the transition with the same action in the current state
                    pair = trMap.get(action);
                    if (pair == null) {
                        pair = new Object[2];
                        pair[0] = SimpleTransition.anyOf(word).action(stringToActionFunction.apply(action));
                        Set<String> words = Sets.newHashSet(word);
                        pair[1] = words;
                        trMap.put(action, pair);
                    } else {
                        //update existing transition
                        Set<String> words = (Set<String>) pair[1];
                        if (!words.contains(word)) {
                            words.add(word);
                            //remove existing transition from the state
                            prevState.remove((SimpleTransition) pair[0]);
                            pair[0] = SimpleTransition.anyOf(words).action(stringToActionFunction.apply(action));
                        }
                    }
                }
                //update the state transition
                prevState.move((SimpleTransition) pair[0], state);
                prevState = state;
            }
        }
        return new SimpleStateMachine(startState);
    }
}
