package com.example.firebaseconnection;

import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class RecyclerViewAdapterTask extends RecyclerView.Adapter<RecyclerViewAdapterTask.MyViewHolder> {
    FirebaseFirestore firebaseFirestore;
    Context context;
    ArrayList<Task> tasks;

    public RecyclerViewAdapterTask(Context context, ArrayList<Task> tasks) {
        this.context = context;
        this.tasks = tasks;
    }

    @NonNull
    @Override
    public RecyclerViewAdapterTask.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_view_task, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapterTask.MyViewHolder holder, int position) {
        holder.name.setText(tasks.get(position).name);
        holder.date.setText(tasks.get(position).date);
        holder.duration.setText(String.valueOf(tasks.get(position).duration));
        holder.mode.setText(tasks.get(position).mode);
        holder.reward.setText(String.valueOf(tasks.get(position).reward));

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = LayoutInflater.from(holder.btnDelete.getContext());
                View popupView = inflater.inflate(R.layout.popup_delete_task, null);

                int width = ViewGroup.LayoutParams.MATCH_PARENT;
                int height = ViewGroup.LayoutParams.WRAP_CONTENT;
                PopupWindow deleteTaskPopUp = new PopupWindow(popupView, width, height, true);

                deleteTaskPopUp.showAtLocation(holder.btnDelete.getRootView(), Gravity.CENTER_VERTICAL, 0, 0);

                LinearLayout btnYes = popupView.findViewById(R.id.btnDeleteYes);
                LinearLayout btnNo = popupView.findViewById(R.id.btnDeleteNo);

                btnNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteTaskPopUp.dismiss();
                    }
                });
                btnYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TasksActivity.deleteTaskFromUser(SignInActivity.UID, holder.name.getText().toString());
                        deleteTaskPopUp.dismiss();
                    }
                });
            }
        });

        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = LayoutInflater.from(holder.btnEdit.getContext());
                View popupView = inflater.inflate(R.layout.popup_edit_task, null);

                int width = ViewGroup.LayoutParams.MATCH_PARENT;
                int height = ViewGroup.LayoutParams.WRAP_CONTENT;
                PopupWindow editTaskPopup = new PopupWindow(popupView, width, height, true);

                editTaskPopup.showAtLocation(holder.btnEdit.getRootView(), Gravity.CENTER_VERTICAL, 0, 0);

                EditText etTaskTitle, etTaskDate,etTaskDuration;
                ToggleButton tbTaskMode = popupView.findViewById(R.id.toggleButtonTaskMode);
                etTaskTitle = popupView.findViewById(R.id.editTextTaskTitle);
                etTaskDate = popupView.findViewById(R.id.editTextDate);
                etTaskDuration = popupView.findViewById(R.id.editTextTime);

                etTaskTitle.setText(holder.name.getText().toString());
                etTaskDate.setText(holder.date.getText().toString());
                etTaskDuration.setText(holder.duration.getText().toString());
                tbTaskMode.setChecked(holder.mode.getText().toString().equals("Focus"));
                ImageView btnClosePopup = popupView.findViewById(R.id.btnClosePopup);
                btnClosePopup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        editTaskPopup.dismiss();
                    }
                });

                LinearLayout btnEditSave = popupView.findViewById(R.id.btnEditSave);
                btnEditSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        firebaseFirestore = FirebaseFirestore.getInstance();
                        String UID = SignInActivity.UID;
                        String newTaskName = etTaskTitle.getText().toString();
                        String newTaskDate = etTaskDate.getText().toString();
                        Long newDuration = Long.parseLong(etTaskDuration.getText().toString());
                        boolean newTaskMode = tbTaskMode.isChecked();
                        TasksActivity.updateTaskInUser(UID, holder.name.getText().toString(), newTaskName, newDuration, newTaskDate,newTaskMode);
                        editTaskPopup.dismiss();
                        TasksActivity.fetchTasks(UID);
                    }
                });
            }
        });

        holder.btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = LayoutInflater.from(holder.btnEdit.getContext());
                View popupView = inflater.inflate(R.layout.popup_start_task, null);

                int width = ViewGroup.LayoutParams.MATCH_PARENT;
                int height = ViewGroup.LayoutParams.WRAP_CONTENT;
                PopupWindow startTaskPopup = new PopupWindow(popupView, width, height, true);

                startTaskPopup.showAtLocation(holder.btnEdit.getRootView(), Gravity.CENTER_VERTICAL, 0, 0);

                LinearLayout btnYes = popupView.findViewById(R.id.btnYes);
                LinearLayout btnNo = popupView.findViewById(R.id.btnNo);
                TextView txtStartingTask = popupView.findViewById(R.id.txtStartingTask);
                txtStartingTask.setText("Start " + holder.name.getText().toString() + "?");

                btnYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(holder.btnStart.getContext(), TimerActivity.class);
                        String taskModeIntent = holder.mode.getText().toString();
                        String taskNameIntent = holder.name.getText().toString();
                        String taskRewardIntent = holder.reward.toString();

                        intent.putExtra("taskNameIntent", taskNameIntent);
                        intent.putExtra("taskModeIntent", taskModeIntent);
                        intent.putExtra("taskRewardIntent", taskRewardIntent);
                        intent.putExtra("taskDurationIntent", holder.duration.getText().toString());
                        holder.btnStart.getContext().startActivity(intent);
                        startTaskPopup.dismiss();
                    }
                });

                btnNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startTaskPopup.dismiss();
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView date;
        TextView duration;
        TextView mode;
        TextView reward;
        LinearLayout btnDelete;
        LinearLayout btnEdit;
        LinearLayout btnStart;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.taskName);
            date = itemView.findViewById(R.id.taskDate);
            duration = itemView.findViewById(R.id.taskDuration);
            mode = itemView.findViewById(R.id.taskMode);
            reward = itemView.findViewById(R.id.taskReward);

            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnStart = itemView.findViewById(R.id.btnStart);
        }
    }
}
