package com.app.Monetic;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
public class MainActivity extends AppCompatActivity {
    private TextView totalBalanceText;
    private TextView dailyExpenseText;
    private TextView weeklyExpenseText;
    private TextView monthlyExpenseText;
    private RecyclerView categoryRecyclerView;
    private ExpenseBarChart expenseBarChart;
    private FloatingActionButton fabAddExpense;
    private ExpenseDatabase database;
    private CategoryAdapter categoryAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        setupRecyclerView();
        setupClickListeners();

        database = ExpenseDatabase.getInstance(this);
        loadExpenseData();
    }
    private void initViews() {
        totalBalanceText = findViewById(R.id.totalBalanceText);
        dailyExpenseText = findViewById(R.id.dailyExpenseText);
        weeklyExpenseText = findViewById(R.id.weeklyExpenseText);
        monthlyExpenseText = findViewById(R.id.monthlyExpenseText);
        categoryRecyclerView = findViewById(R.id.categoryRecyclerView);
        expenseBarChart = findViewById(R.id.expenseBarChart);
        fabAddExpense = findViewById(R.id.fabAddExpense);
    }
    private void setupRecyclerView() {
        categoryAdapter = new CategoryAdapter(new ArrayList<>());
        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        categoryRecyclerView.setAdapter(categoryAdapter);
    }
    private void setupClickListeners() {
        fabAddExpense.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddExpenseActivity.class);
            startActivity(intent);
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        loadExpenseData();
    }
    private void loadExpenseData() {
        new Thread(() -> {
            List<Expense> expenses = database.expenseDao().getAllExpenses();

            runOnUiThread(() -> {
                updateUI(expenses);
            });
        }).start();
    }
    private void updateUI(List<Expense> expenses) {
        double totalBalance = 32500.00; // Starting balance
        double dailyExpense = calculateDailyExpense(expenses);
        double weeklyExpense = calculateWeeklyExpense(expenses);
        double monthlyExpense = calculateMonthlyExpense(expenses);
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);
        totalBalanceText.setText(currencyFormat.format(totalBalance - monthlyExpense));
        dailyExpenseText.setText(currencyFormat.format(dailyExpense).replace("$", "$"));
        weeklyExpenseText.setText(currencyFormat.format(weeklyExpense).replace("$", "$"));
        monthlyExpenseText.setText(currencyFormat.format(monthlyExpense).replace("$", "$"));
        // Update category breakdown
        List<CategorySummary> categorySummaries = calculateCategorySummaries(expenses, monthlyExpense);
        categoryAdapter.updateData(categorySummaries);
        // Update bar chart
        expenseBarChart.updateChart(expenses);
    }
    private double calculateDailyExpense(List<Expense> expenses) {
        Calendar today = Calendar.getInstance();
        double total = 0;
        for (Expense expense : expenses) {
            Calendar expenseDate = Calendar.getInstance();
            expenseDate.setTimeInMillis(expense.getDate());
            if (isSameDay(today, expenseDate)) {
                total += expense.getAmount();
            }
        }
        return total;
    }
    private double calculateWeeklyExpense(List<Expense> expenses) {
        Calendar today = Calendar.getInstance();
        Calendar weekStart = Calendar.getInstance();
        weekStart.add(Calendar.DAY_OF_YEAR, -7);

        double total = 0;
        for (Expense expense : expenses) {
            Calendar expenseDate = Calendar.getInstance();
            expenseDate.setTimeInMillis(expense.getDate());
            if (expenseDate.after(weekStart) && expenseDate.before(today)) {
                total += expense.getAmount();
            }
        }
        return total;
    }
    private double calculateMonthlyExpense(List<Expense> expenses) {
        Calendar today = Calendar.getInstance();
        int currentMonth = today.get(Calendar.MONTH);
        int currentYear = today.get(Calendar.YEAR);

        double total = 0;
        for (Expense expense : expenses) {
            Calendar expenseDate = Calendar.getInstance();
            expenseDate.setTimeInMillis(expense.getDate());
            if (expenseDate.get(Calendar.MONTH) == currentMonth &&
                    expenseDate.get(Calendar.YEAR) == currentYear) {
                total += expense.getAmount();
            }
        }
        return total;
    }
    private boolean isSameDay(Calendar cal1, Calendar cal2) {
        return cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR) &&
                cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR);
    }
    private List<CategorySummary> calculateCategorySummaries(List<Expense> expenses, double totalMonthly) {
        List<CategorySummary> summaries = new ArrayList<>();

        // Group expenses by category
        double shopping = 0, gifts = 0, food = 0, other = 0;

        for (Expense expense : expenses) {
            Calendar expenseDate = Calendar.getInstance();
            expenseDate.setTimeInMillis(expense.getDate());
            Calendar today = Calendar.getInstance();

            if (expenseDate.get(Calendar.MONTH) == today.get(Calendar.MONTH)) {
                switch (expense.getCategory().toLowerCase()) {
                    case "shopping":
                        shopping += expense.getAmount();
                        break;
                    case "gifts":
                        gifts += expense.getAmount();
                        break;
                    case "food":
                        food += expense.getAmount();
                        break;
                    default:
                        other += expense.getAmount();
                        break;
                }
            }
        }
        if (shopping > 0) {
            summaries.add(new CategorySummary("Shopping", "Cash", shopping,
                    totalMonthly > 0 ? (int)(shopping / totalMonthly * 100) : 0, R.drawable.ic_shopping));
        }
        if (gifts > 0) {
            summaries.add(new CategorySummary("Gifts", "Cash • Card", gifts,
                    totalMonthly > 0 ? (int)(gifts / totalMonthly * 100) : 0, R.drawable.ic_gift));
        }
        if (food > 0) {
            summaries.add(new CategorySummary("Food", "Cash", food,
                    totalMonthly > 0 ? (int)(food / totalMonthly * 100) : 0, R.drawable.ic_food));
        }
        return summaries;
    }
}