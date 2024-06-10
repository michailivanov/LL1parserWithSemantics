package michail;

import java.lang.reflect.Array;
import java.util.*;

public class Parser {
    private ParsingTable parsingTable;
    private Stack<String> stack;
    private String input;
    private int index;
    private String finalTerminals;

    public Parser(ParsingTable parsingTable) {
        this.parsingTable = parsingTable;
        this.stack = new Stack<>();
        this.finalTerminals = "";
    }

    public boolean parse(String input) {
        this.input = input;
        this.index = 0;
        stack.push("#");
        stack.push("S");
        String sentenceForm = "S";

        while (!stack.isEmpty()) {
            String top = stack.peek();

            printCurrentState(stack, index, top);

            if (TerminalMatcher.isTerminal(top)) {
                if (TerminalMatcher.match(top, input, index)) {
                    stack.pop();
                    index++;
                    finalTerminals += top;
                } else {
                    System.out.println("Parsing failed. Unexpected terminal: " + getInputSymbol(index));
                    return false;
                }
            } else {
                List<String> applicableProductions = parsingTable.getApplicableProductions(top, getInputSymbol(index));

                if (applicableProductions.isEmpty()) {
                    System.out.println("Parsing failed. No applicable production for non-terminal: " + top);
                    return false;
                }

                String production = applicableProductions.get(0);
                stack.pop();

                ProductionExpander.expand(production, stack);

                System.out.println("Applying production: " + production);

                ArrayList<String> stackCopy = new ArrayList<>(stack);
                stackCopy.removeFirst();
                sentenceForm += " → " + finalTerminals + String.join("", stackCopy.reversed());
            }

            System.out.println("------------------------");

            if (isParsingFinished(index, stack)) {
                printParsingResult(stack, sentenceForm);
                return true;
            }
        }

        System.out.println("Parsing failed. Unexpected end of input.");
        return false;
    }

    private boolean isParsingFinished(int index, Stack<String> stack) {
        return index == input.length() && stack.size() == 1 && stack.peek().equals("#");
    }

    private void printParsingResult(Stack<String> stack, String sentenceForm) {
        System.out.println("Parsing finished.");
        System.out.println("Final stack: " + stack);
        System.out.println("Production sequence: " + sentenceForm);
    }

    private void printCurrentState(Stack<String> stack, int index, String top) {
        System.out.println("Current stack: " + stack);
        System.out.println("Current input symbol: " + getInputSymbol(index));
        System.out.println("Top of the stack: " + top);
    }

    private String getInputSymbol(int index) {
        return index < input.length() ? String.valueOf(input.charAt(index)) : "#";
    }
}