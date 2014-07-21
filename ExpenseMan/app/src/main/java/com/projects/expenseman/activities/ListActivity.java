package com.projects.expenseman.activities;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.projects.expenseman.R;
import com.projects.expenseman.models.Expense;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by ullasjoseph on 18/07/14.
 */
public class ListActivity extends Activity{

    private ListView listView;
    private ArrayList<Expense> expenseList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        expenseList = getIntent().getParcelableArrayListExtra("array");

        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(new ListAdapter(expenseList));

    }

    public class ListAdapter extends BaseAdapter {

        private final ArrayList<Expense> expenseList;

        public ListAdapter(ArrayList<Expense> expenseList) {
            this.expenseList = expenseList;
        }

        @Override
        public int getCount() {
            return expenseList.size();
        }

        @Override
        public Object getItem(int i) {
            return expenseList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            RelativeLayout listItemView = (RelativeLayout) view;

            if (listItemView == null) {
                LayoutInflater inflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                listItemView = (RelativeLayout)inflater.inflate(R.layout.view_list_item, viewGroup, false);
            }

            Expense expense = expenseList.get(i);
            TextView textViewAmount = (TextView) listItemView.findViewById(R.id.textViewAmount);
            textViewAmount.setText(expense.getAmount() + " $");
            TextView textViewCategory = (TextView) listItemView.findViewById(R.id.textViewCategory);
            textViewCategory.setText("Category : "+expense.getCategory());
            TextView textViewPayee = (TextView) listItemView.findViewById(R.id.textViewPayee);
            textViewPayee.setText("Payee : "+expense.getPayee());
            TextView textViewNotes = (TextView) listItemView.findViewById(R.id.textViewNotes);
            textViewNotes.setText("Notes : "+expense.getNotes());
            TextView textViewDate = (TextView) listItemView.findViewById(R.id.textViewDate);
            textViewDate.setText("Date : "+new SimpleDateFormat("dd MMM yyyy hh:mm:ss").format(expense.getTime()));
            return listItemView;
        }
    }
}
