package com.dubinets.requisition.activity.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

import com.dubinets.requisition.R;
import com.dubinets.requisition.databasehelper.DatabaseHelper;
import com.dubinets.requisition.db.Type;

/**
 * Created by dubinets on 07.09.2016.
 */
public class TypeDialog extends AppCompatActivity {
    private final long      NULL            = 0L;

    private EditText    type_name_te;
    private Type        type;
    private Long        type_id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_dialog_type);

        this.type_name_te   = (EditText) findViewById(R.id.activity_navigation_dialog_type_et);

        this.type_id        = getIntent().getLongExtra("type_id", NULL);
        this.type           = getTypeById(type_id);

        setEditText();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_done, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_done_button:
                setType2DB();
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setEditText() {
        if(type.getId() == null) {
            this.type_name_te.setText(type.getType_name());
        }
    }

    private void setType2DB() {
        String type_name = this.type_name_te.getText().toString();

        Type type = new Type(null);
        type.setType_name(type_name);

        synchronized (this) {
            if(type.getId() == null) {
                DatabaseHelper.getTypeDao().insert(type);
            } else {
                DatabaseHelper.getTypeDao().update(type);
            }
        }

    }

    private Type getTypeById(long id) {
        Type type = null;
        if(id != NULL) {
            type = DatabaseHelper.getTypeDao().load(id);
        } else {
            type = new Type(null);
        }

        return type;
    }

}
