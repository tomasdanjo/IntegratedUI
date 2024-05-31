package com.example.firebaseconnection;

import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class RecyclerViewAdapterTask extends RecyclerView.Adapter<RecyclerViewAdapterTask.MyViewHolder> {
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
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        FirebaseFirestore firebaseFirestore;
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

            name = itemView.findViewById(R.id.name);
            date = itemView.findViewById(R.id.date);
            duration = itemView.findViewById(R.id.duration);
            mode = itemView.findViewById(R.id.mode);
            reward = itemView.findViewById(R.id.reward);

            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnStart = itemView.findViewById(R.id.btnStart);
            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LayoutInflater inflater = LayoutInflater.from(btnDelete.getContext());
                    View popupView = inflater.inflate(R.layout.popup_delete_task, null);

                    int width = ViewGroup.LayoutParams.MATCH_PARENT;
                    int height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    PopupWindow deleteTaskPopUp = new PopupWindow(popupView, width, height, true);

                    deleteTaskPopUp.showAtLocation(btnDelete.getRootView(), Gravity.CENTER_VERTICAL, 0, 0);

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
                            TasksActivity.deleteTaskFromUser("YkbW5nnkv1aLDXUvEYxZDMB1oj03", name.getText().toString());
                            deleteTaskPopUp.dismiss();
                        }
                    });
                }
            });

            btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LayoutInflater inflater = LayoutInflater.from(btnEdit.getContext());
                    View popupView = inflater.inflate(R.layout.popup_edit_task, null);

                    int width = ViewGroup.LayoutParams.MATCH_PARENT;
                    int height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    PopupWindow editTaskPopup = new PopupWindow(popupView, width, height, true);

                    editTaskPopup.showAtLocation(btnEdit.getRootView(), Gravity.CENTER_VERTICAL, 0, 0);

                    EditText etTaskTitle, etTaskDate,etTaskDuration;
                    ToggleButton tbTaskMode = popupView.findViewById(R.id.toggleButtonTaskMode);
                    etTaskTitle = popupView.findViewById(R.id.editTextTaskTitle);
                    etTaskDate = popupView.findViewById(R.id.editTextDate);
                    etTaskDuration = popupView.findViewById(R.id.editTextTime);

                    etTaskTitle.setText(name.getText().toString());
                    etTaskDate.setText(date.getText().toString());
                    etTaskDuration.setText(duration.getText().toString());
                    tbTaskMode.setChecked(mode.getText().toString().equals("Focus"));

                    LinearLayout btnEditSave = popupView.findViewById(R.id.btnEditSave);
                    btnEditSave.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            firebaseFirestore = FirebaseFirestore.getInstance();
                            String UID = "YkbW5nnkv1aLDXUvEYxZDMB1oj03";
                            String newTaskName = etTaskTitle.getText().toString();
                            String newTaskDate = etTaskDate.getText().toString();
                            Long newDuration = Long.parseLong(etTaskDuration.getText().toString());
                            boolean newTaskMode = tbTaskMode.isChecked();
                            TasksActivity.updateTaskInUser(UID, name.getText().toString(), newTaskName, newDuration, newTaskDate,newTaskMode);
                            editTaskPopup.dismiss();
                            TasksActivity.fetchTasks(UID);
                        }
                    });
                }
            });

            btnStart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(btnStart.getContext(), TimerActivity.class);
                    String taskModeIntent = mode.getText().toString();

                    intent.putExtra("taskModeIntent", taskModeIntent);
                    intent.putExtra("taskDurationIntent", duration.getText().toString());
                    btnStart.getContext().startActivity(intent);
                }
            });
        }
    }
}
