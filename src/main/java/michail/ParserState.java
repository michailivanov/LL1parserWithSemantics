package michail;

import java.util.List;
import java.util.Stack;

class ParserState {
    private Stack<String> stack;
    private int index;
    private List<String> productionSequence;
    private String finalTerminals;

    public ParserState(List<String> stackValues, int index, List<String> productionSequence, String finalTerminals) {
        this.stack = new Stack<>();
        for (String val : stackValues) {
            this.stack.push(val);
        }
        this.index = index;
        this.productionSequence = productionSequence;
        this.finalTerminals = finalTerminals;
    }

    public Stack<String> getStack() {
        return stack;
    }

    public int getIndex() {
        return index;
    }

    public List<String> getProductionSequence() {
        return productionSequence;
    }

    public String getFinalTerminals() {
        return finalTerminals;
    }
}