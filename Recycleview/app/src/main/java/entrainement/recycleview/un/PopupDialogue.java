package entrainement.recycleview.un;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.recyclerview.widget.RecyclerView;

public class PopupDialogue extends AppCompatDialogFragment {
    private EditText editTextUsername;
    private EditText editTextname;
    private EditText editTextAppreciate;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());
        View view= getActivity().getLayoutInflater().inflate(R.layout.addinflated, null);
        builder.setView(view)
               .setTitle("le titre")
               .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {

                   }
               })
               .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {

                   }
               });
        editTextUsername= view.findViewById(R.id.nom);
//        editTextname= view.findViewById(R.id.prenom);
//        editTextAppreciate= view.findViewById(R.id.apreciation);

        return builder.create();

    }
}
