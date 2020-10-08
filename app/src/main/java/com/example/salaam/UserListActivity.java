package com.example.salaam;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class UserListActivity extends AppCompatActivity {


    ArrayAdapter arrayAdapter;
    ArrayList<String> users = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        ListView userListView=(ListView)findViewById(R.id.userListView);
        users.add("Test list item 1");

        arrayAdapter=new ArrayAdapter(this,android.R.layout.simple_list_item_2,users);
        userListView.setAdapter(arrayAdapter);
    }
}
