package com.app.Monetic;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
    private List<CategorySummary> categories;
    public CategoryAdapter(List<CategorySummary> categories) {
        this.categories = categories;
    }
    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category, parent, false);
        return new CategoryViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        CategorySummary category = categories.get(position);
        holder.bind(category);
    }
    @Override
    public int getItemCount() {
        return categories.size();
    }
    public void updateData(List<CategorySummary> newCategories) {
        this.categories = newCategories;
        notifyDataSetChanged();
    }
    static class CategoryViewHolder extends RecyclerView.ViewHolder {
        private ImageView iconImageView;
        private TextView nameTextView;
        private TextView paymentMethodsTextView;
        private TextView amountTextView;
        private TextView percentageTextView;
        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            iconImageView = itemView.findViewById(R.id.categoryIcon);
            nameTextView = itemView.findViewById(R.id.categoryName);
            paymentMethodsTextView = itemView.findViewById(R.id.paymentMethods);
            amountTextView = itemView.findViewById(R.id.categoryAmount);
            percentageTextView = itemView.findViewById(R.id.categoryPercentage);
        }
        public void bind(CategorySummary category) {
            iconImageView.setImageResource(category.getIconResource());
            nameTextView.setText(category.getName());
            paymentMethodsTextView.setText(category.getPaymentMethods());

            NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);
            amountTextView.setText(currencyFormat.format(category.getAmount()));
            percentageTextView.setText(category.getPercentage() + "%");
        }
    }
}