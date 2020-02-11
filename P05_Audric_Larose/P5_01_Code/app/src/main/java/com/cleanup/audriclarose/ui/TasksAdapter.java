package com.cleanup.audriclarose.ui;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageView;

import com.cleanup.audriclarose.R;
import com.cleanup.audriclarose.model.Project;
import com.cleanup.audriclarose.model.Task;

import java.util.List;


public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.TaskViewHolder> {
    private RelativeLayout item_en_particulier;
    @NonNull
    private List<Task> tasks;
    private OnItemclick clicketit;

    @NonNull
    private final DeleteTaskListener deleteTaskListener;

    public interface OnItemclick{
        void clickclick(int position);
    }

    TasksAdapter(@NonNull final List<Task> tasks, @NonNull final DeleteTaskListener deleteTaskListener, @NonNull final OnItemclick onItemclick) {
        this.tasks = tasks;
        this.deleteTaskListener = deleteTaskListener;
        this.clicketit= onItemclick;
    }

    void updateTasks(@NonNull final List<Task> tasks) {
        this.tasks = tasks;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_task, viewGroup, false);
        TaskViewHolder taskViewHolder = new TaskViewHolder(view, deleteTaskListener, clicketit);
        return taskViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder taskViewHolder, int position) {
        taskViewHolder.bind(tasks.get(position));
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public Task getPositionofTask(int position){
        return tasks.get(position);
    }

    public interface DeleteTaskListener {

        void onDeleteTask(Task task);
    }

    class TaskViewHolder extends RecyclerView.ViewHolder {

        private final AppCompatImageView imgProject;
        private final TextView lblTaskName;
        private final TextView lblProjectName;
        private final AppCompatImageView imgDelete;
        private final DeleteTaskListener deleteTaskListener;
        public RelativeLayout item_en_particulier;

        TaskViewHolder(@NonNull final View itemView, @NonNull DeleteTaskListener deleteTaskListener, final OnItemclick listener) {
            super(itemView);

            this.deleteTaskListener = deleteTaskListener;
            imgProject = itemView.findViewById(R.id.img_project);
            lblTaskName = itemView.findViewById(R.id.lbl_task_name);
            lblProjectName = itemView.findViewById(R.id.lbl_project_name);
            item_en_particulier= itemView.findViewById(R.id.item_particulier);
            View.OnClickListener l = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.clickclick(position);
                        }
                    }

                }
            };
            itemView.setOnClickListener(l);
            imgDelete = itemView.findViewById(R.id.img_delete);
            imgDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final Object tag = view.getTag();
                    if (tag instanceof Task) {
                        TaskViewHolder.this.deleteTaskListener.onDeleteTask((Task) tag);
                    }
                }
            });

        }

        @SuppressLint("RestrictedApi")
        void bind(Task task) {
            lblTaskName.setText(task.getName());
            imgDelete.setTag(task);
            final Project taskProject = task.getProject();
            if (taskProject != null) {
                imgProject.setSupportImageTintList(ColorStateList.valueOf(taskProject.getColor()));
                lblProjectName.setText(taskProject.getName());
            } else {
                imgProject.setVisibility(View.INVISIBLE);
                lblProjectName.setText("pas de projet");
            }

        }
    }
}
