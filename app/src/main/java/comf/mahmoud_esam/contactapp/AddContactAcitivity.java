package comf.mahmoud_esam.contactapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.util.ArrayList;

public class AddContactAcitivity extends AppCompatActivity implements View.OnClickListener {

    EditText nameEditText;
    EditText phoneEditText;
    EditText mailEditText;
    Button insertBtn;
    DatabaseHandler myDatabaseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact_acitivity);
        nameEditText = (EditText) findViewById(R.id.addActivity_name_editText);
        phoneEditText = (EditText) findViewById(R.id.addActivity_phone_editText);
        mailEditText = (EditText) findViewById(R.id.addActivity_mail_editText);
        insertBtn = (Button)findViewById(R.id.addActivity_add_button);
        insertBtn.setOnClickListener(this);
    }

    @Override
    public  void onClick(View view) {
        if(isEmpty(nameEditText))
        {
            Toast.makeText(this, "Insert Your Name", Toast.LENGTH_SHORT).show();
            YoYo.with(Techniques.Tada)
                    .duration(700)
                    .repeat(5)
                    .playOn(findViewById(R.id.addActivity_name_editText));
            nameEditText.setFocusableInTouchMode(true);
            nameEditText.requestFocus();
        }
        else if (isEmpty(phoneEditText))
        {
            Toast.makeText(this, "Insert Your Phone", Toast.LENGTH_SHORT).show();
            YoYo.with(Techniques.Tada)
                    .duration(700)
                    .repeat(5)
                    .playOn(findViewById(R.id.addActivity_phone_editText));
            phoneEditText.setFocusableInTouchMode(true);
            phoneEditText.requestFocus();
        }
        else if (isEmpty(mailEditText))
        {
            Toast.makeText(this, "Insert Your Mail", Toast.LENGTH_SHORT).show();
            YoYo.with(Techniques.Tada)
                    .duration(700)
                    .repeat(5)
                    .playOn(findViewById(R.id.addActivity_mail_editText));
            mailEditText.setFocusableInTouchMode(true);
            mailEditText.requestFocus();
        }
        else {
            Contact contact = new Contact(nameEditText.getText().toString(),
                    phoneEditText.getText().toString(),
                    mailEditText.getText().toString());
            myDatabaseHandler = new DatabaseHandler(this);
            myDatabaseHandler.addContact(contact);
           finish();

        }

    }

    public static boolean isEmpty(EditText checkEdt)
    {
        if(checkEdt.getText().toString().trim().length()>0)
            return false;
        return true;
    }
}
