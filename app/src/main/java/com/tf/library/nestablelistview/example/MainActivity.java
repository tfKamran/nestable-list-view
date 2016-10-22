package com.tf.library.nestablelistview.example;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.tf.library.nestablelistview.NestableListView;

public class MainActivity extends AppCompatActivity {

    private String[] items = new String[]{
            "Item 1", "Item 2", "Item 3", "Item 4", "Item 5", "Item 6", "Item 7", "Item 8", "Item 9", "Item 10"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NestableListView nestableListView1 = (NestableListView) findViewById(R.id.list_view_0);
        NestableListView nestableListView2 = (NestableListView) findViewById(R.id.list_view_1);

        nestableListView1.setAdapter(new MyAdapter(this, android.R.layout.simple_list_item_1));
        nestableListView2.setAdapter(new MyAdapter(this, android.R.layout.simple_list_item_1));
    }

    private class MyAdapter extends NestableListView.Adapter<String> {

        MyAdapter(Context context, int resource) {
            super(context, resource, items);
        }
    }
}
