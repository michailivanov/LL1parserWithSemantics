package michail;

import java.util.*;

public class Parser {
    private ParsingTable parsingTable;
    private Stack<String> stack;
    private String input;
    private int index;
    private String finalTerminals;
    private String sentenceForm;
    private Stack<Integer> semanticActions;

    public Parser(ParsingTable parsingTable) {
        this.parsingTable = parsingTable;
        this.stack = new Stack<>();
        this.semanticActions = new Stack<>();
    }

    public boolean parse(String input) {
        this.input = input;
        this.index = 0;
        stack = new Stack<>();
        stack.push("#");
        stack.push("S");
        this.finalTerminals = "";
        this.sentenceForm = "S";
        this.semanticActions = new Stack<>();

        while (!stack.isEmpty()) {
            String top = stack.peek();

            printCurrentState(stack, index, top);

            if (TerminalMatcher.isTerminal(top)) {
                if (TerminalMatcher.match(top, input, index)) {
                    stack.pop();
                    index++;
                    finalTerminals += top;
                } else {
                    System.out.println("------------------------");
                    System.out.println("Parsing failed. Unexpected terminal: " + getInputSymbol(index));
                    printParsingResult();
                    return false;
                }
            } else {
                String production = parsingTable.getProduction(top, getInputSymbol(index));

                if (production == null) {
                    System.out.println("------------------------");
                    System.out.println("Parsing failed. No acceptable production for non-terminal: " + top);
                    printParsingResult();
                    return false;
                }

                stack.pop();

                String[] symbols = production.split("→")[1].trim().split("");
                for (int i = symbols.length - 1; i >= 0; i--) {
                    String symbol = symbols[i];
                    if (!symbol.equals("ε")) {
                        if (Character.isDigit(symbol.charAt(0))) {
                            int action = Integer.parseInt(symbol);
                            semanticActions.push(action);
                        } else {
                            stack.push(symbol);
                        }
                    }
                }

                System.out.println("Applying production: " + production);

                ArrayList<String> stackCopy = new ArrayList<>(stack);
                stackCopy.removeFirst();
                sentenceForm += " → " + finalTerminals + String.join("", stackCopy.reversed());
            }

            System.out.println("------------------------");

            if (isParsingFinished(index, stack)) {
                System.out.println("Parsing finished successfully.");
                printParsingResult();
                printSemanticActions();
                return true;
            }
        }

        System.out.println("Parsing failed. Unexpected end of input.");
        printParsingResult();
        return false;
    }

    private boolean isParsingFinished(int index, Stack<String> stack) {
        return index == input.length() && stack.size() == 1 && stack.peek().equals("#");
    }

    private void printSemanticActions() {
        System.out.println("Semantic Actions: " + semanticActions);
        System.out.println("Semantic Actions (interpreted):");
        for (int action : semanticActions) {
            switch (action) {
                case 0:
                    System.out.println("LOAD reg, var;");
                    break;
                case 1:
                    System.out.println("STORE reg, var;");
                    break;
                case 2:
                    System.out.println("ADD reg1, reg2;");
                    break;
                case 3:
                    System.out.println("MUL reg1, reg2;");
                    break;
                case 4:
                    System.out.println("CMP reg1, reg2;");
                    break;
                case 5:
                    System.out.println("JMP label;");
                    break;
                case 6:
                    System.out.println("CALL function;");
                    break;
            }
        }
    }

    private void printParsingResult() {
        System.out.println("Final stack: " + stack);
        System.out.println("Input was: " + input);
        if(stack.size() == 1) { // [#]
            System.out.println("Production sequence: " + sentenceForm);
        } else {
            System.out.println("Production sequence: " + sentenceForm + " → ERROR");
        }

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