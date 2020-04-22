package entrainement.timer.alarm;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class TimePickerFragment  extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Calendar c= Calendar.getInstance();
        int heure= c.get(Calendar.HOUR_OF_DAY);
        int minute=c.get(Calendar.MINUTE);
        return  new TimePickerDialog(getActivity(), (TimePickerDialog.OnTimeSetListener) getActivity(),heure, minute, android.text.format.DateFormat.is24HourFormat(getActivity()
        ) );
    }
}