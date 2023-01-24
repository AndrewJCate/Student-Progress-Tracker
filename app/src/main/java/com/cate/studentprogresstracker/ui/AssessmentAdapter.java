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

import entities.Assessment;

public class AssessmentAdapter extends RecyclerView.Adapter<AssessmentAdapter.AssessmentViewHolder> {

    private final Context context;
    private final LayoutInflater mInflater;

    private List<Assessment> mAssessments;

    public AssessmentAdapter(Context context) {
        this.context = context;
        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public AssessmentAdapter.AssessmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.assessment_list_item, parent, false);
        return new AssessmentViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AssessmentViewHolder holder, int position) {
        if (mAssessments != null) {
            Assessment currentAssessment = mAssessments.get(position);
            String title = currentAssessment.getAssessmentTitle();

            holder.assessmentItemView.setText(title);
        }
        else {
            holder.assessmentItemView.setText(R.string.no_assessment_name);
        }
    }

    @Override
    public int getItemCount() {
        return mAssessments.size();
    }

    public void setAssessments(List<Assessment> assessments) {
        mAssessments = assessments;
        notifyDataSetChanged();
    }

    class AssessmentViewHolder extends RecyclerView.ViewHolder {

        private final TextView assessmentItemView;
        private final TextView assessmentItemType;
        private final TextView assessmentItemStartDate;
        private final TextView assessmentItemEndDate;

        private AssessmentViewHolder(View itemView) {
            super(itemView);

            assessmentItemView      = itemView.findViewById(R.id.assessmentItemTitle);
            assessmentItemType      = itemView.findViewById(R.id.assessmentItemType);
            assessmentItemStartDate = itemView.findViewById(R.id.assessmentItemStartDate);
            assessmentItemEndDate   = itemView.findViewById(R.id.assessmentItemEndDate);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    final Assessment currentAssessment = mAssessments.get(position);

                    Intent intent = new Intent(context, AssessmentDetails.class);
                    intent.putExtra("id", currentAssessment.getAssessmentId());
                    intent.putExtra("title", currentAssessment.getAssessmentTitle());
                    intent.putExtra("type", currentAssessment.getAssessmentType());
                    intent.putExtra("startDate", currentAssessment.getAssessmentStartDate());
                    intent.putExtra("endDate", currentAssessment.getAssessmentEndDate());
                    intent.putExtra("courseId", currentAssessment.getCourseId());

                    context.startActivity(intent);
                }
            });
        }
    }
}
