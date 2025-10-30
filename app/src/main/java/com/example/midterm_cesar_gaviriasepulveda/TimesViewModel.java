package com.example.midterm_cesar_gaviriasepulveda;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TimesViewModel extends ViewModel {

    private final MutableLiveData<List<String>> rowStrings = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<List<Integer>> history = new MutableLiveData<>(new ArrayList<>());

    // Remember which multipliers were deleted per base during this session
    private final Map<Integer, Set<Integer>> deletedByBase = new HashMap<>();

    public LiveData<List<String>> getRowStrings() { return rowStrings; }
    public LiveData<List<Integer>> getHistory() { return history; }

    public void generateTable(int base) {
        // update history
        List<Integer> h = new ArrayList<>(history.getValue());
        if (!h.contains(base)) h.add(base);
        history.setValue(h);

        // rebuild rows 1..10 honoring deletions
        Set<Integer> deleted = deletedByBase.getOrDefault(base, new HashSet<>());
        ArrayList<String> labels = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            if (deleted.contains(i)) continue;
            labels.add(base + " × " + i + " = " + (base * i));
        }
        rowStrings.setValue(labels);
    }

    public void loadTable(int base) {
        if (base < 1) return;
        generateTable(base);
    }

    public void deleteRowAt(int position) {
        List<String> labels = new ArrayList<>(rowStrings.getValue());
        if (position < 0 || position >= labels.size()) return;

        // infer multiplier from the label
        String label = labels.get(position); // e.g., "7 × 3 = 21"
        try {
            String[] leftRight = label.split("=");
            String left = leftRight[0]; // "7 × 3 "
            String[] baseMul = left.split("×");
            int base = Integer.parseInt(baseMul[0].trim());
            int multiplier = Integer.parseInt(baseMul[1].trim());
            deletedByBase.computeIfAbsent(base, k -> new HashSet<>()).add(multiplier);
        } catch (Exception ignored) { }

        labels.remove(position);
        rowStrings.setValue(labels);
    }

    public void clearAll() {
        rowStrings.setValue(new ArrayList<>());
        history.setValue(new ArrayList<>());
        deletedByBase.clear();
    }
}

