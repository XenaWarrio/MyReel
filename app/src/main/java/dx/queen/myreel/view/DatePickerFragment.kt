package dx.queen.myreel.view

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import dx.queen.myreel.R
import dx.queen.myreel.viewModel.RegistrationViewModel
import java.util.*



class DatePickerFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        return DatePickerDialog(context,
            R.style.MyReelDatePickerSpinnerStyle,this, year, month ,day)
    }



    override fun onDateSet(p0: DatePicker?, p1: Int, p2: Int, p3: Int) {
        val viewModel = RegistrationViewModel()
        val stringOfDate = "$p1/$p2/$p3"

        viewModel.dateOfBirth.value = stringOfDate

    }

}