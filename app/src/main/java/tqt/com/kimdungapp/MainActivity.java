package tqt.com.kimdungapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import tqt.com.kimdungapp.adapter.ListStoryAdapter;
import tqt.com.kimdungapp.database.DatabaseHelperSdcard;
import tqt.com.kimdungapp.entry.Story;

public class MainActivity extends AppCompatActivity {

    private static final String TAG_LOG ="KimDungApp" ;
    private ListView listView;
    private ListStoryAdapter adapter;
    private ArrayList<Story> arrayList;
    public DatabaseHelperSdcard databaseHelperSdcard;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG_LOG," onCreated mainActivity ");

        // set các đối tượng view
        listView =(ListView)findViewById(R.id.list_view);

        // set doi tuong database

        databaseHelperSdcard = new DatabaseHelperSdcard(getBaseContext());

        // get list story from db
        arrayList = databaseHelperSdcard.getListSotry();

        // khởi tao adapter
        adapter = new ListStoryAdapter(MainActivity.this,R.layout.item_list_story,arrayList);


        // set listview
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this,ContentActivity.class);
                intent.putExtra("stid",arrayList.get(position).getStID());
                intent.putExtra("title",arrayList.get(position).getStName());
                intent.putExtra("content",arrayList.get(position).getStDescribe());
                startActivity(intent);
            }
        });
    }
}
