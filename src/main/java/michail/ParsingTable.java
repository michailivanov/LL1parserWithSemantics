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
        table.put(new Pair<>("S", "a"), "S → AB0C");
        table.put(new Pair<>("S", "b"), "S → AB0C");
        table.put(new Pair<>("S", "c"), "S → AB0C");
        table.put(new Pair<>("A", "a"), "A → a1BB");
        table.put(new Pair<>("A", "b"), "A → 2ε");
        table.put(new Pair<>("A", "c"), "A → 2ε");
        table.put(new Pair<>("B", "b"), "B → bB3");
        table.put(new Pair<>("B", "c"), "B → c4");
        table.put(new Pair<>("C", "a"), "C → 5aC");
        table.put(new Pair<>("C", "#"), "C → ε6");
    }

    public String getProduction(String nonTerminal, String terminal) {
        return table.getOrDefault(new Pair<>(nonTerminal, terminal), null);
    }
}
