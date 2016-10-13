package com.example.achan.databasetest;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private MyDatabaseHelper myDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myDatabaseHelper=new MyDatabaseHelper(this,"BookStore.db",null,2);
        Button createDatabase=(Button)findViewById(R.id.create_database);
        createDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDatabaseHelper.getWritableDatabase();
            }
        });

        Button addData=(Button)findViewById(R.id.add_data);
        Button updateData=(Button)findViewById(R.id.update_data);
        Button deleteData=(Button)findViewById(R.id.delete_data);
        Button queryData=(Button)findViewById(R.id.query_data);
        Button replaceData=(Button)findViewById(R.id.replace_data);

        addData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db=myDatabaseHelper.getWritableDatabase();
                ContentValues values=new ContentValues();
                //开始组装第一条数据
                values.put("name","The DaVinci code");
                values.put("author","Dan");
                values.put("pages",454);
                values.put("price",26.96);
                db.insert("Book",null,values);
                values.clear();
                values.put("name","First Code");
                values.put("author","GuoLin");
                values.put("pages",552);
                values.put("price",79.00);
                db.insert("Book",null,values);
            }
        });

        updateData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db=myDatabaseHelper.getWritableDatabase();
                ContentValues values=new ContentValues();
                values.put("price",10.00);
                db.update("Book",values,"name=?",new String[]{"The DaVinci code"});
            }
        });

        deleteData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db=myDatabaseHelper.getWritableDatabase();
                db.delete("Book","pages>?",new String[] {"300"});
            }
        });

        queryData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db=myDatabaseHelper.getWritableDatabase();
                Cursor cursor=db.query("Book",null,null,null,null,null,null);
                if(cursor.moveToFirst()){
                    do{
                        //遍历cursor对象
                        String name=cursor.getString(cursor.getColumnIndex("name"));
                        String author=cursor.getString(cursor.getColumnIndex("author"));
                        int pages=cursor.getInt(cursor.getColumnIndex("pages"));
                        double price=cursor.getDouble(cursor.getColumnIndex("price"));

                        Log.d("MainActivity","name:"+name+" author:"+author+" pages:"+pages+" price:"+price);

                    }while (cursor.moveToNext());
                }
                cursor.close();
            }
        });

        replaceData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db=myDatabaseHelper.getWritableDatabase();
                db.beginTransaction();//开始事物
                try{
                    db.delete("Book",null,null);
                    if(true){
                        //手动抛出一个异常,让事物失败
                        //throw  new NullPointerException();
                    }
                    ContentValues values=new ContentValues();
                    values.put("name","Game of throne");
                    values.put("author","George");
                    values.put("pages",780);
                    values.put("price",20);
                    db.insert("Book",null,values);
                    db.setTransactionSuccessful();//事物执行成功

                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    db.endTransaction();
                }
            }
        });
    }
}
