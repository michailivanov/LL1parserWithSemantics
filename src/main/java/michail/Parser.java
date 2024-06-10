package michail;

import java.util.*;

public class Parser {
    private ParsingTable parsingTable;
    private Stack<ParserState> stateStack;
    private String input;

    public Parser(ParsingTable parsingTable) {
        this.parsingTable = parsingTable;
        this.stateStack = new Stack<>();
    }

    public boolean parse(String input) {
        this.input = input;
        stateStack.push(new ParserState(List.of("#", "S"), 0, new ArrayList<>(), ""));

        while (!stateStack.isEmpty()) {
            ParserState currentState = stateStack.pop();
            Stack<String> stack = currentState.getStack();
            int index = currentState.getIndex();
            List<String> productionSequence = currentState.getProductionSequence();
            String finalTerminals = currentState.getFinalTerminals();

            if (isParsingFinished(index, stack)) {
                printParsingResult(stack, productionSequence);
                return true;
            }

            if (isInputExhausted(index, stack)) {
                continue;
            }

            String top = stack.peek();

            printCurrentState(stack, index, top);

            if (TerminalMatcher.isTerminal(top)) {
                if (TerminalMatcher.match(top, input, index)) {
                    stack.pop();
                    index++;
                    finalTerminals += top;
                    continue;
                }
            } else {
                List<String> applicableProductions = parsingTable.getApplicableProductions(top, getInputSymbol(index));

                for (String production : applicableProductions) {
                    Stack<String> newStack = new Stack<>();
                    newStack.addAll(stack);
                    newStack.pop();

                    ProductionExpander.expand(production, newStack);

                    List<String> newProductionSequence = new ArrayList<>(productionSequence);
                    newProductionSequence.add(production);

                    System.out.println("Applying production: " + production);
                    stateStack.push(new ParserState(newStack, index, newProductionSequence, finalTerminals));
                }
            }

            System.out.println("------------------------");
        }

        System.out.println("No more states to explore. Parsing failed.");
        return false;
    }

    private boolean isParsingFinished(int index, Stack<String> stack) {
        return index == input.length() && stack.size() == 1 && stack.peek().equals("#");
    }

    private void printParsingResult(Stack<String> stack, List<String> productionSequence) {
        System.out.println("Parsing finished.");
        System.out.println("Final stack: " + stack);
        System.out.println("Production sequence: " + String.join(" → ", productionSequence));
    }

    private boolean isInputExhausted(int index, Stack<String> stack) {
        return index == input.length() && stack.size() > 1;
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