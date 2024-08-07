package com.cate.studentprogresstracker.ui;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cate.studentprogresstracker.R;

import java.util.List;

import entities.Term;

public class TermAdapter extends RecyclerView.Adapter<TermAdapter.TermViewHolder> {

    private final Context context;
    private final LayoutInflater mInflater;

    private List<Term> mTerms;

    public TermAdapter(Context context) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public TermAdapter.TermViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.term_list_item, parent, false);
        return new TermViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TermAdapter.TermViewHolder holder, int position) {
        if(mTerms != null) {
            Term currentTerm = mTerms.get(position);
            String title     = currentTerm.getTermTitle();
            String startDate = currentTerm.getTermStartDate();
            String endDate   = currentTerm.getTermEndDate();

            holder.termItemView.setText(title);
            holder.termItemStartDate.setText(startDate);
            holder.termItemEndDate.setText(endDate);
        }
        else {
            holder.termItemView.setText(R.string.no_data);
        }
    }

    @Override
    public int getItemCount() {
        return mTerms.size();
    }

    public void setTerms(List<Term> terms) {
        mTerms = terms;
        notifyDataSetChanged();
    }

    class TermViewHolder extends RecyclerView.ViewHolder {

        private final TextView termItemView;
        private final TextView termItemStartDate;
        private final TextView termItemEndDate;

        private TermViewHolder(View itemView) {
            super(itemView);

            termItemView      = itemView.findViewById(R.id.termItem1);
            termItemStartDate = itemView.findViewById(R.id.termItemStartDate);
            termItemEndDate   = itemView.findViewById(R.id.termItemEndDate);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                final Term currentTerm = mTerms.get(position);

                Intent intent = new Intent(context, TermDetails.class);
                intent.putExtra("id", currentTerm.getTermId());
                intent.putExtra("title", currentTerm.getTermTitle());
                intent.putExtra("startDate", currentTerm.getTermStartDate());
                intent.putExtra("endDate", currentTerm.getTermEndDate());

                context.startActivity(intent);
            });
        }
    }
}
