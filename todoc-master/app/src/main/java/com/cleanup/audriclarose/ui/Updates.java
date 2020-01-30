//package com.cleanup.audriclarose.ui;
//
//import android.app.AlertDialog;
//import android.app.Dialog;
//import android.content.DialogInterface;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.widget.EditText;
//import android.widget.Spinner;
//
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatDialogFragment;
//
//import com.cleanup.audriclarose.R;
//import com.cleanup.audriclarose.model.Project;
//import com.cleanup.audriclarose.model.Task;
//
//import java.util.Date;
//
//import static android.content.ContentValues.TAG;
//
//public class Updates extends AppCompatDialogFragment {
//    public capture chamgement;
//    @Nullable
//    public androidx.appcompat.app.AlertDialog dialog = null;
//    @Nullable
//    private EditText dialogEditText = null;
//    @Nullable
//    private Spinner dialogSpinner = null;
//
//    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        LayoutInflater inflater= getActivity().getLayoutInflater();
//        View view= inflater.inflate(R.layout.dialog_add_task,null);
//        builder.setView(view);
//        builder.setTitle("Update du choix de la tache");
//        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//
//            }
//        });
//        builder.setPositiveButton("Maj", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                if (dialogEditText != null && dialogSpinner != null) {
//                    String taskName = dialogEditText.getText().toString();
//                    Project taskProject = null;
//                    if (dialogSpinner.getSelectedItem() instanceof Project) {
//                        taskProject = (Project) dialogSpinner.getSelectedItem();
//                    }
//
//                    if (taskName.trim().isEmpty()) {
//                        dialogEditText.setError(getString(R.string.empty_task_name));
//                    }
//                    else  {
//                        // TODO: Replace this by id of persisted task
//                        long id = (long) (Math.random() * 50000);
//                        Task task = new Task(
//                                taskProject.getId(),
//                                taskName,
//                                new Date().getTime()
//                        );
//                        Log.d(TAG, "onClick: "+ task);
//                        chamgement.uploading(task);
//                        dialogInterface.dismiss();
//                    }
//                    // If name has been set, but project has not been set (this should never occur)
//                }
//                // If dialog is aloready closed
//                else {
//                    dialogInterface.dismiss();
//                }
//
//            }
//        });
//        return builder.create();
//    }
//
//    public interface capture{
//        void uploading(Task task);
//        }
//    }
//
//
