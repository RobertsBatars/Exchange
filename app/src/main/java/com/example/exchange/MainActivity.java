package com.example.exchange;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity implements DataLoader.DataLoadCallback {
    private List<CurrencyRate> allRates = new ArrayList<>();
    private ArrayAdapter<CurrencyRate> adapter;
    private EditText searchEditText;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        searchEditText = findViewById(R.id.searchEditText);
        listView = findViewById(R.id.currencyListView);

        // Setup adapter
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new ArrayList<>());
        listView.setAdapter(adapter);

        // Setup search functionality
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterRates(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Load data
        new DataLoader(this).execute();
    }

    private void filterRates(String query) {
        if (query.isEmpty()) {
            updateList(allRates);
        } else {
            List<CurrencyRate> filtered = allRates.stream()
                    .filter(rate -> rate.getCurrencyCode().toLowerCase().contains(query.toLowerCase()))
                    .collect(Collectors.toList());
            updateList(filtered);
        }
    }

    private void updateList(List<CurrencyRate> rates) {
        adapter.clear();
        adapter.addAll(rates);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onDataLoaded(List<CurrencyRate> rates) {
        allRates = rates;
        runOnUiThread(() -> updateList(rates));
    }

    @Override
    public void onError(String error) {
        runOnUiThread(() -> {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.error_loading)
                    .setMessage(error)
                    .setPositiveButton(R.string.retry, (dialog, which) -> {
                        new DataLoader(this).execute();
                    })
                    .setNegativeButton(R.string.ok, null)
                    .show();
        });
    }
}