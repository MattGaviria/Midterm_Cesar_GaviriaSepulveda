package com.example.midterm_cesar_gaviriasepulveda;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;

public class HistoryFragment extends Fragment {

    private TimesViewModel vm;
    private ArrayAdapter<Integer> adapter;
    private final ArrayList<Integer> items = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        vm = new ViewModelProvider(requireActivity()).get(TimesViewModel.class);

        ListView lv = view.findViewById(R.id.lvHistory);
        adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, items);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener((parent, v, position, id) -> {
            if (position < 0 || position >= items.size()) return;
            int base = items.get(position);
            vm.loadTable(base);
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).switchToTable();
            }
        });

        vm.getHistory().observe(getViewLifecycleOwner(), list -> {
            items.clear();
            if (list != null) items.addAll(list);
            adapter.notifyDataSetChanged();
        });
    }
}