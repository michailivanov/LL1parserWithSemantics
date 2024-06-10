package michail;

import java.util.Stack;

class ProductionExpander {
    public static void expand(String production, Stack<String> stack) {
        String[] symbols = production.split("→")[1].trim().split("");
        for (int i = symbols.length - 1; i >= 0; i--) {
            if (!symbols[i].equals("ε")) {
                stack.push(symbols[i]);
            }
        }
    }
}
