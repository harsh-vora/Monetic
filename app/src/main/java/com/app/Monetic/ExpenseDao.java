package com.app.Monetic;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;
@Dao
public interface ExpenseDao {
    @Insert
    void insert(Expense expense);
    @Query("SELECT * FROM expenses ORDER BY date DESC")
    List<Expense> getAllExpenses();
    @Query("SELECT * FROM expenses WHERE date >= :startDate AND date <= :endDate")
    List<Expense> getExpensesByDateRange(long startDate, long endDate);
    @Query("SELECT SUM(amount) FROM expenses WHERE category = :category")
    double getTotalByCategory(String category);
}