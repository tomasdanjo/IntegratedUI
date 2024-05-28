package com.example.firebaseconnection;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.firebaseconnection.R;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class TaskListActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;
    private TextView tvTaskName, tvTaskDate, tvTaskMode, tvTaskCoins;
    private Button btnAdd, btnEdit, btnDelete, btnRedirectToShop;
    private Map<String, Object> user;
    private String UID;

    private List<Map<String, Object>> tasksList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //methods:
        //create task (addTaskToUser), read task (fetchTasks),
        //update task (updateTaskInUser), delete task (deleteTaskFromUser)

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_task_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        firebaseFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        UID = mAuth.getCurrentUser().getUid();


        tvTaskName = findViewById(R.id.tvTaskName);
        tvTaskDate = findViewById(R.id.tvTaskDate);
        tvTaskMode = findViewById(R.id.tvTaskMode);
        tvTaskCoins = findViewById(R.id.tvTaskCoins);
        btnAdd = findViewById(R.id.btnAdd);
        btnEdit = findViewById(R.id.btnEdit);
        btnDelete = findViewById(R.id.btnDelete);
        btnRedirectToShop = findViewById(R.id.btnRedirectToShop);


        tasksList = new ArrayList<>();

        btnAdd.setOnClickListener(v -> {
            Timestamp taskDate = Timestamp.now();
            Log.d("TAG", "HERE1");
            Log.d("TAG", UID);
            addTaskToUser(UID, "Mhm", taskDate, true, 8);
            Log.d("TAG", "HERE2");

        });

        btnEdit.setOnClickListener(v->{

            String oldTaskName = "Scooby Doo PAPA";
            String newTaskName = "Updated Task Name";
            Timestamp newTaskDate = Timestamp.now();
            boolean newTaskMode = false;
            int newTaskCoins = 10;
            updateTaskInUser(UID, oldTaskName, newTaskName, newTaskDate, newTaskMode, newTaskCoins);
        });

        btnDelete.setOnClickListener(v->{
            String taskNameToDelete = "Updated Task Name";
            deleteTaskFromUser(UID, taskNameToDelete);
        });

        btnRedirectToShop.setOnClickListener(v->{
            Intent intent = new Intent(TaskListActivity.this, CatShopActivity.class);
            startActivity(intent);
        });

        fetchTasks(UID);

    }

    private void addTaskToUser(String userId, String taskName, Timestamp taskDate, boolean taskMode, int taskCoins) {
        //map for the new task
        Map<String, Object> newTask = new HashMap<>();
        newTask.put("taskName", taskName);
        newTask.put("taskDate", taskDate);
        newTask.put("taskMode", taskMode);
        newTask.put("taskCoins", taskCoins);

        DocumentReference userRef = firebaseFirestore.collection("users").document(userId);

        userRef.update("tasks", FieldValue.arrayUnion(newTask))
                .addOnSuccessListener(aVoid -> {
                    Log.d("TAG", "Task added successfully");
                })
                .addOnFailureListener(e -> {
                    Log.e("TAG", "Error adding task", e);
                });
    }

    private void fetchTasks(String userId) {
        //user document reference
        DocumentReference userRef = firebaseFirestore.collection("users").document(userId);
        //user document fetch
        userRef.get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        //retrieve the tasks array from the document
                        List<Map<String, Object>> tasks = (List<Map<String, Object>>) documentSnapshot.get("tasks");
                        if (tasks != null) {
                            tasksList.clear();
                            for (Map<String, Object> task : tasks) {
                                //get the task details
                                String taskName = (String) task.get("taskName");
                                Timestamp taskDate = (Timestamp) task.get("taskDate");
                                boolean taskMode = (boolean) task.get("taskMode");
                                int taskCoins = ((Long) task.get("taskCoins")).intValue();

                                //create a new map to hold the task details
                                Map<String, Object> taskMap = new HashMap<>();
                                taskMap.put("taskName", taskName);
                                taskMap.put("taskDate", taskDate);
                                taskMap.put("taskMode", taskMode);
                                taskMap.put("taskCoins", taskCoins);

                                //add the task map to the tasksList
                                tasksList.add(taskMap);

                                Log.i("TAG", "Size " + tasksList.size());
                                Log.d("TAG", "Task Name: " + taskName);
                                Log.d("TAG", "Task Date: " + taskDate);
                                Log.d("TAG", "Task Mode: " + taskMode);
                                Log.d("TAG", "Task Coins: " + taskCoins);
                            }
                            updateUIWithTasks();
                        } else {
                            Log.d("TAG", "No tasks found");
                        }
                    } else {
                        Log.d("TAG", "User document does not exist");
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("TAG", "Error fetching tasks", e);
                    // Handle the error here
                });
    }

    private void updateTaskInUser(String userId, String oldTaskName, String newTaskName, Timestamp newTaskDate, boolean newTaskMode, int newTaskCoins) {
        DocumentReference userRef = firebaseFirestore.collection("users").document(userId);

        userRef.get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        List<Map<String, Object>> tasks = (List<Map<String, Object>>) documentSnapshot.get("tasks");
                        if (tasks != null) {
                            //find the task to update (or have a iD sumn for direct find)
                            for (Map<String, Object> task : tasks) {
                                String taskName = (String) task.get("taskName");
                                if (taskName.equals(oldTaskName)) {
                                    //update the task details
                                    task.put("taskName", newTaskName);
                                    task.put("taskDate", newTaskDate);
                                    task.put("taskMode", newTaskMode);
                                    task.put("taskCoins", newTaskCoins);
                                    break;
                                }
                            }
                            //update the user document with the modified tasks list
                            userRef.update("tasks", tasks)
                                    .addOnSuccessListener(aVoid -> {
                                        Log.d("TAG", "Task updated successfully");
                                        fetchTasks(userId); // Re-fetch tasks to update local list and UI
                                    })
                                    .addOnFailureListener(e -> Log.e("TAG", "Error updating task", e));
                        }
                    } else {
                        Log.d("TAG", "User document does not exist");
                    }
                })
                .addOnFailureListener(e -> Log.e("TAG", "Error fetching tasks", e));
    }


    private void updateUIWithTasks() {
        if (!tasksList.isEmpty()) {
            tvTaskName.setText(Objects.requireNonNull(tasksList.get(0).get("taskName")).toString());
            Log.i("TAG", "SUCCESS");
        } else {
            Log.i("TAG", "EMPTY LIST");
            tvTaskName.setText("wa uie");
        }
        Log.i("TAG", Integer.toString(tasksList.size()));
    }

    private void deleteTaskFromUser(String userId, String taskNameToDelete) {

        DocumentReference userRef = firebaseFirestore.collection("users").document(userId);

        userRef.get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        List<Map<String, Object>> tasks = (List<Map<String, Object>>) documentSnapshot.get("tasks");
                        if (tasks != null) {
                            // Find the task to delete
                            for (Iterator<Map<String, Object>> iterator = tasks.iterator(); iterator.hasNext();) {
                                Map<String, Object> task = iterator.next();
                                String taskName = (String) task.get("taskName");
                                if (taskName.equals(taskNameToDelete)) {
                                    //remove the task from the list
                                    iterator.remove();
                                    break;
                                }
                            }
                            userRef.update("tasks", tasks)
                                    .addOnSuccessListener(aVoid -> {
                                        Log.d("TAG", "Task deleted successfully");
                                        fetchTasks(userId); // Re-fetch tasks to update local list and UI
                                    })
                                    .addOnFailureListener(e -> Log.e("TAG", "Error deleting task", e));
                        }
                    } else {
                        Log.d("TAG", "User document does not exist");
                    }
                })
                .addOnFailureListener(e -> Log.e("TAG", "Error fetching tasks", e));
    }



}
