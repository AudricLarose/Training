package com.cleanup.audriclarose.ui;


import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import 	androidx.recyclerview.widget.ItemTouchHelper.Callback;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.cleanup.audriclarose.R;
import com.cleanup.audriclarose.model.Project;
import com.cleanup.audriclarose.model.Task;
import com.cleanup.audriclarose.ui.TaskViewModel;
import com.cleanup.audriclarose.ui.TasksAdapter;



import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TooManyListenersException;

import static android.provider.Settings.System.getString;

/**
 * <p>Home activity of the application which is displayed when the user opens the app.</p>
 * <p>Displays the list of tasks.</p>
 *
 * @author Gaëtan HERFRAY
 */
public class MainActivity extends AppCompatActivity implements TasksAdapter.DeleteTaskListener, TasksAdapter.OnItemclick {
    private static final String TAG = "MainActivity";

    private final Project[] allProjects = Project.getAllProjects();
    @NonNull
    private final ArrayList<Task> tasks = new ArrayList<>();
    private List<Task> tache;

    private TaskViewModel taskViewModel;
    private Task theTask;
    private final TasksAdapter adapter = new TasksAdapter(tasks, this, this);
    @NonNull
    private SortMethod sortMethod = SortMethod.NONE;
    @Nullable
    public AlertDialog dialog = null;
    @Nullable
    private EditText dialogEditText = null;
    @Nullable
    private Spinner dialogSpinner = null;
    @NonNull
    private RecyclerView listTasks;
    @NonNull
    private TextView lblNoTasks;
    private TasksAdapter.TaskViewHolder itemss;

    @Override
    protected void onResume() {
        adapter.notifyDataSetChanged();
        super.onResume();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        taskViewModel=ViewModelProviders.of(this).get(TaskViewModel.class);
        listTasks = findViewById(R.id.list_tasks);
        lblNoTasks = findViewById(R.id.lbl_no_task);
        listTasks.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        listTasks.setAdapter(adapter);
        findViewById(R.id.fab_add_task).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddTaskDialog();
            }
        });
        taskViewModel.SelectAllThosedatas().observe(this, new Observer<List<Task>>() {
            @Override
            public void onChanged( List<Task> task) {
                Log.d(TAG, "onChanged: "+ task);
                adapter.updateTasks(task);
                tache=task;
                updateTasks();
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                taskViewModel.deleteThisData(adapter.getPositionofTask(viewHolder.getAdapterPosition()));
                Toast.makeText(getApplicationContext(), "effacé !", Toast.LENGTH_LONG).show();

            }
        }).attachToRecyclerView(listTasks);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.filter_alphabetical) {
            Log.d(TAG, "onOptionsItemSelected: trie activé" );
            sortMethod = SortMethod.ALPHABETICAL;
        } else if (id == R.id.filter_alphabetical_inverted) {
            sortMethod = SortMethod.ALPHABETICAL_INVERTED;
        } else if (id == R.id.filter_oldest_first) {
            sortMethod = SortMethod.OLD_FIRST;
        } else if (id == R.id.filter_recent_first) {
            sortMethod = SortMethod.RECENT_FIRST;
        } else if (id == R.id.deleteAll) {
            taskViewModel.deleteAlldata();
        }
        updateTasks();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDeleteTask(Task task) {
        //todo : effacer
        tasks.remove(task);
        taskViewModel.deleteThisData(task);
        updateTasks();
    }
    /**
     * Called when the user clicks on the positive button of the Create Task Dialog.
     *
     * @param dialogInterface the current displayed dialog
     */
    private void onPositiveButtonClick(DialogInterface dialogInterface) {
        // If dialog is open
        if (dialogEditText != null && dialogSpinner != null) {
            // Get the name of the task
            String taskName = dialogEditText.getText().toString();

            // Get the selected project to be associated to the task
            Project taskProject = null;
            if (dialogSpinner.getSelectedItem() instanceof Project) {
                taskProject = (Project) dialogSpinner.getSelectedItem();
            }

            // If a name has not been set
            if (taskName.trim().isEmpty()) {
//                dialogEditText.setError(getString(R.string.empty_task_name));
            }
            // If both project and name of the task have been set
            else if (taskProject != null) {
                // TODO: Replace this by id of persisted task

                Task task = new Task(
                        taskProject.getId(),
                        taskName,
                        new Date().getTime()
                );
                addTask(task);

                dialogInterface.dismiss();
            }
            // If name has been set, but project has not been set (this should never occur)
            else{
                dialogInterface.dismiss();
            }
        }
        // If dialog is already closed
        else {
            dialogInterface.dismiss();
        }
    }

    /**
     * Shows the Dialog for adding a Task
     */
    private void showAddTaskDialog() {
        final AlertDialog dialog = getAddTaskDialog();

        dialog.show();

        dialogEditText = dialog.findViewById(R.id.txt_task_name);
        dialogSpinner = dialog.findViewById(R.id.project_spinner);

        populateDialogSpinner();
    }

    /**
     * Adds the given task to the list of created tasks.
     *
     * @param task the task to be added to the list
     */
    private void addTask(@NonNull Task task) {
        //todo : ajouter sql
        taskViewModel.InsertThisData(task);
        updateTasks();
    }

    /**
     * Updates the list of tasks in the UI
     */

    //todo ici
    private void updateTasks() {
        if (tache.size() == 0) {
            lblNoTasks.setVisibility(View.VISIBLE);
            listTasks.setVisibility(View.GONE);
        } else {
               lblNoTasks.setVisibility(View.GONE);
             listTasks.setVisibility(View.VISIBLE);
            switch (sortMethod) {
                case ALPHABETICAL:
                    Log.d(TAG,     "updateTasks: alpha");
                    Collections.sort(tache, new Task.TaskAZComparator());
                    break;
                case ALPHABETICAL_INVERTED:
                    Collections.sort(tache, new Task.TaskZAComparator());
                    break;
                case RECENT_FIRST:
                    Collections.sort(tache, new Task.TaskRecentComparator());
                    break;
                case OLD_FIRST:
                    Collections.sort(tache , new Task.TaskOldComparator());
                    break;
            }

            adapter.updateTasks(tache);
        }
        }

        /**
         * Returns the dialog allowing the user to create a new task.
         *
         * @return the dialog allowing the user to create a new task
         */
        @NonNull
        private AlertDialog getAddTaskDialog() {
            final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this, R.style.Dialog);

            alertBuilder.setTitle(R.string.add_task);
            alertBuilder.setView(R.layout.dialog_add_task);
            alertBuilder.setPositiveButton(R.string.add, null);
            alertBuilder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    dialogEditText = null;
                    dialogSpinner = null;
                    dialog = null;
                }
            });

            dialog = alertBuilder.create();

            // This instead of listener to positive button in order to avoid automatic dismiss
            dialog.setOnShowListener(new DialogInterface.OnShowListener() {

                @Override
                public void onShow(DialogInterface dialogInterface) {
                    Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                    button.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {
                            onPositiveButtonClick(dialog);
                        }
                    });
                }
            });

            return dialog;
        }

        /**
         * Sets the data of the Spinner with projects to associate to a new task
         */
        private void populateDialogSpinner() {
            final ArrayAdapter<Project> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, allProjects);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            if (dialogSpinner != null) {
                dialogSpinner.setAdapter(adapter);
            }
        }

    @Override
    public void clickclick(int position) {

    }


    private enum SortMethod {
            /**
             * Sort alphabetical by name
             */
            ALPHABETICAL,
            /**
             * Inverted sort alphabetical by name
             */
            ALPHABETICAL_INVERTED,
            /**
             * Lastly created first
             */
            RECENT_FIRST,
            /**
             * First created first
             */
            OLD_FIRST,
            /**
             * No sort
             */
            NONE
        }
    }
