package fine.koacaiia.mnfwms;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener{
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Calendar calendar=Calendar.getInstance();
        int year=calendar.get(Calendar.YEAR);
        int month=calendar.get(Calendar.MONTH);
        int day=calendar.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(),this,year,month,day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String strMonth;
        String strDay;
        if(month<10){
            strMonth="0"+(month+1);
        }else{
            strMonth=String.valueOf(month+1);
        }

        if(dayOfMonth<10){
            strDay="0"+dayOfMonth;
        }else{
            strDay=String.valueOf(dayOfMonth);
        }
        String date=year+"-"+strMonth+"-"+strDay;
        MainActivity mainActivity=(MainActivity)getActivity();
        mainActivity.datePickerResult(date);

    }
}
