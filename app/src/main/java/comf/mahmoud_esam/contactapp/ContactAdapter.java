package comf.mahmoud_esam.contactapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by mahmoud-esam on 6/1/2017.
 */

public class ContactAdapter extends ArrayAdapter {
    private ArrayList<Contact> contacts;
    private LayoutInflater inflater;
    Context context;

    public ContactAdapter(Context context, int resource, ArrayList<Contact> contacts) {
        super(context, resource, contacts);
        this.contacts = contacts;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (inflater == null) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.contact_list_item, null);
            convertView.setLongClickable(true);
        }
        TextView name = (TextView) convertView.findViewById(R.id.contact_list_item_name_textview);
        name.setText(contacts.get(position).getName().toString());
        Button sendSMS = (Button) convertView.findViewById(R.id.contact_list_item_message_button);
        sendSMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("smsto:" + contacts.get(position).getPhone().toString()));
                intent.putExtra("sms_body", "Hi you got a message !");
                intent.setType("vnd.android-dir/mms-sms");
                context.startActivity(Intent.createChooser(intent, "send SMS"));

//               SmsManager smsManager = SmsManager.getDefault();
//               smsManager.sendTextMessage(contacts.get(position).getPhone().toString(),null,"Hi you got a message !",null,null);

            }
        });
        Button call = (Button) convertView.findViewById(R.id.contact_list_item_call_button);
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + contacts.get(position).getPhone().toString()));
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                context.startActivity(intent);
            }
        });
        return convertView;
    }
}
