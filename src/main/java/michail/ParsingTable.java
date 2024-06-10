package michail;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class ParsingTable {
    private Map<Pair<String, String>, List<String>> table;

    public ParsingTable() {
        table = new HashMap<>();
        initializeTable();
    }

    private void initializeTable() {
        table.put(new Pair<>("S", "a"), List.of("S → ABC"));
        table.put(new Pair<>("S", "b"), List.of("S → ABC"));
        table.put(new Pair<>("A", "a"), List.of("A → BB"));
        table.put(new Pair<>("A", "b"), List.of("A → BB"));
        table.put(new Pair<>("A", "#"), List.of("A → ε"));
        table.put(new Pair<>("B", "a"), List.of("B → a"));
        table.put(new Pair<>("B", "b"), List.of("B → CC"));
        table.put(new Pair<>("C", "a"), List.of("C → AA"));
        table.put(new Pair<>("C", "b"), List.of("C → b"));
    }

    public List<String> getApplicableProductions(String nonTerminal, String terminal) {
        return table.getOrDefault(new Pair<>(nonTerminal, terminal), Collections.emptyList());
    }
}
