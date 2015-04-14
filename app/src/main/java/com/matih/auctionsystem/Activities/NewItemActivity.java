package com.matih.auctionsystem.Activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.matih.auctionsystem.Classes.AuctionItem;
import com.matih.auctionsystem.Managers.InitManager;
import com.matih.auctionsystem.R;

import java.util.Calendar;
import java.util.Date;

public class NewItemActivity extends Activity {

    public static final String EXTRA_NAME_USER_ID = "userId";

    private Context context;
    private AuctionItem auctionItem;
    private long currUserId;

    private EditText editText_Name;
    private EditText editText_Desc;
    private EditText editText_Price;
    private Button button_Post;
    private Button button_SetTime;
    private Button button_SetDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_item);

        context = this;
        currUserId = getIntent().getLongExtra(EXTRA_NAME_USER_ID, -1);

        editText_Name = (EditText)findViewById(R.id.editText_New_ItemName);
        editText_Desc = (EditText)findViewById(R.id.editText_New_ItemDesc);
        editText_Price = (EditText)findViewById(R.id.editText_New_ItemPrice);
        button_Post = (Button)findViewById(R.id.button_New_Post);
        button_SetTime = (Button)findViewById(R.id.button_New_SetTime);
        button_SetDate = (Button)findViewById(R.id.button_New_SetDate);

        auctionItem = new AuctionItem();

        auctionItem.setExpiry(new Date());
        Calendar calNow = Calendar.getInstance();
        calNow.setTime(auctionItem.getExpiry());

        button_SetTime.setText(((calNow.get(Calendar.HOUR_OF_DAY) < 10) ? ("0" + calNow.get(Calendar.HOUR_OF_DAY)) : calNow.get(Calendar.HOUR_OF_DAY))
                + ":" + ((calNow.get(Calendar.MINUTE) < 10) ? ("0" + calNow.get(Calendar.MINUTE)) : calNow.get(Calendar.MINUTE)));
        button_SetDate.setText(((calNow.get(Calendar.MONTH) < 10) ? ("0" + calNow.get(Calendar.MONTH)) : calNow.get(Calendar.MONTH))
                + "-" + ((calNow.get(Calendar.DAY_OF_MONTH) < 10) ? ("0" + calNow.get(Calendar.DAY_OF_MONTH)) : calNow.get(Calendar.DAY_OF_MONTH)
                + "-" + calNow.get(Calendar.YEAR)));

        // Open Time Picker Dialog
        button_SetTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                c.setTime(auctionItem.getExpiry());
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);
                TimePickerDialog dialog = new TimePickerDialog(context, TimePickerDialog.THEME_HOLO_LIGHT,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                c.setTime(auctionItem.getExpiry());
                                c.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                c.set(Calendar.MINUTE, minute);
                                auctionItem.setExpiry(c.getTime());

                                button_SetTime.setText(((hourOfDay < 10) ? ("0" + hourOfDay) : hourOfDay)
                                        + ":" + ((minute < 10) ? ("0" + minute) : minute));
                            }
                        }, hour, minute, DateFormat.is24HourFormat(context));
                dialog.show();
            }
        });

        // Open Date Picker Dialog
        button_SetDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                c.setTime(auctionItem.getExpiry());
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(context, DatePickerDialog.THEME_HOLO_LIGHT,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                c.setTime(auctionItem.getExpiry());
                                c.set(Calendar.YEAR, year);
                                c.set(Calendar.MONTH, monthOfYear);
                                c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                auctionItem.setExpiry(c.getTime());
                                button_SetDate.setText(((monthOfYear < 10) ? ("0" + monthOfYear) : monthOfYear)
                                                    + "-" + ((dayOfMonth < 10) ? ("0" + dayOfMonth) : dayOfMonth)
                                                    + "-" + year);
                            }
                        }, year, month, day);
                dialog.show();
            }
        });

        button_Post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                post();
            }
        });
    }

    /*
    * Post new Auction Item
    * */
    private void post(){
        if(editText_Name.getText().toString().isEmpty()){
            editText_Name.setError(getResources().getString(R.string.error_no_name));
            return;
        }

        if(editText_Price.getText().toString().isEmpty()){
            editText_Price.setError(getResources().getString(R.string.error_no_price));
            return;
        }

        auctionItem.setItemName(editText_Name.getText().toString());
        auctionItem.setItemDescription(editText_Desc.getText().toString());
        auctionItem.setPrice(Double.parseDouble(editText_Price.getText().toString()));
        auctionItem.setUserId(currUserId);
        auctionItem.setSold(false);

        long result = InitManager.getSharedInstance().getDbManager().addAuctionItem(
                InitManager.getSharedInstance().getDatabase(), auctionItem
        );

        if(result > -1){
            Toast.makeText(this, getResources().getString(R.string.msg_post_success), Toast.LENGTH_SHORT).show();
            finish();
        }
        else{
            Toast.makeText(this, getResources().getString(R.string.msg_post_fail), Toast.LENGTH_SHORT).show();
        }
    }
}
