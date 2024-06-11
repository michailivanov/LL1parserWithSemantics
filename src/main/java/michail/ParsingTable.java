package michail;

import java.util.HashMap;
import java.util.Map;

class ParsingTable {
    private Map<Pair<String, String>, String> table;

    public ParsingTable() {
        table = new HashMap<>();
        initializeTable();
    }

    private void initializeTable() {
        table.put(new Pair<>("S", "a"), "S → ABC");
        table.put(new Pair<>("S", "b"), "S → ABC");
        table.put(new Pair<>("S", "c"), "S → ABC");
        table.put(new Pair<>("A", "a"), "A → aBB");
        table.put(new Pair<>("A", "b"), "A → ε");
        table.put(new Pair<>("A", "c"), "A → ε");
        table.put(new Pair<>("B", "b"), "B → bB");
        table.put(new Pair<>("B", "c"), "B → c");
        table.put(new Pair<>("C", "a"), "C → aC");
        table.put(new Pair<>("C", "#"), "C → ε");
    }

    public String getProduction(String nonTerminal, String terminal) {
        return table.getOrDefault(new Pair<>(nonTerminal, terminal), null);
    }
}
