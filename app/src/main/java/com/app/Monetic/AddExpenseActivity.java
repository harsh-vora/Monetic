package com.app.Monetic;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
public class AddExpenseActivity extends AppCompatActivity {
    private EditText amountEditText;
    private EditText commentEditText;
    private Spinner paymentMethodSpinner;
    private Spinner categorySpinner;
    private MaterialButton saveButton;
    private ExpenseDatabase database;
    private List<String> paymentMethods = new ArrayList<>(Arrays.asList("Cash", "Card", "Digital Wallet"));
    private List<String> categories = new ArrayList<>(Arrays.asList("Shopping", "Food", "Gifts", "Transport", "Entertainment", "Bills", "Other"));
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);
        initViews();
        setupSpinners();
        setupClickListeners();

        database = ExpenseDatabase.getInstance(this);
    }
    private void initViews() {
        amountEditText = findViewById(R.id.amountEditText);
        commentEditText = findViewById(R.id.commentEditText);
        paymentMethodSpinner = findViewById(R.id.paymentMethodSpinner);
        categorySpinner = findViewById(R.id.categorySpinner);
        saveButton = findViewById(R.id.saveButton);
    }
    private void setupSpinners() {
        ArrayAdapter<String> paymentAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, paymentMethods);
        paymentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        paymentMethodSpinner.setAdapter(paymentAdapter);
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, categories);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryAdapter);
    }
    private void setupClickListeners() {
        saveButton.setOnClickListener(v -> saveExpense());

        findViewById(R.id.backButton).setOnClickListener(v -> finish());
    }
    private void saveExpense() {
        String amountStr = amountEditText.getText().toString().trim();
        String comment = commentEditText.getText().toString().trim();
        String paymentMethod = paymentMethodSpinner.getSelectedItem().toString();
        String category = categorySpinner.getSelectedItem().toString();
        if (TextUtils.isEmpty(amountStr)) {
            Toast.makeText(this, "Please enter amount", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            double amount = Double.parseDouble(amountStr);
            if (amount <= 0) {
                Toast.makeText(this, "Please enter valid amount", Toast.LENGTH_SHORT).show();
                return;
            }
            Expense expense = new Expense();
            expense.setAmount(amount);
            expense.setCategory(category);
            expense.setPaymentMethod(paymentMethod);
            expense.setComment(comment);
            expense.setDate(System.currentTimeMillis());
            new Thread(() -> {
                database.expenseDao().insert(expense);
                runOnUiThread(() -> {
                    Toast.makeText(AddExpenseActivity.this, "Expense saved successfully", Toast.LENGTH_SHORT).show();
                    finish();
                });
            }).start();
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter valid amount", Toast.LENGTH_SHORT).show();
        }
    }
}