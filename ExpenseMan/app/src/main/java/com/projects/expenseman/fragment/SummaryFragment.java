package com.projects.expenseman.fragment;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.projects.expenseman.R;
import com.projects.expenseman.activities.ListActivity;
import com.projects.expenseman.database.DBHelper;
import com.projects.expenseman.models.Expense;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by ullasjoseph on 18/07/14.
 */
@SuppressLint("ValidFragment")
public class SummaryFragment extends Fragment implements View.OnClickListener{

    private static final String ARG_SECTION_NUMBER = "section_number";
    private final int sectionNumber;
    private View rootView;
    private TextView textViewTitle;
    private TextView textViewAmount;
    private Button buttomTable;
    private DBHelper dbHelper;
    ArrayList<Expense> selectedExpense;


    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static SummaryFragment newInstance(int sectionNumber) {
        SummaryFragment fragment = new SummaryFragment(sectionNumber);
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }


    @SuppressLint("ValidFragment")
    public SummaryFragment(int sectionNumber) {
        this.sectionNumber = sectionNumber;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_summary, container, false);
        dbHelper = new DBHelper(getActivity().getBaseContext());
        textViewTitle = (TextView) rootView.findViewById(R.id.textViewTitle);
        textViewAmount = (TextView) rootView.findViewById(R.id.textViewAmount);
        buttomTable = (Button) rootView.findViewById(R.id.buttomTable);
        buttomTable.setOnClickListener(this);
        loadValues();
        return rootView;
    }

    @Override
    public void onClick(View view) {
        if (view == buttomTable) {
            Intent objIntent = new Intent(getActivity().getBaseContext(), ListActivity.class);
            objIntent.putParcelableArrayListExtra("array", selectedExpense);
            startActivity(objIntent);
        }
    }

    private void loadValues() {

        selectedExpense = new ArrayList<Expense>();
        ArrayList<Expense> allExpense = dbHelper.getAllExpense();
        Calendar calender = Calendar.getInstance();
        int filterStartDay = 0;
        switch (sectionNumber) {
            case 2:
                textViewTitle.setText("Total Expense of the Day");
                filterStartDay = calender.get(Calendar.DAY_OF_MONTH);
                break;

            case 3:
                textViewTitle.setText("Total Expense of the Week");
                filterStartDay = calender.getFirstDayOfWeek();
                break;

            case 4:
                textViewTitle.setText("Total Expense of the Month");
                filterStartDay = 1;
                break;

            default:
                break;
        }



        double totalExpense = 0;
        for (Expense e : allExpense){
            Calendar c = Calendar.getInstance();
            c.setTime(e.getTime());
            int day = c.get(Calendar.DAY_OF_MONTH);

            if (day >= filterStartDay){
                selectedExpense.add(e);
                totalExpense += e.getAmount();
            }
        }
        textViewAmount.setText(totalExpense+" $");
    }
}
