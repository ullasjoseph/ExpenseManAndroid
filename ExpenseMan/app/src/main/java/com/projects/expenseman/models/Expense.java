package com.projects.expenseman.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by ullasjoseph on 17/07/14.
 */
public class Expense implements Parcelable {
    private String category;
    private Date time;
    private String payee;
    private String notes;
    private Double amount;

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Expense() {
        setAmount(0.0);
        setTime(Calendar.getInstance().getTime());
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getPayee() {
        return payee;
    }

    public void setPayee(String payee) {
        this.payee = payee;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(category);
        out.writeString(payee);
        out.writeString(notes);
        out.writeDouble(amount);
        out.writeLong(time.getTime());

    }

    public static final Parcelable.Creator<Expense> CREATOR
            = new Parcelable.Creator<Expense>() {
        public Expense createFromParcel(Parcel in) {
            return new Expense(in);
        }

        public Expense[] newArray(int size) {
            return new Expense[size];
        }
    };

    private Expense(Parcel in) {
        category = in.readString();
        payee = in.readString();
        notes = in.readString();
        amount = in.readDouble();
        time = new Date(in.readLong());
    }
}
