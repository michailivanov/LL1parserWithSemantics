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
        table.put(new Pair<>("S", "c"), List.of("S → ABC"));
        table.put(new Pair<>("A", "a"), List.of("A → aBB"));
        table.put(new Pair<>("A", "b"), List.of("A → ε"));
        table.put(new Pair<>("A", "c"), List.of("A → ε"));
        table.put(new Pair<>("B", "b"), List.of("B → bB"));
        table.put(new Pair<>("B", "c"), List.of("B → c"));
        table.put(new Pair<>("C", "a"), List.of("C → aC"));
        table.put(new Pair<>("C", "#"), List.of("C → ε"));
    }

    public List<String> getApplicableProductions(String nonTerminal, String terminal) {
        return table.getOrDefault(new Pair<>(nonTerminal, terminal), Collections.emptyList());
    }
}
