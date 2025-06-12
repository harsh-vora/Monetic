package com.app.Monetic;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.Ignore;
@Entity(tableName = "expenses")
public class Expense {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private double amount;
    private String category;
    private String paymentMethod;
    private String comment;
    private long date;
    // Constructors
    public Expense() {}
    @Ignore
    public Expense(double amount, String category, String paymentMethod, String comment, long date) {
        this.amount = amount;
        this.category = category;
        this.paymentMethod = paymentMethod;
        this.comment = comment;
        this.date = date;
    }
    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
    public long getDate() { return date; }
    public void setDate(long date) { this.date = date; }
}