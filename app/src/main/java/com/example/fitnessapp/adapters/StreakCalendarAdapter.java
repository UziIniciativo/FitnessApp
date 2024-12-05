package com.example.fitnessapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnessapp.R;

import java.util.List;

public class StreakCalendarAdapter extends RecyclerView.Adapter<StreakCalendarAdapter.StreakViewHolder> {

    private final List<String> dates;
    private final int streakLength;
    private final Context context;

    public StreakCalendarAdapter(Context context, List<String> dates, int streakLength) {
        this.context = context;
        this.dates = dates;
        this.streakLength = streakLength;
    }

    @NonNull
    @Override
    public StreakViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_calendar_day, parent, false);
        return new StreakViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StreakViewHolder holder, int position) {
        String date = dates.get(position);
        holder.dateTextView.setText(date);

        // Resalta los días que están dentro de la racha
        if (position < streakLength) {
            holder.dateTextView.setBackgroundResource(R.drawable.streak_highlight);
            holder.dateTextView.setTextColor(context.getResources().getColor(R.color.white));
        } else {
            holder.dateTextView.setBackgroundResource(R.drawable.streak_normal);
            holder.dateTextView.setTextColor(context.getResources().getColor(R.color.text));
        }
    }

    @Override
    public int getItemCount() {
        return dates.size();
    }

    static class StreakViewHolder extends RecyclerView.ViewHolder {
        TextView dateTextView;

        StreakViewHolder(View itemView) {
            super(itemView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
        }
    }
}
