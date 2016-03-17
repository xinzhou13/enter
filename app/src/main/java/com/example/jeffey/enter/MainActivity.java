package com.example.jeffey.enter;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import static android.content.Context.*;

public class MainActivity extends AppCompatActivity {

    private static final String[] list = {"aadasda","fdsgfd","sfdsfd"};
    private ArrayAdapter<String> adapter;
    private Spinner spinner;
    private EditText moneyInput;

    private DatePicker datePicker;
    private Button submit;

    private String item;
    private int money;
    private String date;
    SQLiteDatabase db;

    public static boolean isNumeric(String str){
        for (int i = str.length();--i>=0;){
            if (!Character.isDigit(str.charAt(i))){
                return false;
            }
        }
        return true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        spinner = (Spinner)findViewById(R.id.spinner);
        moneyInput = (EditText)findViewById(R.id.editText);
        submit = (Button)findViewById(R.id.Submit);
        datePicker=(DatePicker)this.findViewById(R.id.datePicker1);

        db = openOrCreateDatabase("test.db", MODE_PRIVATE, null);


        //db.execSQL("CREATE TABLE demo (item varchar, money int, date varchar)");


        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
        spinner.setVisibility(View.VISIBLE);
        spinner.setMinimumWidth(800);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            spinner.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        }


        submit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {

                String yearStr = String.valueOf(datePicker.getYear());
                String monthStr = String.valueOf(datePicker.getMonth() + 1);
                String dayStr = String.valueOf(datePicker.getDayOfMonth());

                if (!isNumeric(moneyInput.getText().toString())) {
                    Toast.makeText(MainActivity.this, "Money must be number!!!", Toast.LENGTH_LONG).show();
                }
                else if(moneyInput.getText().toString().isEmpty()){
                    Toast.makeText(MainActivity.this, "Money can not be empty!!!", Toast.LENGTH_LONG).show();
                }

                else {
                    item = spinner.getSelectedItem().toString();
                    money = Integer.parseInt(moneyInput.getText().toString());
                    date = yearStr + "/" + monthStr + "/" + dayStr;

                    try {
                        db.execSQL("insert into demo values (?,?,?)", new Object[]{item, money, date});
                    } catch (Exception e) {
                        Log.d("Database:", e.toString());
                    }
                }

                Cursor cursor = db.rawQuery("SELECT * FROM demo", null);

                Log.d("Database:", "item/money/date");
                while (cursor.moveToNext()) {
                    Log.d("Database:", cursor.getString(0) +" || "+ String.valueOf(cursor.getInt(1)) + "||" + cursor.getString(2));
                }


            }
        });

    }

    public void OnDestroy(){
        super.onDestroy();
        db.close();
    }
}