package fizzbuzz;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import universal.TuringMachine;
import universal.state.StringTransitionTable;
import universal.state.TransitionLookup;
import universal.tape.Tape;

/*
 * How would FizzBuzz be on the Tape = kind of stack?
 * We start with 100$. And we want to end with 1$2$Fizz$4$Buzz$  
 * 1. step: 100$ -> 1$2$3$...
 *    for (int i=1;i<100;i++)
 *    S100$ -> 1$L$100$ -> 1$2$L$100
 *    go left to S
 *    insert 1$
 *    while prev < next do
 *      insert prev+1
 * 2. step: replace 3's -> Fizz
 * 3. step: replace 5's -> Buzz
 * 
 * Maybe minimal 1st step = inc, for numbers 1-100 = 7 bit.
 * 
 * start with 0000001 and do DUP INC (back left) DUP INC etc. until 100
 * 1 100: dup left left dup left inc compare? repeat
 */
class FizzBuzzTest {

    Tape<CharSymbol> tape;
    FizzBuzzTransitions transitions = new FizzBuzzTransitions();
    StringTransitionTable table = new StringTransitionTable(CharSymbol::new);
    TuringMachine<CharSymbol> machine;

    @Nested
    class Command {

        @Test
        void duplicate() {
            transitions.addDuplicate(table);
            createMachineWith("0000000$", Q.Dup, table);
            machine.loop();
            assertTapeEquals("0000000$0000000$");
        }

        @Test
        void incZero() {
            transitions.addInc(table);
            createMachineWith("0000000$", Q.Inc, table);
            machine.loop();
            assertTapeEquals("0000001$");
        }

        @Test
        void incOne() {
            transitions.addInc(table);
            createMachineWith("0000001$", Q.Inc, table);
            machine.loop();
            assertTapeEquals("0000010$");
        }

        @Test
        void incToMax() {
            transitions.addInc(table);
            createMachineWith("0111111$", Q.Inc, table);
            machine.loop();
            assertTapeEquals("1000000$");
        }

        @Test
        void equal() {
            transitions.addEqual(table);
            createMachineWith("0101101$0101101$", Q.Equal, table);
            machine.loop();
            assertTapeEquals("0101101C0101101$");
        }

    }

    @Nested
    class InstructionPointer {

        @Test
        void halt() {
            createMachineWith("Ph", Q.Ip_Restart, transitions.create());
            machine.loop();
            assertTapeEquals("hP");
        }

        @Test
        void inc() {
            createMachineWith("PihC0101010$", Q.Ip_Restart, transitions.create());
            machine.loop();
            assertTapeEquals("ihP$0101011C");
        }

        @Test
        void dupWithCursorMovement() {
            createMachineWith("PdllrhC0101010$", Q.Ip_Restart, transitions.create());
            machine.loop();
            assertTapeEquals("dllrhP$0101010C0101010$");
        }

        @Test
        void equalThenMultipleStatements() {
            createMachineWith("P=ilili;hC0000000$0000000$", Q.Ip_Restart, transitions.create());
            machine.loop();
            assertTapeEquals("=ilili;hP$0000000$0000011C");
        }

        @Test
        void notEqual() {
            createMachineWith("P=i;hC0101101$0101001$", Q.Ip_Restart, transitions.create());
            machine.loop();
            assertTapeEquals("=i;hP$0101101C0101001$");
        }

        @Test
        void lessFalseOnSameInput() {
            createMachineWith("P<i;hC0101101$0101101$", Q.Ip_Restart, transitions.create());
            machine.loop();
            assertTapeEquals("<i;hP$0101101C0101101$");
        }

        @Test
        void lessSuccessOnOneLargerInput() {
            createMachineWith("P<i;hC0101101$0101110$", Q.Ip_Restart, transitions.create());
            machine.loop();
            assertTapeEquals("<i;hP$0101101$0101111C");
        }

        @Test
        void gotoLabel() {
            createMachineWith("PLil=lg;hC0000000$0000001$", Q.Ip_Restart, transitions.create());
            machine.loop();
            assertTapeEquals("Lil=lg;hP$0000010C0000001$");
        }
    }

    @Nested
    class FizzBuzzCode {

        @Test
        void createNumbersOneTo16() {
            // int limit = 16;
            createMachineWith("P" + // 
                    "Lrdllldlil<lg;" + // loop
                    "i" + // fix last element
                    "llllllllllllllll" + // go back all = "l"*limit
                    // "rrFrrFrrFrrFrrFr" + // 
                    // "llllllllllllllll" + // 
                    // "rrrrBrrrrBrrrrBr" + //
                    // "llllllllllllllll" + // 
                    "hC0000001$0001111$", // from - to starting data = 1 (limit-1).toBinary
                    Q.Ip_Restart, transitions.create());
            machine.loop();
            assertHumanTapeEquals("C1$2$3$4$5$6$7$8$9$10$11$12$13$14$15$16$");
        }
    }

    private void createMachineWith(String initialTape, Q initial, TransitionLookup transitionRules) {
        tape = new Tape<>(CharSymbol.fromString(initialTape), CharSymbol.EMPTY);
        machine = new TuringMachine<>(tape, transitionRules, initial);
    }

    private void assertTapeEquals(String expectedTape) {
        String result = CharSymbol.toString(tape.getCells());
        assertEquals(expectedTape, result);
    }

    private void assertHumanTapeEquals(String expected) {
        String tabe = CharSymbol.toString(tape.getCells());
        String bitNumbers = tabe.replaceAll("^.*P", "");
        String numbers = replaceBinaryNumbers(bitNumbers);
        assertEquals(expected, numbers);
    }

    private String replaceBinaryNumbers(String bitNumbers) {
        StringBuilder numbers = new StringBuilder();

        Matcher binaryMatcher = Pattern.compile("[0-1]{7}").matcher(bitNumbers);
        while (binaryMatcher.find()) {
            int number = Integer.parseInt(binaryMatcher.group(), 2);
            binaryMatcher.appendReplacement(numbers, Integer.toString(number));
        }
        binaryMatcher.appendTail(numbers);

        return numbers.toString();
    }

}
