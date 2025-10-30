package com.example.midterm_cesar_gaviriasepulveda;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;

public class TableFragment extends Fragment {

    private TimesViewModel vm;
    private ArrayAdapter<String> adapter;
    private final ArrayList<String> items = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_table, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        vm = new ViewModelProvider(requireActivity()).get(TimesViewModel.class);

        EditText edtNumber = view.findViewById(R.id.edtNumber);
        Button btnGenerate = view.findViewById(R.id.btnGenerate);
        Button btnClearAll = view.findViewById(R.id.btnClearAll);
        ListView lvTable = view.findViewById(R.id.lvTable);

        adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, items);
        lvTable.setAdapter(adapter);

        btnGenerate.setOnClickListener(v -> {
            String input = edtNumber.getText().toString().trim();
            if (input.isEmpty()) {
                Toast.makeText(requireContext(), "Please enter a number", Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                int base = Integer.parseInt(input);
                edtNumber.onEditorAction(EditorInfo.IME_ACTION_DONE);
                vm.generateTable(base);
            } catch (NumberFormatException e) {
                Toast.makeText(requireContext(), "Invalid number", Toast.LENGTH_SHORT).show();
            }
        });

        btnClearAll.setOnClickListener(v -> {
            new AlertDialog.Builder(requireContext())
                    .setTitle("Clear all?")
                    .setMessage("This clears current tables and history for this session.")
                    .setPositiveButton("Clear", (d, w) -> vm.clearAll())
                    .setNegativeButton("Cancel", null)
                    .show();
        });

        lvTable.setOnItemClickListener((AdapterView<?> parent, View v2, int position, long id) -> {
            String row = (position >= 0 && position < items.size()) ? items.get(position) : "";
            new AlertDialog.Builder(requireContext())
                    .setTitle("Delete row?")
                    .setMessage("Do you want to delete:\n\n" + row)
                    .setPositiveButton("Delete", (d, w) -> {
                        vm.deleteRowAt(position);
                        Toast.makeText(requireContext(), "Deleted: " + row, Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });

        vm.getRowStrings().observe(getViewLifecycleOwner(), list -> {
            items.clear();
            if (list != null) items.addAll(list);
            adapter.notifyDataSetChanged();
        });
    }
}