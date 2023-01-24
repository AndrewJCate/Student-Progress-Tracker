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

import entities.Course;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> {

    private final Context context;
    private final LayoutInflater mInflater;

    private List<Course> mCourses;

    public CourseAdapter(Context context) {
        this.context = context;
        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public CourseAdapter.CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.course_list_item, parent, false);
        return new CourseViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseAdapter.CourseViewHolder holder, int position) {
        if (mCourses != null) {
            Course currentCourse = mCourses.get(position);
            String title         = currentCourse.getCourseTitle();
            String startDate     = currentCourse.getCourseStartDate();
            String endDate       = currentCourse.getCourseEndDate();

            holder.courseItemTitle.setText(title);
            holder.courseItemStartDate.setText(startDate);
            holder.courseItemEndDate.setText(endDate);
        }
        else {
            holder.courseItemTitle.setText(R.string.no_data);
            holder.courseItemStartDate.setText(R.string.no_data);
            holder.courseItemEndDate.setText(R.string.no_data);
        }
    }

    @Override
    public int getItemCount() {
        return mCourses.size();
    }

    public void setCourses(List<Course> courses) {
        mCourses = courses;
        notifyDataSetChanged();
    }

    class CourseViewHolder extends RecyclerView.ViewHolder {

        private final TextView courseItemEndDate;
        private final TextView courseItemStartDate;
        private final TextView courseItemTitle;

        private CourseViewHolder(View itemView) {
            super(itemView);

            courseItemTitle     = itemView.findViewById(R.id.courseItemTitle);
            courseItemStartDate = itemView.findViewById(R.id.courseItemStartDate);
            courseItemEndDate   = itemView.findViewById(R.id.courseItemEndDate);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    final Course currentCourse = mCourses.get(position);

                    Intent intent = new Intent(context, CourseDetails.class);
                    intent.putExtra("id", currentCourse.getCourseId());
                    intent.putExtra("title", currentCourse.getCourseTitle());
                    intent.putExtra("startDate", currentCourse.getCourseStartDate());
                    intent.putExtra("endDate", currentCourse.getCourseEndDate());
                    intent.putExtra("status", currentCourse.getCourseStatus());
                    intent.putExtra("instructorLastName", currentCourse.getCourseInstructorLastName());
                    intent.putExtra("instructorFirstName", currentCourse.getCourseInstructorFirstName());
                    intent.putExtra("instructorPhoneNumber", currentCourse.getCourseInstructorPhoneNumber());
                    intent.putExtra("instructorEmail", currentCourse.getCourseInstructorEmail());
                    intent.putExtra("note", currentCourse.getCourseNote());
                    intent.putExtra("termId", currentCourse.getTermId());

                    context.startActivity(intent);
                }
            });
        }
    }
}
