package com.example.firebaseconnection;

import static com.example.firebaseconnection.ProfileActivity.userCoins;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class TaskListActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private static FirebaseFirestore firebaseFirestore;
    private TextView TaskName, TaskDate, TaskMode, TaskCoins;
    private LinearLayout btnAdd, btnEdit, btnDelete;
    private Map<String, Object> user;
    private String UID;

    private static List<Map<String, Object>> tasksList;

    private static ArrayList<Task> tasks;

    public static ConstraintLayout tasksConstraintLayout;

    public static LinearLayout tasksLinearLayout;

    static  TextView txtCoin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //methods:
        //create task (addTaskToUser), read task (fetchTasks),
        //update task (updateTaskInUser), delete task (deleteTaskFromUser)
        tasks = new ArrayList<>();

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_task_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.tasks), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        LinearLayout btnMenu = findViewById(R.id.btnMenu);
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TaskListActivity.this, Menu.class);
                startActivity(i);
            }
        });

        tasksConstraintLayout = findViewById(R.id.tasks);

        btnAdd = findViewById(R.id.btnAddTask);

        firebaseFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        UID = "YkbW5nnkv1aLDXUvEYxZDMB1oj03";


        tasksLinearLayout = findViewById(R.id.tasksLinearLayout);
        generateTasks();

        tasksList = new ArrayList<>();

        btnAdd.setOnClickListener(v -> {
            LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            View popupView = inflater.inflate(R.layout.popup_create_task, null);

            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            PopupWindow createTaskPopup = new PopupWindow(popupView, width, height, true);

            createTaskPopup.showAtLocation(findViewById(R.id.tasks), Gravity.CENTER_VERTICAL, 0, 0);
            EditText etTaskTitle, etTaskDate, etDuration;
            ToggleButton tbTaskMode;

            etTaskTitle = popupView.findViewById(R.id.editTextMode);
            etTaskDate = popupView.findViewById(R.id.editTextTaskDate);
            etDuration = popupView.findViewById(R.id.editTextTime);
            tbTaskMode = popupView.findViewById(R.id.toggleMode);

            LinearLayout create_task = popupView.findViewById(R.id.createTaskBtn);
            create_task.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String taskTitle, taskDateStr,taskMode;

                    Long taskDuration;

                    taskTitle = etTaskTitle.getText().toString();
                    taskDateStr = etTaskDate.getText().toString();
                    taskDuration = Long.valueOf(etDuration.getText().toString());
                    taskMode = tbTaskMode.isChecked()?"Focus":"Chill";

                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                    Date date = null;
                    try {
                        date = dateFormat.parse(taskDateStr);
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }

                    // Create a Timestamp object from the parsed Date object
                    addTaskToUser(UID,taskTitle,taskDuration, taskDateStr, tbTaskMode.isChecked());
                    fetchTasks(UID);
                    createTaskPopup.dismiss();
                }
            });




        });

//        btnEdit.setOnClickListener(v->{
//            LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
//            View popupView = inflater.inflate(R.layout.popup_edit_task, null);
//
//            int width = ViewGroup.LayoutParams.MATCH_PARENT;
//            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
//            PopupWindow editTaskPopUp = new PopupWindow(popupView, width, height, true);
//
//            editTaskPopUp.showAtLocation(findViewById(R.id.tasks), Gravity.CENTER_VERTICAL, 0, 0);
//
//
//            EditText etTaskTitle, etTaskDate,etTaskDuration;
//            ToggleButton tbTaskMode = findViewById(R.id.toggleButtonTaskMode);
//            etTaskTitle = findViewById(R.id.editTextTaskTitle);
//
//            etTaskDate = findViewById(R.id.editTextDate);
//            etTaskDuration = findViewById(R.id.editTextTime);
//
//
//
//            LinearLayout btnSave = findViewById(R.id.btnEditSave);
//            btnSave.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//                    Date date = null;
//                    try {
//                        date = dateFormat.parse(etTaskDate.getText().toString());
//                    } catch (ParseException e) {
//                        throw new RuntimeException(e);
//                    }
//
//                    // Create a Timestamp object from the parsed Date object
//                    Timestamp newTaskDate = new Timestamp(Instant.ofEpochSecond(date.getTime()));
//
//
//                    String oldTaskName = "Scooby Doo PAPA";
//
//                    String newTaskName = etTaskTitle.getText().toString();
//                    String taskDuration = etTaskDuration.getText().toString();
//                    String newTaskMode = tbTaskMode.isChecked()?"Focus":"Chill";
//                    updateTaskInUser(UID, oldTaskName, newTaskName, taskDuration,newTaskDate, newTaskMode);
//
//                }
//            });
//        });
//
//        btnDelete.setOnClickListener(v->{
//            LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
//            View popupView = inflater.inflate(R.layout.popup_delete_task, null);
//
//            int width = ViewGroup.LayoutParams.MATCH_PARENT;
//            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
//            PopupWindow deleteTaskPopUp = new PopupWindow(popupView, width, height, true);
//
//            deleteTaskPopUp.showAtLocation(findViewById(R.id.tasks), Gravity.CENTER_VERTICAL, 0, 0);
//
//            LinearLayout btnYes, btnNo;
//            btnYes = findViewById(R.id.btnDeleteYes);
//            btnNo = findViewById(R.id.btnDeleteNo);
//
//            btnYes.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    String taskNameToDelete = "Updated Task Name";
//                    deleteTaskFromUser(UID, taskNameToDelete);
//                }
//            });
//
//            btnNo.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    deleteTaskPopUp.dismiss();
//                }
//            });
//
//
//        });

        fetchTasks(UID);

        getUserBalance(UID);



    }

    private void addTaskToUser(String userId, String taskName, Long duration, String taskDate, boolean taskMode) {
        //map for the new task
        Map<String, Object> newTask = new HashMap<>();
        newTask.put("taskName", taskName);
        newTask.put("taskDate", taskDate);
        newTask.put("taskMode", taskMode);
        newTask.put("taskDuration",duration);
        int coins = Integer.parseInt(String.valueOf(duration))/2;
        newTask.put("taskCoins", coins);
        newTask.put("taskIsDone",false);

        DocumentReference userRef = firebaseFirestore.collection("users").document(userId);

        userRef.update("tasks", FieldValue.arrayUnion(newTask))
                .addOnSuccessListener(aVoid -> {
                    Log.d("TAG", "Task added successfully");
                })
                .addOnFailureListener(e -> {
                    Log.e("TAG", "Error adding task", e);
                });
    }

    public static void fetchTasks(String userId) {
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
                                String taskDate = (String) task.get("taskDate");
                                boolean taskMode = (boolean) task.get("taskMode");
                                int taskCoins = ((Long) task.get("taskCoins")).intValue();
                                boolean taskIsDone = ((boolean) task.get("taskIsDone"));
                                Long taskDuration = (Long) task.get("taskDuration");

                                //create a new map to hold the task details
                                Map<String, Object> taskMap = new HashMap<>();
                                taskMap.put("taskName", taskName);
                                taskMap.put("taskDate", taskDate);
                                taskMap.put("taskMode", taskMode);
                                taskMap.put("taskCoins", taskCoins);
                                taskMap.put("taskIsDone", taskIsDone);
                                taskMap.put("taskDuration", taskDuration);

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

    public static void updateTaskInUser(String userId, String oldTaskName, String newTaskName, Long duration, String newTaskDate, boolean newTaskMode) {
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
                                    int newTaskCoins = (int) (duration/2);
                                    task.put("taskDuration",duration);
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


    private static void updateUIWithTasks() {
//        if (!tasksList.isEmpty()) {
//            TaskName.setText(Objects.requireNonNull(tasksList.get().get("taskName")).toString());
//            Log.i("TAG", "SUCCESS");
//        } else {
//            Log.i("TAG", "EMPTY LIST");
//            TaskName.setText("wa uie");
//        }
//        Log.i("TAG", Integer.toString(tasksList.size()));

        getTasks();
        generateTasks();
    }

    public static void deleteTaskFromUser(String userId, String taskNameToDelete) {

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

    public static void getTasks() {
        tasks.clear();
        for (int i = 0; i < tasksList.size(); i++) {
            String taskName = tasksList.get(i).get("taskName").toString();
            boolean taskMode = (boolean) tasksList.get(i).get("taskMode");
            Long taskDuration = (Long) tasksList.get(i).get("taskDuration");
            int taskCoins = (int) tasksList.get(i).get("taskCoins");
            String taskDate = (String) tasksList.get(i).get("taskDate");
            tasks.add(new Task(taskName, taskMode, taskDuration, taskCoins, taskDate));
        }
    }

    public static void generateTasks() {
        tasksLinearLayout.removeAllViews();

        for (Task task : tasks) {
            tasksLinearLayout.addView(task.generate(tasksLinearLayout.getContext(), tasksConstraintLayout));
        }
    }

    public void getUserBalance(String userID) {
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        DocumentReference userRef = firebaseFirestore.collection("users").document(userID);

        userRef.get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        //get the "coins" field from the document

                        userCoins = documentSnapshot.getLong("coins");
                        if (userCoins != null) {
                            updateCoinText();
                            Log.d("TAG", "User has " + userCoins + " coins.");
                        } else {
                            Log.d("TAG", "Coins field is not found in the document.");
                        }
                        //update



                    } else {
                        Log.d("TAG", "User document does not exist");
                    }
                })
                .addOnFailureListener(e -> Log.e("TAG", "Error fetching user document", e));
    }

    private void updateCoinText(){
        txtCoin =  findViewById(R.id.txtCoinBalance);
        txtCoin.setText(String.valueOf(userCoins));
    }
}
