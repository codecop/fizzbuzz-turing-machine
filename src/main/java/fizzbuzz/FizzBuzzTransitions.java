package fizzbuzz;

import universal.state.State;
import universal.state.TransitionTable;

public class FizzBuzzTransitions {

    public TransitionTable create() {
        TransitionTable table = new TransitionTable(CharSymbol::new);
        code(table);
        less(table);
        equal(table);
        inc(table);
        duplicate(table);
        return table;
    }

    void code(TransitionTable table) {
        table //
                .row(Q.Ip_Restart, "P", Q.Ip_SwitchRight, null, "R") // 

                // restart goes to the left until P
                .row(Q.Ip_Restart, "P", Q.Ip_SwitchRight, null, "R") // 
                .row(Q.Ip_Restart, null, null, null, "L")

                // h = halt
                .row(Q.Ip_SwitchRight, "h", Q.Ip_SwitchLeftHalt, "P", "L") // switch P<>instruction 
                .row(Q.Ip_SwitchLeftHalt, "P", Q.Halt, "h", null) // switch P<>instruction

                // d = dup
                .row(Q.Ip_SwitchRight, "d", Q.Ip_SwitchLeftDup, "P", "L") // switch P<>instruction
                .row(Q.Ip_SwitchLeftDup, "P", Q.Code_Dup, "d", null) // switch P<>instruction
                .row(Q.Code_Dup, "C", Q.Dup, "$", "R") // go until Cursor C
                .row(Q.Code_Dup, null, null, null, "R") // go until Cursor C
                .row(Q.Dup_Write$AndMove7Back, null, Q.Ip_Restart, "C", null) // overwrite end of Dup

                // i = inc
                .row(Q.Ip_SwitchRight, "i", Q.Ip_SwitchLeftInc, "P", "L") // switch P<>instruction 
                .row(Q.Ip_SwitchLeftInc, "P", Q.Code_Inc, "i", null) // switch P<>instruction 
                .row(Q.Code_Inc, "C", Q.Inc, "$", "R") // go until Cursor C
                .row(Q.Code_Inc, null, null, null, "R") // go until Cursor C
                .row(Q.Inc_DoneMoveRight, "$", Q.Ip_Restart, "C", null) // overwrite end of Inc

                // r = right
                .row(Q.Ip_SwitchRight, "r", Q.Ip_SwitchLeftRightMove, "P", "L") // switch P<>instruction
                .row(Q.Ip_SwitchLeftRightMove, "P", Q.Code_Right, "r", null) // switch P<>instruction
                .row(Q.Code_Right, "C", Q.Right_Move, "$", "R") // go until Cursor C
                .row(Q.Code_Right, null, null, null, "R") // go until Cursor C
                .row(Q.Right_Move, "$", Q.Ip_Restart, "C", null) //
                .row(Q.Right_Move, null, null, null, "R") //

                // l = left
                .row(Q.Ip_SwitchRight, "l", Q.Ip_SwitchLeftLeftMove, "P", "L") // switch P<>instruction
                .row(Q.Ip_SwitchLeftLeftMove, "P", Q.Code_Left, "l", null) // switch P<>instruction
                .row(Q.Code_Left, "C", Q.Left_Move, "$", "L") // go until Cursor C
                .row(Q.Code_Left, null, null, null, "R") // go until Cursor C
                .row(Q.Left_Move, "$", Q.Ip_Restart, "C", null) //
                .row(Q.Left_Move, null, null, null, "L") //

                // =...; = if equal then ;
                .row(Q.Ip_SwitchRight, "=", Q.Ip_SwitchLeftEqual, "P", "L") // switch P<>instruction
                .row(Q.Ip_SwitchLeftEqual, "P", Q.Code_Equal, "=", null) // switch P<>instruction
                .row(Q.Code_Equal, "C", Q.Equal, "$", "R") // go until Cursor C
                .row(Q.Code_Equal, null, null, null, "R") // go until Cursor C
                .row(Q.Equal, "$", Q.Ip_Restart, "C", null) // all OK
                .row(Q.Equal_Expect0, "1", Q.Equal_FailedLeft, null, null) // not equal
                .row(Q.Equal_Expect1, "0", Q.Equal_FailedLeft, null, null) // not equal
                .row(Q.Equal_FailedLeft, "$", Q.Ip_RestartSkip, "C", null) // find left marker, not stop in middle
                .row(Q.Equal_FailedLeft, null, null, null, "L") // find left marker, not stop in middle

                // equal success, process ;
                .row(Q.Ip_SwitchRight, ";", Q.Ip_SwitchLeftEqualEnd, "P", "L") // switch P<>instruction 
                .row(Q.Ip_SwitchLeftEqualEnd, "P", Q.Ip_Restart, ";", "R") // switch P<>instruction and skip ;

                // skip all until it reaches ;
                .row(Q.Ip_RestartSkip, "P", Q.Ip_Skip, null, "R") //
                .row(Q.Ip_RestartSkip, null, null, null, "L") //
                // hdirl=<;
                .row(Q.Ip_Skip, "h", Q.Ip_SkipLeftHalt, "P", "L") // switch P<>instruction in skip 
                .row(Q.Ip_SkipLeftHalt, "P", Q.Ip_RestartSkip, "h", "R") // switch P<>instruction in skip
                .row(Q.Ip_Skip, "d", Q.Ip_SkipLeftDup, "P", "L") // switch P<>instruction in skip
                .row(Q.Ip_SkipLeftDup, "P", Q.Ip_RestartSkip, "d", "R") // switch P<>instruction in skip
                .row(Q.Ip_Skip, "i", Q.Ip_SkipLeftInc, "P", "L") // switch P<>instruction in skip
                .row(Q.Ip_SkipLeftInc, "P", Q.Ip_RestartSkip, "i", "R") // switch P<>instruction in skip
                .row(Q.Ip_Skip, "r", Q.Ip_SkipLeftRightMove, "P", "L") // switch P<>instruction in skip
                .row(Q.Ip_SkipLeftRightMove, "P", Q.Ip_RestartSkip, "r", "R") // switch P<>instruction in skip
                .row(Q.Ip_Skip, "l", Q.Ip_SkipLeftLeftMove, "P", "L") // switch P<>instruction in skip
                .row(Q.Ip_SkipLeftLeftMove, "P", Q.Ip_RestartSkip, "l", "R") // switch P<>instruction in skip
                .row(Q.Ip_Skip, "=", Q.Ip_SkipLeftEqual, "P", "L") // switch P<>instruction in skip
                .row(Q.Ip_SkipLeftEqual, "P", Q.Ip_RestartSkip, "=", "R") // switch P<>instruction in skip
                .row(Q.Ip_Skip, "<", Q.Ip_SkipLeftLess, "P", "L") // switch P<>instruction in skip
                .row(Q.Ip_SkipLeftLess, "P", Q.Ip_RestartSkip, "<", "R") // switch P<>instruction in skip
                .row(Q.Ip_Skip, "g", Q.Ip_SkipLeftGoto, "P", "L") // switch P<>instruction in skip
                .row(Q.Ip_SkipLeftGoto, "P", Q.Ip_RestartSkip, "g", "R") // switch P<>instruction in skip
                .row(Q.Ip_Skip, ";", Q.Ip_SkipLeftEqualEnd, "P", "L") // switch P<>instruction in skip
                .row(Q.Ip_SkipLeftEqualEnd, "P", Q.Ip_Restart, ";", "R") // switch P<>instruction in skip

                // <...;
                .row(Q.Ip_SwitchRight, "<", Q.Ip_SwitchLeftLess, "P", "L") // switch P<>instruction
                .row(Q.Ip_SwitchLeftLess, "P", Q.Code_Less, "<", null) // switch P<>instruction
                .row(Q.Code_Less, "C", Q.Less, "$", "R") // go until Cursor C
                .row(Q.Code_Less, null, null, null, "R") // go until Cursor C
                .row(Q.Less, "$", Q.Less_FailedLeft, null, null) // all OK
                .row(Q.Less_Expect0, "1", Q.Less_SuccessLeft, null, null) // not equal
                .row(Q.Less_SuccessLeft, "$", Q.Ip_Restart, "C", null) // find left marker, not stop in middle
                .row(Q.Less_SuccessLeft, null, null, null, "L") // find left marker, not stop in middle
                .row(Q.Less_Expect1, "0", Q.Less_FailedLeft, null, null) // not equal
                .row(Q.Less_FailedLeft, "$", Q.Ip_RestartSkip, "C", null) // find left marker, not stop in middle
                .row(Q.Less_FailedLeft, null, null, null, "L") // find left marker, not stop in middle

                // Label, process L
                .row(Q.Ip_SwitchRight, "L", Q.Ip_SwitchLeftLabel, "P", "L") // switch P<>instruction 
                .row(Q.Ip_SwitchLeftLabel, "P", Q.Ip_Restart, "L", "R") // switch P<>instruction and skip ;

                // g = goto
                .row(Q.Ip_SwitchRight, "g", Q.Ip_Goto, null, "L") //
                .row(Q.Ip_Goto, "P", null, null, "L") //
                .row(Q.Ip_Goto, "h", Q.Ip_GotoRightHalt, "P", "R") // switch P<>instruction in Goto 
                .row(Q.Ip_GotoRightHalt, "P", Q.Ip_Goto, "h", "L") // switch P<>instruction in Goto
                .row(Q.Ip_Goto, "d", Q.Ip_GotoRightDup, "P", "R") // switch P<>instruction in Goto
                .row(Q.Ip_GotoRightDup, "P", Q.Ip_Goto, "d", "L") // switch P<>instruction in Goto
                .row(Q.Ip_Goto, "i", Q.Ip_GotoRightInc, "P", "R") // switch P<>instruction in Goto
                .row(Q.Ip_GotoRightInc, "P", Q.Ip_Goto, "i", "L") // switch P<>instruction in Goto
                .row(Q.Ip_Goto, "r", Q.Ip_GotoRightRightMove, "P", "R") // switch P<>instruction in Goto
                .row(Q.Ip_GotoRightRightMove, "P", Q.Ip_Goto, "r", "L") // switch P<>instruction in Goto
                .row(Q.Ip_Goto, "l", Q.Ip_GotoRightLeftMove, "P", "R") // switch P<>instruction in Goto
                .row(Q.Ip_GotoRightLeftMove, "P", Q.Ip_Goto, "l", "L") // switch P<>instruction in Goto
                .row(Q.Ip_Goto, "=", Q.Ip_GotoRightEqual, "P", "R") // switch P<>instruction in Goto
                .row(Q.Ip_GotoRightEqual, "P", Q.Ip_Goto, "=", "L") // switch P<>instruction in Goto
                .row(Q.Ip_Goto, "<", Q.Ip_GotoRightLess, "P", "R") // switch P<>instruction in Goto
                .row(Q.Ip_GotoRightLess, "P", Q.Ip_Goto, "<", "L") // switch P<>instruction in Goto
                .row(Q.Ip_Goto, ";", Q.Ip_GotoRightEqualEnd, "P", "R") // switch P<>instruction in Goto
                .row(Q.Ip_GotoRightEqualEnd, "P", Q.Ip_Goto, ";", "L") // switch P<>instruction in Goto

                .row(Q.Ip_Goto, "L", Q.Ip_Restart, null, "R");
    }

    /**
     * Less starts on left first (highest) digit of left argument and stops on the end of the first $.
     */
    void less(TransitionTable table) {
        table //
                .row(Q.Less, "0", Q.Less_MoveRight7And0, null, "R")
                .row(Q.Less_MoveRight7And0, null, Q.Less_MoveRight6And0, null, "R")
                .row(Q.Less_MoveRight6And0, null, Q.Less_MoveRight5And0, null, "R")
                .row(Q.Less_MoveRight5And0, null, Q.Less_MoveRight4And0, null, "R")
                .row(Q.Less_MoveRight4And0, null, Q.Less_MoveRight3And0, null, "R")
                .row(Q.Less_MoveRight3And0, null, Q.Less_MoveRight2And0, null, "R")
                .row(Q.Less_MoveRight2And0, null, Q.Less_MoveRight1And0, null, "R")
                .row(Q.Less_MoveRight1And0, null, Q.Less_Expect0, null, "R") //
                .row(Q.Less_Expect0, "0", Q.Less_MoveBack7, null, null) // OK

                .row(Q.Less, "1", Q.Less_MoveRight7And1, null, "R")
                .row(Q.Less_MoveRight7And1, null, Q.Less_MoveRight6And1, null, "R")
                .row(Q.Less_MoveRight6And1, null, Q.Less_MoveRight5And1, null, "R")
                .row(Q.Less_MoveRight5And1, null, Q.Less_MoveRight4And1, null, "R")
                .row(Q.Less_MoveRight4And1, null, Q.Less_MoveRight3And1, null, "R")
                .row(Q.Less_MoveRight3And1, null, Q.Less_MoveRight2And1, null, "R")
                .row(Q.Less_MoveRight2And1, null, Q.Less_MoveRight1And1, null, "R")
                .row(Q.Less_MoveRight1And1, null, Q.Less_Expect1, null, "R") //
                .row(Q.Less_Expect1, "1", Q.Less_MoveBack7, null, null) // OK

                .row(Q.Less_MoveBack7, null, Q.Less_MoveBack6, null, "L")
                .row(Q.Less_MoveBack6, null, Q.Less_MoveBack5, null, "L")
                .row(Q.Less_MoveBack5, null, Q.Less_MoveBack4, null, "L")
                .row(Q.Less_MoveBack4, null, Q.Less_MoveBack3, null, "L")
                .row(Q.Less_MoveBack3, null, Q.Less_MoveBack2, null, "L")
                .row(Q.Less_MoveBack2, null, Q.Less_MoveBack1, null, "L") //
                .row(Q.Less_MoveBack1, null, Q.Less, null, "L")

                .row(Q.Less, "$", Q.Halt, "C", null) // not OK
                .row(Q.Less_Expect0, "1", Q.Halt, "C", null) // OK - higher bit was set in other
                .row(Q.Less_Expect1, "0", Q.Halt, "C", null) // not OK

        // > would work the same
        ;
    }

    /**
     * Equal starts on left first digit of left argument and stops on the end of the first $.
     */
    void equal(TransitionTable table) {
        table //
                .row(Q.Equal, "0", Q.Equal_MoveRight7And0, null, "R")
                .row(Q.Equal_MoveRight7And0, null, Q.Equal_MoveRight6And0, null, "R")
                .row(Q.Equal_MoveRight6And0, null, Q.Equal_MoveRight5And0, null, "R")
                .row(Q.Equal_MoveRight5And0, null, Q.Equal_MoveRight4And0, null, "R")
                .row(Q.Equal_MoveRight4And0, null, Q.Equal_MoveRight3And0, null, "R")
                .row(Q.Equal_MoveRight3And0, null, Q.Equal_MoveRight2And0, null, "R")
                .row(Q.Equal_MoveRight2And0, null, Q.Equal_MoveRight1And0, null, "R")
                .row(Q.Equal_MoveRight1And0, null, Q.Equal_Expect0, null, "R") //
                .row(Q.Equal_Expect0, "0", Q.Equal_MoveBack7, null, null) // OK

                .row(Q.Equal, "1", Q.Equal_MoveRight7And1, null, "R")
                .row(Q.Equal_MoveRight7And1, null, Q.Equal_MoveRight6And1, null, "R")
                .row(Q.Equal_MoveRight6And1, null, Q.Equal_MoveRight5And1, null, "R")
                .row(Q.Equal_MoveRight5And1, null, Q.Equal_MoveRight4And1, null, "R")
                .row(Q.Equal_MoveRight4And1, null, Q.Equal_MoveRight3And1, null, "R")
                .row(Q.Equal_MoveRight3And1, null, Q.Equal_MoveRight2And1, null, "R")
                .row(Q.Equal_MoveRight2And1, null, Q.Equal_MoveRight1And1, null, "R")
                .row(Q.Equal_MoveRight1And1, null, Q.Equal_Expect1, null, "R") //
                .row(Q.Equal_Expect1, "1", Q.Equal_MoveBack7, null, null) // OK

                .row(Q.Equal_MoveBack7, null, Q.Equal_MoveBack6, null, "L")
                .row(Q.Equal_MoveBack6, null, Q.Equal_MoveBack5, null, "L")
                .row(Q.Equal_MoveBack5, null, Q.Equal_MoveBack4, null, "L")
                .row(Q.Equal_MoveBack4, null, Q.Equal_MoveBack3, null, "L")
                .row(Q.Equal_MoveBack3, null, Q.Equal_MoveBack2, null, "L")
                .row(Q.Equal_MoveBack2, null, Q.Equal_MoveBack1, null, "L")
                .row(Q.Equal_MoveBack1, null, Q.Equal, null, "L")

                .row(Q.Equal, "$", Q.Halt, "C", null) // all OK
                .row(Q.Equal_Expect0, "1", Q.Halt, "C", null) // not OK
                .row(Q.Equal_Expect1, "0", Q.Halt, "C", null); // not OK
    }

    /**
     * Inc starts on left first digit of argument and stops on the inc'ed $. <br />
     * The last digit must not overflow.
     */
    void inc(TransitionTable table) {
        table //
                // 1. go to right $, then work backwards
                .row(Q.Inc, null, Q.Inc_MoveRightAndInc, null, "R")
                .row(Q.Inc_MoveRightAndInc, "0", Q.Inc_MoveRightAndInc, null, "R")
                .row(Q.Inc_MoveRightAndInc, "1", Q.Inc_MoveRightAndInc, null, "R")
                .row(Q.Inc_MoveRightAndInc, "$", Q.Inc_IncToTheLeft, null, "L")
                .row(Q.Inc_IncToTheLeft, "0", Q.Inc_DoneMoveRight, "1", null) // finished
                .row(Q.Inc_IncToTheLeft, "1", Q.Inc_IncToTheLeft, "0", "L") // add and continue with overflow/carry

                // done, move right again
                .row(Q.Inc_DoneMoveRight, "0", Q.Inc_DoneMoveRight, null, "R")
                .row(Q.Inc_DoneMoveRight, "1", Q.Inc_DoneMoveRight, null, "R")
                .row(Q.Inc_DoneMoveRight, "$", Q.Halt, null, null)

        // decrement would work the same
        ;
    }

    /**
     * Duplicate starts on left first digit of argument and stops on the duplicated $.
     */
    void duplicate(TransitionTable table) {
        table //
                // duplicate the 0 eight to the right
                .row(Q.Dup, "0", Q.Dup_Move7RightAndWrite0, null, "R") // remember to write 0 and move right, need to move 7+1 right
                .row(Q.Dup_Move7RightAndWrite0, null, Q.Dup_Move6RightAndWrite0, null, "R")
                .row(Q.Dup_Move6RightAndWrite0, null, Q.Dup_Move5RightAndWrite0, null, "R")
                .row(Q.Dup_Move5RightAndWrite0, null, Q.Dup_Move4RightAndWrite0, null, "R")
                .row(Q.Dup_Move4RightAndWrite0, null, Q.Dup_Move3RightAndWrite0, null, "R")
                .row(Q.Dup_Move3RightAndWrite0, null, Q.Dup_Move2RightAndWrite0, null, "R")
                .row(Q.Dup_Move2RightAndWrite0, null, Q.Dup_Move1RightAndWrite0, null, "R")
                .row(Q.Dup_Move1RightAndWrite0, null, Q.Dup_Write0AndMove7Back, null, "R")
                .row(Q.Dup_Write0AndMove7Back, null, Q.Dup_Move7LeftAndStartAgain, "0", null)

                // duplicate the 1 eight to the right
                .row(Q.Dup, "1", Q.Dup_Move7RightAndWrite1, null, "R")
                .row(Q.Dup_Move7RightAndWrite1, null, Q.Dup_Move6RightAndWrite1, null, "R")
                .row(Q.Dup_Move6RightAndWrite1, null, Q.Dup_Move5RightAndWrite1, null, "R")
                .row(Q.Dup_Move5RightAndWrite1, null, Q.Dup_Move4RightAndWrite1, null, "R")
                .row(Q.Dup_Move4RightAndWrite1, null, Q.Dup_Move3RightAndWrite1, null, "R")
                .row(Q.Dup_Move3RightAndWrite1, null, Q.Dup_Move2RightAndWrite1, null, "R")
                .row(Q.Dup_Move2RightAndWrite1, null, Q.Dup_Move1RightAndWrite1, null, "R")
                .row(Q.Dup_Move1RightAndWrite1, null, Q.Dup_Write1AndMove7Back, null, "R")
                .row(Q.Dup_Write1AndMove7Back, null, Q.Dup_Move7LeftAndStartAgain, "1", null)

                // duplicate the $ eight to the right
                .row(Q.Dup, "$", Q.Dup_Move7RightAndWrite$, null, "R")
                .row(Q.Dup_Move7RightAndWrite$, null, Q.Dup_Move6RightAndWrite$, null, "R")
                .row(Q.Dup_Move6RightAndWrite$, null, Q.Dup_Move5RightAndWrite$, null, "R")
                .row(Q.Dup_Move5RightAndWrite$, null, Q.Dup_Move4RightAndWrite$, null, "R")
                .row(Q.Dup_Move4RightAndWrite$, null, Q.Dup_Move3RightAndWrite$, null, "R")
                .row(Q.Dup_Move3RightAndWrite$, null, Q.Dup_Move2RightAndWrite$, null, "R")
                .row(Q.Dup_Move2RightAndWrite$, null, Q.Dup_Move1RightAndWrite$, null, "R")
                .row(Q.Dup_Move1RightAndWrite$, null, Q.Dup_Write$AndMove7Back, null, "R")
                .row(Q.Dup_Write$AndMove7Back, null, Q.Halt, "$", null) // we are done with duplicating

                // move 7 to the left including the first after setting
                .row(Q.Dup_Move7LeftAndStartAgain, null, Q.Dup_Move6LeftAndStartAgain, null, "L")
                .row(Q.Dup_Move6LeftAndStartAgain, null, Q.Dup_Move5LeftAndStartAgain, null, "L")
                .row(Q.Dup_Move5LeftAndStartAgain, null, Q.Dup_Move4LeftAndStartAgain, null, "L")
                .row(Q.Dup_Move4LeftAndStartAgain, null, Q.Dup_Move3LeftAndStartAgain, null, "L")
                .row(Q.Dup_Move3LeftAndStartAgain, null, Q.Dup_Move2LeftAndStartAgain, null, "L")
                .row(Q.Dup_Move2LeftAndStartAgain, null, Q.Dup_Move1LeftAndStartAgain, null, "L")
                .row(Q.Dup_Move1LeftAndStartAgain, null, Q.Dup, null, "L");
    }
}

enum Q implements State {

    Halt,

    Dup, //
    Dup_Move7RightAndWrite0, Dup_Move6RightAndWrite0, Dup_Move5RightAndWrite0, Dup_Move4RightAndWrite0,
    Dup_Move3RightAndWrite0, Dup_Move2RightAndWrite0, Dup_Move1RightAndWrite0, Dup_Write0AndMove7Back,

    Dup_Move7RightAndWrite1, Dup_Move6RightAndWrite1, Dup_Move5RightAndWrite1, Dup_Move4RightAndWrite1,
    Dup_Move3RightAndWrite1, Dup_Move2RightAndWrite1, Dup_Move1RightAndWrite1, Dup_Write1AndMove7Back,

    Dup_Move7RightAndWrite$, Dup_Move6RightAndWrite$, Dup_Move5RightAndWrite$, Dup_Move4RightAndWrite$,
    Dup_Move3RightAndWrite$, Dup_Move2RightAndWrite$, Dup_Move1RightAndWrite$, Dup_Write$AndMove7Back,

    Dup_Move7LeftAndStartAgain, Dup_Move6LeftAndStartAgain, Dup_Move5LeftAndStartAgain, Dup_Move4LeftAndStartAgain,
    Dup_Move3LeftAndStartAgain, Dup_Move2LeftAndStartAgain, Dup_Move1LeftAndStartAgain,

    Inc, // 
    Inc_MoveRightAndInc, Inc_IncToTheLeft, Inc_DoneMoveRight,

    Code_Dup, Code_Inc, Code_Right, Right_Move, Code_Left, Left_Move, Code_Equal, Code_Less,

    Equal, //
    Equal_MoveRight7And0, Equal_MoveRight6And0, Equal_MoveRight5And0, Equal_MoveRight4And0, Equal_MoveRight3And0,
    Equal_MoveRight2And0, Equal_MoveRight1And0, Equal_Expect0,

    Equal_MoveRight7And1, Equal_MoveRight6And1, Equal_MoveRight5And1, Equal_MoveRight4And1, Equal_MoveRight3And1,
    Equal_MoveRight2And1, Equal_MoveRight1And1, Equal_Expect1,

    Equal_MoveBack7, Equal_MoveBack6, Equal_MoveBack5, Equal_MoveBack4, Equal_MoveBack3, Equal_MoveBack2,
    Equal_MoveBack1, Equal_FailedLeft,

    Ip_Restart, //
    Ip_SwitchRight, Ip_SwitchLeftHalt, Ip_SwitchLeftDup, Ip_SwitchLeftInc, Ip_SwitchLeftRightMove,
    Ip_SwitchLeftLeftMove, Ip_SwitchLeftEqual, Ip_SwitchLeftEqualEnd, Ip_SwitchLeftLess,

    Ip_RestartSkip, // 
    Ip_Skip, Ip_SkipLeftHalt, Ip_SkipLeftDup, Ip_SkipLeftInc, Ip_SkipLeftRightMove, Ip_SkipLeftLeftMove,
    Ip_SkipLeftEqual, Ip_SkipLeftEqualEnd, Ip_SkipLeftLess, Ip_SkipLeftGoto,

    Less, // 
    Less_MoveRight7And0, Less_MoveRight6And0, Less_MoveRight5And0, Less_MoveRight4And0, Less_MoveRight3And0,
    Less_MoveRight2And0, Less_MoveRight1And0, Less_Expect0,

    Less_MoveRight7And1, Less_MoveRight6And1, Less_MoveRight5And1, Less_MoveRight4And1, Less_MoveRight3And1,
    Less_MoveRight2And1, Less_MoveRight1And1, Less_Expect1,

    Less_MoveBack7, Less_MoveBack6, Less_MoveBack5, Less_MoveBack4, Less_MoveBack3, Less_MoveBack2, Less_MoveBack1,
    Less_FailedLeft, Less_SuccessLeft,

    //
    Ip_SwitchLeftLabel, Ip_Goto, Ip_GotoRightHalt, Ip_GotoRightDup, Ip_GotoRightInc, Ip_GotoRightRightMove,
    Ip_GotoRightLeftMove, Ip_GotoRightEqual, Ip_GotoRightLess, Ip_GotoRightEqualEnd,

    ;

    @Override
    public boolean isTerminal() {
        return this == Halt;
    }
}
