package zut.edu.cn.notepad;

        import androidx.appcompat.app.AppCompatActivity;

        import android.content.ContentValues;
        import android.content.Intent;
        import android.database.sqlite.SQLiteDatabase;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.View;
        import android.widget.TextView;
        import android.widget.Button;
        import android.widget.Toast;

public class ShowDelActivity extends AppCompatActivity {
    private Button btnRestore;
    private Button btnClear;
    private Button btnBack;
    private TextView showTime;
    private TextView showContent;
    private TextView showTitle;
    DBService myDb;
    Values values;
    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_del);
        myDb = new DBService(this);
        init();
    }

    public void init() {

        btnClear = findViewById(R.id.show_clear);
        btnRestore = findViewById(R.id.show_restore);
        btnBack = findViewById(R.id.show_back);
        showTime = findViewById(R.id.show_time);
        showTitle = findViewById(R.id.show_title);
        showContent = findViewById(R.id.show_content);
        Intent intent = this.getIntent();


        if (intent != null) {

            showTime.setText(intent.getStringExtra(DBService.TIME));
            showTitle.setText(intent.getStringExtra(DBService.TITLE));
            showContent.setText(intent.getStringExtra(DBService.CONTENT));
            id =intent.getStringExtra(DBService.ID);
        }
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = myDb.getWritableDatabase();
                db.delete(DBService.TABLE2,DBService.ID+"=?",new String[]{id});
                Intent intent = new Intent(ShowDelActivity.this, DeleteActivity.class);
                startActivity(intent);
                Toast.makeText(ShowDelActivity.this, "已删除", Toast.LENGTH_LONG).show();
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowDelActivity.this, DeleteActivity.class);
                startActivity(intent);
            }
        });
        btnRestore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = myDb.getWritableDatabase();
                ContentValues values = new ContentValues();
                String title= showTitle.getText().toString();
                String content=showContent.getText().toString();
                String time= showTime.getText().toString();
                values.put(DBService.TITLE,title);
                values.put(DBService.CONTENT,content);
                values.put(DBService.TIME,time);
                db.insert(DBService.TABLE,null,values);//恢复到notes表
                db.delete(DBService.TABLE2,DBService.ID+"=?",new String[]{id});//在history表中删除
                Intent intent = new Intent(ShowDelActivity.this, DeleteActivity.class);
                startActivity(intent);
                Toast.makeText(ShowDelActivity.this, "已成功恢复数据", Toast.LENGTH_LONG).show();
            }
        });
    }
}
