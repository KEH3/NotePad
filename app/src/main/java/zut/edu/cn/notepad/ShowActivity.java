package zut.edu.cn.notepad;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ShowActivity extends AppCompatActivity {

    private Button btnSave;
    private Button btnCancel;
    private TextView showTime;
    private EditText showContent;
    private EditText showTitle;

    private String prevTitle;
    private String prevContent;

    private Values value;
    DBService myDb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        init();
    }

    public void init() {
        myDb = new DBService(this);
        btnCancel = findViewById(R.id.show_cancel);
        btnSave = findViewById(R.id.show_save);
        showTime = findViewById(R.id.show_time);
        showTitle = findViewById(R.id.show_title);
        showContent = findViewById(R.id.show_content);

        Intent intent = this.getIntent();
        if (intent != null) {
            value = new Values();

            value.setTime(intent.getStringExtra(DBService.TIME));
            value.setTitle(intent.getStringExtra(DBService.TITLE));
            value.setContent(intent.getStringExtra(DBService.CONTENT));
            value.setId(Integer.valueOf(intent.getStringExtra(DBService.ID)));

            showTime.setText(value.getTime());
            showTitle.setText(value.getTitle());
            showContent.setText(value.getContent());

            prevTitle = value.getTitle();
            prevContent = value.getContent();
        }

        //按钮点击事件
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = myDb.getWritableDatabase();
                ContentValues values = new ContentValues();//  ContentValues是用来组装数据的；
                String content = showContent.getText().toString();
                String title = showTitle.getText().toString();

                values.put(DBService.TIME, getTime());
                values.put(DBService.TITLE,title);
                values.put(DBService.CONTENT,content);

                db.update(DBService.TABLE,values,DBService.ID+"=?",new String[]{value.getId().toString()});
                Toast.makeText(ShowActivity.this,"修改成功",Toast.LENGTH_LONG).show();
                db.close();
                Intent intent = new Intent(ShowActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String content = showContent.getText().toString();
                final String title = showTitle.getText().toString();
                if(prevContent.equals(content)&&prevTitle.equals(title)) {
                    Intent intent = new Intent(ShowActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    new AlertDialog.Builder(ShowActivity.this)
                            .setTitle("提示框")
                            .setMessage("是否保存改动?")
                            .setPositiveButton("保存",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            SQLiteDatabase db = myDb.getWritableDatabase();
                                            ContentValues values = new ContentValues();
                                            values.put(DBService.TIME, getTime());
                                            values.put(DBService.TITLE, title);
                                            values.put(DBService.CONTENT, content);
                                            db.update(DBService.TABLE, values, DBService.ID + "=?", new String[]{value.getId().toString()});
                                            Toast.makeText(ShowActivity.this, "修改成功", Toast.LENGTH_LONG).show();
                                            db.close();
                                            Intent intent = new Intent(ShowActivity.this, MainActivity.class);
                                            startActivity(intent);
                                        }
                                    })
                            .setNegativeButton("不保存",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent(ShowActivity.this, MainActivity.class);
                                            startActivity(intent);
                                        }
                                    }).show();
                }
            }
        });
    }

    String getTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //获取当前时间
        Date date = new Date(System.currentTimeMillis());
        return simpleDateFormat.format(date);
    }
}
