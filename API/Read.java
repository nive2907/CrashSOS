package com.example.roadsaftey;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View;
import android.widget.AdapterView;
//import android.widget.Toast;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
public class Read extends ActionBarActivity {
    String log;
    String x[];
    int a=0;
    String number[];
    int i=0;
    private ListView mainListView ;
    public ArrayAdapter<String> listAdapter ;
    List<Contact> contacts;
    DatabaseHandler db = new DatabaseHandler(this);
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);
        mainListView = (ListView) findViewById( R.id.mainListView );
        // DatabaseHandler db = new DatabaseHandler(this);

        /**
         * CRUD Operations
         * */


        ArrayList<String> planetList = new ArrayList<String>();

        // Reading all contacts

        contacts = db.getAllContacts();

        for (Contact cn : contacts) {
            log = "Phone: " + cn.getPhoneNumber();


            // Writing Contacts to log
            planetList.addAll( Arrays.asList(log) );
        }

        listAdapter = new ArrayAdapter<String>(this, R.layout.simplerow, planetList);
        mainListView.setAdapter( listAdapter );
        registerForContextMenu(mainListView);
    }
    public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        menu.add(0, v.getId(), 0, "DELETE");

    }

    @Override
    public boolean onContextItemSelected(MenuItem item)
    {



        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        try
        {

            //int IndexSelected=info.position;

            //  info.position will give the index of selected item

            if(item.getTitle()=="DELETE")
            {
                Contact cn=new Contact();
                String s = listAdapter.getItem(info.position);
                String num = s.substring(s.indexOf(":")+1);
                db.deleteContact(new Contact(cn._id,num));

                listAdapter.remove(listAdapter.getItem(info.position));

                listAdapter.notifyDataSetChanged();



            }
            else
            {
                return false;
            }

            return true;


        }
        catch(Exception e)
        {
            return true;
        }
    }

}

