package com.example.currencyconverteer;

import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    EditText amount;
    Spinner from, to;
    TextView result;
    Button btn;

    String[] currencies = {"INR", "USD", "EUR", "JPY"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        amount = findViewById(R.id.amount);
        from = findViewById(R.id.from);
        to = findViewById(R.id.to);
        result = findViewById(R.id.result);
        btn = findViewById(R.id.btn);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, currencies);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        from.setAdapter(adapter);
        to.setAdapter(adapter);

        btn.setOnClickListener(v -> {

            String amtStr = amount.getText().toString();

            if (amtStr.isEmpty()) {
                result.setText(R.string.enter_amount);
                return;
            }

            double amt = Double.parseDouble(amtStr);

            String fromCur = from.getSelectedItem().toString();
            String toCur = to.getSelectedItem().toString();

            double converted = amt * getRate(fromCur, toCur);

            result.setText(getString(R.string.result_format, converted, toCur));
        });
    }

    double getRate(String from, String to) {

        double usd = 83.0;
        double eur = 90.0;
        double jpy = 0.55;

        double fromRate = 1, toRate = 1;

        if (from.equals("USD")) fromRate = usd;
        if (from.equals("EUR")) fromRate = eur;
        if (from.equals("JPY")) fromRate = jpy;

        if (to.equals("USD")) toRate = usd;
        if (to.equals("EUR")) toRate = eur;
        if (to.equals("JPY")) toRate = jpy;

        return fromRate / toRate;
    }
}