package michail;

class TerminalMatcher {
    public static boolean isTerminal(String symbol) {
        return symbol.equals("a") || symbol.equals("b") || symbol.equals("#");
    }

    public static boolean match(String terminal, String input, int index) {
        if (index < input.length() && terminal.equals(String.valueOf(input.charAt(index)))) {
            System.out.println("Matched terminal: " + terminal);
            return true;
        }
        return false;
    }
}
