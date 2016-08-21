package com.dubinets.requisition;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static ArrayList<String> list = new ArrayList<>();

    static {
        list.add("Dog");
        list.add("Cat");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewList();
    }

    private void viewList() {
        TextView textView = (TextView) findViewById(R.id.textView);
        StringBuilder sb = new StringBuilder();
        for (String element : list) {
            sb.append(element);
        }
        textView.setText(sb);
    }

}
