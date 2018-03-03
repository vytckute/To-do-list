package lt.to_do_list.controller;

// Android importai

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import lt.to_do_list.R;
import lt.to_do_list.model.DBActions;

// Mūsų vietiniai importai


public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    // Model
    private DBActions dbActions;

    //Controller
    private ItemVOAdapter itemVOAdapter;

    // View
    private ListView itemListView;

    public void updateView() {

        if (itemVOAdapter == null) {
            itemVOAdapter = new ItemVOAdapter(this, dbActions.getAllItems(), dbActions);
            itemListView.setAdapter(itemVOAdapter);
        } else {
            itemVOAdapter.clear();
            itemVOAdapter.addAll(dbActions.getAllItems());
            itemVOAdapter.notifyDataSetChanged();
        }

    }

    public void showDialog() {
        final EditText taskEditText = new EditText(this);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Pridėti naują įrašą")
                .setMessage("Ką norite dar nuveikti?")
                .setView(taskEditText)
                .setPositiveButton("Pridėti", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String task = String.valueOf(taskEditText.getText());
                        dbActions.addItem(task);
                        updateView();
                    }
                })
                .setNegativeButton("Atšaukti", null)
                .create();
        dialog.show();
    }

    @Override
    public void onClick(View view) {
        showDialog();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_main_activity);
        dbActions = new DBActions(this);

        itemListView = (ListView) findViewById(R.id.list_todo);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);
        updateView();
    }



}