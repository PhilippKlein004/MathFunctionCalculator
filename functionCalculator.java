import java.util.Arrays;

/**
 * @author Philipp Klein
 * @version 1.0.3
 * @since 10.05.2022
 */

// NOTE: This program is not designed to be efficient in any way!

public class functionCalculator {

    public static void main(String[] args) {
        new functionCalculatorGUI();
    }

    // Notes for regex input :

    // 1. Ignore all whitespaces : \\s+
    // 2. Replace brackets : \\[ \\] \\( \\)

    /**
     * That is the 'only' variable used in the entire program.
     */

    private String[] function;
    private String[] function_save;

    // function_save is necessary for efficiency and gets overwritten in every new use.

    public functionCalculator(String function) {
        putFunctionIntoArray(function);
    }

    /**
     * This method gets a math function f(x) via a String.
     * By using the String split method, the function will be
     * split up into small parts that can be easily interpreted and
     * calculated.
     *
     * @param f function f(x) as String.
     */

    private void putFunctionIntoArray(String f) {
        f = prepareString(f);
        function_save = f.split("\\s+"); // "\\s+" means, that one or more whitespaces, will be ignored.
        if ( function_save[0].equals("-") ) eliminateMinusInFront();
    }

    /**
     * The function calculator splits the String at the whitespaces. This method
     * guarantees, that every operator and number are evenly spaced.
     *
     * @param s
     */

    private String prepareString(String s) {
        String tmp = "";
        for( int i = 0 ; i < s.length() ;++i ) {
            if( Character.isDigit( s.charAt(i)) ) {
                tmp += "" + s.charAt(i);
            } else if( !Character.isWhitespace(s.charAt(i)) ) {
                if ( 0 == i ) {
                    tmp += "" + s.charAt(i) + " ";
                } else {
                    tmp += " " + s.charAt(i) + " ";
                }
            }
        }
        return tmp;
    }

    /**
     * If the function e.g. looks like this: -2*x, in the array it'll look like
     * this: [-][2][*][x]. This Method replaces the '-' through '-1' this ensures,
     * that even brackets will be calculated correctly with '-' in front.
     *
     */

    private void eliminateMinusInFront() {
        String[] tmp = new String[function_save.length + 1];
        tmp[0] = "-1";
        tmp[1] = "*";
        for ( int i = 1 ; i < function_save.length ; ++i ) {
            tmp[i+1] = function_save[i];
        }
        function_save = tmp;
    }

    /**
     * This method puts the x values in the array.
     *
     * @param x
     */

    private void putXIntoArray(double x) {
        function = function_save.clone();
        for( int i = 0 ; i < function.length ; ++i ) {
            if( function[i].equals("x") ) {
                function[i] = "" + x;
            }
        }
    }

    /**
     * This method shortens the function array. It is achieved by converting the array
     * into a String again and, by the split method converting it back, eliminating all spaces.
     */

    private void shortenFunctionArray() {
        String s = Arrays.toString(function);
        s = s.replaceAll("\\[","").replaceAll("\\]","").replaceAll(",","");
        while ( s.charAt(0) == ' ' ) {
            s = s.substring(1);
        }
        function = s.split("\\s+");
    }

    /**
     * This method gives the index of a searched character in the function array. The index is in
     * between the start and end index. If no match is found -1 will be returned.
     *
     * @param start where to start the search.
     * @param end where to stop the search.
     * @param c searched operator or bracket.
     * @return result
     */

    private byte searchFor(byte start, byte end, char c) {
        for( byte i = start ; i < end ; ++i ) {
            if( function[i].equals("" + c) ) {
                return i;
            }
        }
        return -1;
    }

    /**
     * If a pattern is detected like : (24), the brackets can and need to be removed.
     */

    private void eliminateBracketsIfPossible() {
        byte i = searchFor((byte) 0, (byte) function.length,'(');
        if( function[i + 2].equals(")") ) {
            function[i] = " ";
            function[i + 2] = " ";
            shortenFunctionArray();
        }
    }

    /**
     * While the index for the brackets != -1, it can be assumed that brackets
     * still exist. The method will calculate the result of the brackets and respect
     * the math rule : * -> / -> + -> - !
     * NOTE:
     * It is necessary that during every calculation, the index of the brackets (input for start and end)
     * is newly calculated due to the elimination and movement in the array e.g., by the method
     * 'shortenFunctionArray()'.
     */

    private void calculateBrackets() {
        while( searchFor((byte) 0, (byte) function.length,'(') != -1 ) {
            calculateTheOperatorInArea((searchFor((byte) 0, (byte) function.length,'(')),searchFor((byte) 0, (byte) function.length,')'),'^');
            calculateTheOperatorInArea((searchFor((byte) 0, (byte) function.length,'(')),searchFor((byte) 0, (byte) function.length,')'),'*');
            calculateTheOperatorInArea((searchFor((byte) 0, (byte) function.length,'(')),searchFor((byte) 0, (byte) function.length,')'),'/');
            calculateTheOperatorInArea((searchFor((byte) 0, (byte) function.length,'(')),searchFor((byte) 0, (byte) function.length,')'),':');
            calculateTheOperatorInArea((searchFor((byte) 0, (byte) function.length,'(')),searchFor((byte) 0, (byte) function.length,')'),'+');
            calculateTheOperatorInArea((searchFor((byte) 0, (byte) function.length,'(')),searchFor((byte) 0, (byte) function.length,')'),'-');
            eliminateBracketsIfPossible();
        }
    }

    /**
     * This method calculates a specific area until the operator is nonexistent. The end and
     * I (attribute) need to be updated to ensure the dimension of the brackets is accurate. If the
     * operator does not exist in the area, the method will skip the while loop and stop.
     *
     * @param start : where to start the search for the operator.
     * @param end : where to stop the search for the operator.
     * @param operator : is the operator.
     */

    private void calculateTheOperatorInArea(byte start, byte end, char operator) {
        byte i = searchFor(start,end,operator); // i = index of the operator, ideally != -1.
        while( i != -1 ) {
            double x1 = Double.parseDouble(function[i - 1]);
            double x2 = Double.parseDouble(function[i + 1]);
            switch(operator) {
                case '+':
                    function[i] = "" + (x1 + x2);
                    function[i - 1] = "";
                    function[i + 1] = "";
                    shortenFunctionArray();
                    break;
                case '-':
                    function[i] = "" + (x1 - x2);
                    function[i - 1] = "";
                    function[i + 1] = "";
                    shortenFunctionArray();
                    break;
                case '*':
                    function[i] = "" + (x1 * x2);
                    function[i - 1] = "";
                    function[i + 1] = "";
                    shortenFunctionArray();
                    break;
                case ':':
                case '/':
                    function[i] = "" + (x1 / x2);
                    function[i - 1] = "";
                    function[i + 1] = "";
                    shortenFunctionArray();
                    break;
                case '^':
                    function[i] = "" + Math.pow(x1,x2);
                    function[i - 1] = "";
                    function[i + 1] = "";
                    shortenFunctionArray();
                    break;
                default:
                    break;
            }
            // Because the length of the function array is constantly changing, it needs to be updated per arithmetic operation.
            end = (end > (byte) function.length) ? (byte) function.length : searchFor((byte) 0, (byte) function.length,')');
            i = searchFor(start,end,operator);
        }
    }

    /**
     * This method takes the function as a String and puts it in a String array.
     * After that, the x will be replaced with the given double value. At first, the
     * brackets will be calculated, and then the whole function. It is necessary that
     * the function length is calculated every time, due to the constant change in length, e.g.
     * via the method 'shortenFunctionArray()'.
     *
     * @param x double value for X.
     * @return result as a double.
     */

    public double calculate(double x) {
        putXIntoArray(x);
        calculateBrackets();
        calculateTheOperatorInArea((byte) 0,(byte) function.length,'^');
        calculateTheOperatorInArea((byte) 0,(byte) function.length,'*');
        calculateTheOperatorInArea((byte) 0,(byte) function.length,'/');
        calculateTheOperatorInArea((byte) 0,(byte) function.length,':');
        calculateTheOperatorInArea((byte) 0,(byte) function.length,'+');
        calculateTheOperatorInArea((byte) 0,(byte) function.length,'-');
        return Double.parseDouble(Arrays.toString(function).replaceAll("\\[","").replaceAll("\\]",""));
    }

    /**
     * Returns the function in String form.
     *
     * @return
     */

    public String toString() {
        return Arrays.toString(function_save);
    }
}
