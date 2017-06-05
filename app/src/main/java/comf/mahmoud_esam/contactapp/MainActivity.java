package comf.mahmoud_esam.contactapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {

    Button insertBtn ;
    EditText searchEdtText;
    ListView contactList;
    ContactAdapter contactAdapter;
    DatabaseHandler databaseHandler;
    ArrayList<Contact>contacts;
    String newName ;
    AdapterView.AdapterContextMenuInfo acmi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        insertBtn = (Button) findViewById(R.id.main_activity_insert_button);
        insertBtn.setOnClickListener(this);
        searchEdtText = (EditText) findViewById(R.id.mainActivity_search_editText);
        searchEdtText.addTextChangedListener(this);
        contactList = (ListView) findViewById(R.id.main_activity_contactlist_listview);
        databaseHandler= new DatabaseHandler(this);
        registerForContextMenu(contactList);

    }

    @Override
    protected void onResume() {
        super.onResume();
        updateList();
        YoYo.with(Techniques.DropOut)
                .duration(700)
                .repeat(5)
                .playOn(findViewById(R.id.main_activity_insert_button));
        YoYo.with(Techniques.Tada)
                .duration(700)
                .repeat(5)
                .playOn(findViewById(R.id.mainActivity_search_editText));
    }



    @Override
    public void onClick(View view) {
        Intent intent = new Intent(this,AddContactAcitivity.class);
        startActivity(intent);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        databaseHandler= new DatabaseHandler(this);
        contacts=databaseHandler.getContactByName(searchEdtText.getText().toString());
        contactAdapter=new ContactAdapter(this,android.R.layout.simple_list_item_1,contacts);
        contactList.setAdapter(contactAdapter);
    }

    @Override
    public void afterTextChanged(Editable editable) {
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId()==R.id.main_activity_contactlist_listview)
        {
            menu.add("Delete ");
            menu.add("Update ");
        }
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        acmi = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        final Contact selectedContact = (Contact) contactList.getItemAtPosition(acmi.position);
        final String name = String.valueOf(selectedContact.getName());

        if(item.toString().equals("Delete "))
        {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setTitle("Delete Contact");
            builder1.setMessage("Are you sure that you want to delete "+name+" ?");
            builder1.setCancelable(true);

            builder1.setPositiveButton(
                    "yes",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            databaseHandler.deleteContactByID(selectedContact.getId());
                            Toast.makeText(MainActivity.this,name+" is Deleted !", Toast.LENGTH_SHORT).show();
                            updateList();
                            dialog.cancel();
                        }
                    });

            builder1.setNegativeButton(
                    "No",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

            AlertDialog alert11 = builder1.create();
            alert11.show();

        }


        if(item.toString().equals("Update "))

        {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Update Contact");


            final EditText input = new EditText(this);
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            builder.setView(input);

            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    newName = input.getText().toString();
                    databaseHandler.updateContactById(selectedContact.getId(),newName);
                    Toast.makeText(MainActivity.this,name+" is Updated !", Toast.LENGTH_SHORT).show();
                    updateList();
                    dialog.cancel();
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            builder.show();
        }
        return super.onContextItemSelected(item);
    }
    private void updateList()
    {
        contacts=databaseHandler.getAllContacts();
        contactAdapter=new ContactAdapter(this,android.R.layout.simple_list_item_1,contacts);
        contactList.setAdapter(contactAdapter);
    }
}
