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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class TasksActivity extends AppCompatActivity {
    private static TasksActivity instance;
    private FirebaseAuth mAuth;
    private static FirebaseFirestore firebaseFirestore;
    private LinearLayout btnAdd;
    private Map<String, Object> user;
    private String UID;
    private TextView tvModeDisplay, txtTimer;
    private static List<Map<String, Object>> tasksList;

    private static ArrayList<Task> tasks;
    static RecyclerView tasksRecyclerView;
    public static ConstraintLayout tasksConstraintLayout;
    static TextView txtCoin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        tasks = new ArrayList<>();

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_5_tasks);
        instance = this;
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.tasks), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        LinearLayout btnMenu = findViewById(R.id.btnMenu);
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TasksActivity.this, MenuActivity.class);
                startActivity(i);
            }
        });

        firebaseFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        UID = "YkbW5nnkv1aLDXUvEYxZDMB1oj03";

        tasksConstraintLayout = findViewById(R.id.tasks);
        tasksRecyclerView = findViewById(R.id.tasksRecyclerView);
        tasksList = new ArrayList<>();

        btnAdd = findViewById(R.id.btnAddTask);

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
        fetchTasks(UID);
        getUserBalance(UID);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        instance = null;
    }

    public static TasksActivity getInstance() {
        return instance;
    }

    private void addTaskToUser(String userId, String taskName, Long duration, String taskDate, boolean taskMode) {
        //map for the new task
        Map<String, Object> newTask = new HashMap<>();
        newTask.put("taskName", taskName);
        newTask.put("taskDate", taskDate);
        newTask.put("taskMode", taskMode);
        newTask.put("taskDuration",duration);
        int coins = Integer.parseInt(String.valueOf(duration))/2;

        if(coins == 0) coins = 1;
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

    public static void deleteTaskFromUser(String userId, String taskNameToDelete) {
        DocumentReference userRef = firebaseFirestore.collection("users").document(userId);
        userRef.get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        List<Map<String, Object>> tasks = (List<Map<String, Object>>) documentSnapshot.get("tasks");
                        if (tasks != null) {
                            for (Iterator<Map<String, Object>> iterator = tasks.iterator(); iterator.hasNext();) {
                                Map<String, Object> task = iterator.next();
                                String taskName = (String) task.get("taskName");
                                if (taskName.equals(taskNameToDelete)) {
                                    iterator.remove();
                                    break;
                                }
                            }
                            userRef.update("tasks", tasks)
                                    .addOnSuccessListener(aVoid -> {
                                        Log.d("TAG", "Task deleted successfully");
                                        fetchTasks(userId);
                                    })
                                    .addOnFailureListener(e -> Log.e("TAG", "Error deleting task", e));
                        }
                    } else {
                        Log.d("TAG", "User document does not exist");
                    }
                })
                .addOnFailureListener(e -> Log.e("TAG", "Error fetching tasks", e));
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
                            userRef.update("tasks", tasks)
                                    .addOnSuccessListener(aVoid -> {
                                        Log.d("TAG", "Task updated successfully");
                                        fetchTasks(userId);
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
        getTasks();
        generateTasks();
    }

    public static void getTasks() {
        tasks.clear();
        for (int i = 0; i < tasksList.size(); i++) {
            String taskName = tasksList.get(i).get("taskName").toString();
            String taskDate = (String) tasksList.get(i).get("taskDate");
            Long taskDuration = (Long) tasksList.get(i).get("taskDuration");
            boolean taskMode = (boolean) tasksList.get(i).get("taskMode");
            int taskCoins = (int) tasksList.get(i).get("taskCoins");
            tasks.add(new Task(taskName, taskDate, taskDuration, (taskMode) ? "Focus" : "Chill", taskCoins));
        }
    }

    public static void generateTasks() {
        getTasks();
        RecyclerViewAdapterTask adapterTask = new RecyclerViewAdapterTask(getInstance(), tasks);
        tasksRecyclerView.setAdapter(adapterTask);
        tasksRecyclerView.setLayoutManager(new LinearLayoutManager(getInstance()));
    }

    public void getUserBalance(String userID) {
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        DocumentReference userRef = firebaseFirestore.collection("users").document(userID);

        userRef.get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        userCoins = documentSnapshot.getLong("coins");
                        if (userCoins != null) {
                            updateCoinText();
                            Log.d("TAG", "User has " + userCoins + " coins.");
                        } else {
                            Log.d("TAG", "Coins field is not found in the document.");
                        }
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
