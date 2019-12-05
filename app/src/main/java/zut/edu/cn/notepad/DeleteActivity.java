package zut.edu.cn.notepad;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class DeleteActivity extends AppCompatActivity {
    DBService myDb;
    private ListView lv_note;
    private FloatingActionButton done;
   // List<Values> valuesList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        myDb = new DBService(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);
        init();
//        Check_Adapter adapter = new Check_Adapter(DeleteActivity.this,valuesList);
//        lv_note.setAdapter(adapter);
    }

    public void init() {

        done = findViewById(R.id.done);
        lv_note = findViewById(R.id.lisv_note);
        List<Values> valuesList = new ArrayList<>();//泛型用法 这里valuesList是个对象数组 只能“add”Value对象
        SQLiteDatabase db = myDb.getReadableDatabase();

        //查询数据库中的数据
        Cursor cursor = db.query(DBService.TABLE, null, null,
                null, null, null, null);
        if (cursor.moveToFirst()) {
            Values values;
            while (!cursor.isAfterLast()) {//判断当前指针是否已经到最后一条记录的下一个若是则返回true
                //实例化values对象
                values = new Values();

                //把数据库中的一个表中的数据赋值给values
                values.setId(Integer.valueOf(cursor.getString(cursor.getColumnIndex(DBService.ID))));
                values.setTitle(cursor.getString(cursor.getColumnIndex(DBService.TITLE)));
                values.setContent(cursor.getString(cursor.getColumnIndex(DBService.CONTENT)));
                values.setTime(cursor.getString(cursor.getColumnIndex(DBService.TIME)));

                //将values对象存入list对象数组中 valueList里面有要显示的数据所有数据 但不能直接显示在listview上 要适配器
                valuesList.add(values);
                cursor.moveToNext();
            }
        }
        cursor.close();
        db.close();

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3 = new Intent(DeleteActivity.this, MainActivity.class);
                startActivity(intent3);
            }
        });
        Check_Adapter adapter = new Check_Adapter(DeleteActivity.this,valuesList);
        lv_note.setAdapter(adapter);

    }
    class Check_Adapter extends BaseAdapter {
        private List<Values> mData; //相当于valuelist 一个类型都是对象数组
        private LayoutInflater mInflater;

        public Check_Adapter(Context context, List<Values> data) {
            mInflater = LayoutInflater.from(context);
            this.mData = data;
            //if(mData==null){ Log.d("DeleteActivity","不高兴！");}
        }

        public void setChecks(List<Values> data) {
            this.mData = data;
        }

        @Override
        public int getCount() {
            if (mData != null && mData.size() > 0)
                return mData.size();
            else
                return 0;
        }

        @Override
        public Object getItem(int position) {
            return mData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.delete_item, null);
                viewHolder.title = (TextView) convertView.findViewById(R.id.tv_title);
                viewHolder.time = (TextView) convertView.findViewById(R.id.tv_time);
                viewHolder.content = (TextView) convertView.findViewById(R.id.tv_content);
                viewHolder.checkBox = (CheckBox)convertView.findViewById(R.id.checkBox);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            if (mData.get(position).isChecked()) {
                viewHolder.checkBox.setVisibility(View.VISIBLE);
                Log.d("DeleteActivity","不高兴！");
            }else {
                viewHolder.checkBox.setVisibility(View.INVISIBLE);
            }
            viewHolder.title.setText(mData.get(position).getTitle());
            viewHolder.content.setText(mData.get(position).getContent());
            viewHolder.time.setText(mData.get(position).getTime());

            return convertView;
        }

        class ViewHolder {
            TextView title;
            TextView content;
            TextView time;
            CheckBox checkBox;
        }
    }
}

