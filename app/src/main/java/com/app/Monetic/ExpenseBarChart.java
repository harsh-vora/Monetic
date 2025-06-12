package com.app.Monetic;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import java.util.Calendar;
import java.util.List;
public class ExpenseBarChart extends View {
    private Paint barPaint;
    private Paint textPaint;
    private List<Expense> expenses;
    private float[] dailyAmounts;
    private int[] barColors = {
            0xFFFFB3BA, 0xFFBAE1FF, 0xFFFFFFBA, 0xFFBAFFBA,
            0xFFE1BAFF, 0xFFFFBAE1, 0xFFBAFFE1, 0xFFFFE1BA
    };
    public ExpenseBarChart(Context context) {
        super(context);
        init();
    }
    public ExpenseBarChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    private void init() {
        barPaint = new Paint();
        barPaint.setAntiAlias(true);

        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(24);
        textPaint.setColor(0xFF666666);
        textPaint.setTextAlign(Paint.Align.CENTER);

        dailyAmounts = new float[31]; // Maximum days in a month
    }
    public void updateChart(List<Expense> expenses) {
        this.expenses = expenses;
        calculateDailyAmounts();
        invalidate();
    }
    private void calculateDailyAmounts() {
        // Reset array
        for (int i = 0; i < dailyAmounts.length; i++) {
            dailyAmounts[i] = 0;
        }
        Calendar currentMonth = Calendar.getInstance();
        int month = currentMonth.get(Calendar.MONTH);
        int year = currentMonth.get(Calendar.YEAR);
        for (Expense expense : expenses) {
            Calendar expenseDate = Calendar.getInstance();
            expenseDate.setTimeInMillis(expense.getDate());

            if (expenseDate.get(Calendar.MONTH) == month &&
                    expenseDate.get(Calendar.YEAR) == year) {
                int day = expenseDate.get(Calendar.DAY_OF_MONTH) - 1; // 0-indexed
                if (day >= 0 && day < dailyAmounts.length) {
                    dailyAmounts[day] += (float) expense.getAmount();
                }
            }
        }
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (dailyAmounts == null) return;
        int width = getWidth() - getPaddingLeft() - getPaddingRight();
        int height = getHeight() - getPaddingTop() - getPaddingBottom();

        Calendar cal = Calendar.getInstance();
        int daysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

        float barWidth = (float) width / daysInMonth;
        float maxAmount = getMaxAmount();

        if (maxAmount == 0) return;
        for (int i = 0; i < daysInMonth; i++) {
            float amount = dailyAmounts[i];
            if (amount > 0) {
                float barHeight = (amount / maxAmount) * height * 0.8f;
                float left = getPaddingLeft() + i * barWidth + barWidth * 0.1f;
                float right = left + barWidth * 0.8f;
                float top = getPaddingTop() + height - barHeight;
                float bottom = getPaddingTop() + height;

                barPaint.setColor(barColors[i % barColors.length]);
                canvas.drawRoundRect(new RectF(left, top, right, bottom), 8, 8, barPaint);

                // Draw percentage
                int percentage = (int) ((amount / maxAmount) * 100);
                canvas.drawText(percentage + "%", left + barWidth * 0.4f, top - 10, textPaint);
            }
        }

        // Draw day numbers
        textPaint.setTextSize(18);
        for (int i = 0; i < daysInMonth; i += 5) { // Show every 5th day
            float x = getPaddingLeft() + i * barWidth + barWidth * 0.5f;
            canvas.drawText(String.valueOf(i + 1), x, height + getPaddingTop() + 30, textPaint);
        }
    }
    private float getMaxAmount() {
        float max = 0;
        for (float amount : dailyAmounts) {
            if (amount > max) {
                max = amount;
            }
        }
        return max;
    }
}
