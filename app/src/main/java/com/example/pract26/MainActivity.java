package com.example.pract26;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    DbHandler db;
    EditText t1, t2, t3;
    TextView tv;
    SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        t1 = findViewById(R.id.t1);
        t2 = findViewById(R.id.t2);
        t3 = findViewById(R.id.t3);

        tv = findViewById(R.id.tv);
        db = new DbHandler(this);

        database = db.getWritableDatabase();
    }

    public void InsertData(View v) {
        int roll = Integer.parseInt(t1.getText().toString());
        String name = t2.getText().toString();
        String Class = t3.getText().toString();

        new Background().execute(roll, name, Class);
    }

    private class Background extends AsyncTask<Object, Void, Void> {
        long res;
        @Override
        protected Void doInBackground(Object... objects) {
            ContentValues c = new ContentValues();
            c.put(db.roll, (int) objects[0]);
            c.put(db.name, (String) objects[1]);
            c.put(db.Class, (String) objects[2]);
            res= database.insert("students", null, c);
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            if(res==-1){
                Toast.makeText(MainActivity.this, "Failed to insert record!", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(MainActivity.this, "Record Inserted Successfully ", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void ReadData(View v) {
        EditText et;
        et = findViewById(R.id.search);
        int roll = Integer.parseInt(et.getText().toString());
        new background2().execute(roll);
    }

    private class background2 extends AsyncTask<Integer, Void, String> {
        @SuppressLint("Range")
        @Override
        protected String doInBackground(Integer... integers) {
            StringBuilder sb = new StringBuilder();
            String query = "select * from students where roll=?";
            Cursor c = database.rawQuery(query, new String[]{String.valueOf(integers[0])});
            if (c.moveToNext()) {
                sb.append(c.getInt(c.getColumnIndex(db.roll))).append("\n");
                sb.append(c.getString(c.getColumnIndex(db.name))).append("\n");
                sb.append(c.getString(c.getColumnIndex(db.Class))).append("\n");
            }
            c.close();
            return sb.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            tv.setText(s);
        }
    }
}
