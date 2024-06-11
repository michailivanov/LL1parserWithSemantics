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
                if (Character.isDigit(top.charAt(0))) {
                    int semanticNum = Character.getNumericValue(top.charAt(0));
                    if (semanticNum <= 6 && semanticNum >= 0) {
                        stack.pop();
                        semanticActions.push(semanticNum);
                        System.out.println("Semantic value found: " + semanticNum);
                    } else {
                        throw new RuntimeException("ERROR: there's no such semantic number: " + semanticNum);
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
                            stack.push(symbol);
                        }
                    }

                    System.out.println("Applying production: " + production);

                    ArrayList<String> stackCopy = new ArrayList<>(stack);
                    stackCopy.removeFirst();
                    sentenceForm += " → " + finalTerminals + String.join("", stackCopy.reversed());
                }
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

    public String generate() {
        StringBuilder sb = new StringBuilder();
        Stack<String> generationStack = new Stack<>();
        generationStack.push("S");

        while (!generationStack.isEmpty()) {
            String symbol = generationStack.pop();
            if (TerminalMatcher.isTerminal(symbol)) {
                sb.append(symbol);
            } else {
                String production = getProductionForGeneration(symbol);
                if (production != null) {
                    String[] symbols = production.split("→")[1].trim().split("");
                    for (int i = symbols.length - 1; i >= 0; i--) {
                        String s = symbols[i];
                        if (!s.equals("ε")) {
                            generationStack.push(s);
                        }
                    }
                }
            }
        }

        return sb.toString();
    }

    private String getProductionForGeneration(String nonTerminal) {
        List<String> terminals = Arrays.asList("a", "b", "c", "#");
        List<String> possibleProductions = new ArrayList<>();
        for (String terminal : terminals) {
            String production = parsingTable.getProduction(nonTerminal, terminal);
            if (production != null) {
                possibleProductions.add(production);
            }
        }

        if (possibleProductions.isEmpty()) {
            return null; // no valid production is found
        }

        Random random = new Random();
        int randomIndex = random.nextInt(possibleProductions.size());
        return possibleProductions.get(randomIndex);
    }

}