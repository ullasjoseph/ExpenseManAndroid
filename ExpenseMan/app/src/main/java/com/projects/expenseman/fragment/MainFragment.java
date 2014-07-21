package com.projects.expenseman.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.projects.expenseman.R;
import com.projects.expenseman.database.DBHelper;
import com.projects.expenseman.models.Expense;
import com.projects.expenseman.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by ullasjoseph on 17/07/14.
 */
public class MainFragment extends Fragment implements View.OnClickListener {

    private static Expense expense;
    private DBHelper dbHelper;
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private Spinner spinnerCategory;
    private Button buttonDate;
    private Button buttonTime;
    private Button buttonAddCategory;
    private Button buttonAddPayee;
    private Spinner spinnerPayee;
    private EditText editTextNotes;
    private Button buttonReset;
    private Button buttonSave;

    private ArrayList<String>categories;
    private ArrayList<String>payees;
    private View rootView;
    private Button buttonAmount;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static MainFragment newInstance(int sectionNumber) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public static void setExpense(Expense expense) {
        MainFragment.expense = expense;
    }

    public MainFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_main, container, false);
        setExpense(new Expense());
        dbHelper = new DBHelper(getActivity().getBaseContext());
        spinnerCategory = (Spinner) rootView.findViewById(R.id.spinnerCategory);
        buttonDate = (Button) rootView.findViewById(R.id.buttonDate);
        buttonTime = (Button) rootView.findViewById(R.id.buttonTime);
        buttonAddCategory = (Button) rootView.findViewById(R.id.buttonAddCategory);
        buttonAddPayee = (Button) rootView.findViewById(R.id.buttonAddPayee);
        buttonAmount = (Button) rootView.findViewById(R.id.buttonAmount);
        spinnerPayee = (Spinner) rootView.findViewById(R.id.spinnerPayee);
        editTextNotes = (EditText) rootView.findViewById(R.id.editTextNotes);
        buttonReset = (Button) rootView.findViewById(R.id.buttonReset);
        buttonSave = (Button) rootView.findViewById(R.id.buttonSave);
        buttonAmount.setOnClickListener(this);
        buttonAddCategory.setOnClickListener(this);
        buttonAddPayee.setOnClickListener(this);
        buttonDate.setOnClickListener(this);
        buttonTime.setOnClickListener(this);
        buttonReset.setOnClickListener(this);
        buttonSave.setOnClickListener(this);
        categories = Utils.getStringArrayPref(getActivity().getBaseContext(), "categories");
        ArrayAdapter<String> adapterCategory = new ArrayAdapter<String>(this.getActivity(),
                android.R.layout.simple_spinner_item, categories);
        adapterCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapterCategory);

        payees = Utils.getStringArrayPref(getActivity().getBaseContext(), "payees");
        ArrayAdapter<String> adapterPayee = new ArrayAdapter<String>(this.getActivity(),
                android.R.layout.simple_spinner_item, payees);
        adapterPayee.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPayee.setAdapter(adapterPayee);

        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(expense != null)
                    expense.setCategory(categories.get(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerPayee.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(expense != null)
                    expense.setPayee(payees.get(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        loadExpenseInView();
        return rootView;
    }

    private void saveExpense(){
//        dbHelper.resetDB();
        expense.setCategory(spinnerCategory.getSelectedItem().toString());
        expense.setPayee(spinnerPayee.getSelectedItem().toString());
        expense.setNotes(editTextNotes.getText().toString());
        dbHelper.insertExpense(expense);

        Toast toast = Toast.makeText(getActivity().getBaseContext(), "saved", Toast.LENGTH_SHORT);
        toast.show();
    }

    private void showEditTextAlertForCategories() {
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());

        alert.setTitle("New Category");

        final EditText input = new EditText(getActivity());
        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                categories.add(String.valueOf(input.getText()));
                Utils.setStringArrayPref(getActivity().getBaseContext(), "categories",categories);
                // Do something with value!
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });

        alert.show();
    }

    private void showEditTextAlertForPayees() {
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());

        alert.setTitle("New Payee");

        final EditText input = new EditText(getActivity());
        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                payees.add(String.valueOf(input.getText()));
                Utils.setStringArrayPref(getActivity().getBaseContext(), "payees",payees);
                // Do something with value!
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });

        alert.show();
    }

    private void showEditTextAlertForAmount() {
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());

        alert.setTitle("Amount");

        final EditText input = new EditText(getActivity());
        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                expense.setAmount(Double.valueOf(String.valueOf(input.getText())));
                buttonAmount.setText(expense.getAmount() + " $");
                // Do something with value!
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });

        alert.show();
    }
    @Override
    public void onClick(View view) {
        if(view == buttonAddCategory) {
            showEditTextAlertForCategories();
        }
        if(view == buttonAddPayee) {
            showEditTextAlertForPayees();
        }
        if(view == buttonDate) {
            showDatePickerDialog(rootView);
        }
        if(view == buttonAmount) {
            showEditTextAlertForAmount();
        }
        if(view == buttonTime) {
            showTimePickerDialog(rootView);
        }
        if(view == buttonReset) {
            setExpense(new Expense());
            loadExpenseInView();
        }
        if(view == buttonSave) {
            saveExpense();
            setExpense(new Expense());
            loadExpenseInView();
        }
    }

    private void loadExpenseInView(){
        editTextNotes.setText(expense.getNotes());
        buttonDate.setText(new SimpleDateFormat("dd MMM yyyy").format(expense.getTime()));
        buttonTime.setText(new SimpleDateFormat("hh:mm:ss").format(expense.getTime()));
        spinnerCategory.setSelection(categories.indexOf(expense.getCategory()));
        spinnerPayee.setSelection(payees.indexOf(expense.getPayee()));
        buttonAmount.setText(expense.getAmount()+" $");
    }

    private void loadExpenseDateTimeInView(){
        buttonDate.setText(new SimpleDateFormat("dd MMM yyyy").format(expense.getTime()));
        buttonTime.setText(new SimpleDateFormat("hh:mm:ss").format(expense.getTime()));
    }

    private void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment(this);
        newFragment.show(getFragmentManager(), "datePicker");
    }

    private void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment(this);
        newFragment.show(getFragmentManager(), "timePicker");
    }

    @SuppressLint("ValidFragment")
    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        private final MainFragment fragment;

        public DatePickerFragment(MainFragment mainFragment) {
            this.fragment = mainFragment;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            Calendar calendar=Calendar.getInstance();
            calendar.setTime(expense.getTime());
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, day);
            expense.setTime(calendar.getTime());
            fragment.loadExpenseDateTimeInView();
        }
    }

    @SuppressLint("ValidFragment")
    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        private final MainFragment lis;

        public TimePickerFragment(MainFragment lis) {
            this.lis = lis;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            Calendar calendar=Calendar.getInstance();
            calendar.setTime(expense.getTime());
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendar.set(Calendar.MINUTE, minute);
            expense.setTime(calendar.getTime());
            lis.loadExpenseDateTimeInView();
            // Do something with the time chosen by the user
        }
    }
}

