package fizzbuzz;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Known Symbols: "P<code>hC<bit number>$<bit number>$"
 * <ul>
 * <li>0,1 ... bits of 7 bit numbers
 * <li>$ ... number record separator
 * <li>C ... Cursor (standing on top of a separator) to continue there. Needs to be after Code.
 * <li>P ... Instruction pointer for executed code. Needs to be at beginning of code.
 * <li>h ... program code to halt
 * <li>d, i, r, l ... program code to inc, dup, move right and move left
 * <li>=???; ... program code to execute code on cell equality, else skip to after ;
 * <li>&lt;???; ... program code to execute code on cell less, else skip to after ;
 * <li>L ??? g ... goto jump backwards to nearest label "L" 
 * </ul>
 */
public class CharSymbol implements Symbol {

    public static final CharSymbol SEP = new CharSymbol('$');
    public static final CharSymbol EMPTY = new CharSymbol(' ');

    public final char symbol;

    private CharSymbol(int symbol) {
        this((char) symbol);
    }

    public CharSymbol(char symbol) {
        this.symbol = symbol;
    }

    public CharSymbol(String symbol) {
        this(symbol.charAt(0));
    }

    public char getSymbol() {
        return symbol;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof CharSymbol)) {
            return false;
        }
        CharSymbol that = (CharSymbol) other;
        return this.symbol == that.symbol;
    }

    @Override
    public int hashCode() {
        return symbol;
    }

    @Override
    public String toString() {
        return Character.toString(symbol);
    }

    public static List<CharSymbol> fromString(String characters) {
        return characters.chars(). //
                mapToObj(CharSymbol::new). //
                collect(Collectors.toList());
    }

    public static String toString(List<CharSymbol> symbols) {
        return symbols.stream(). //
                map(CharSymbol::toString). // 
                collect(Collectors.joining());
    }
}
