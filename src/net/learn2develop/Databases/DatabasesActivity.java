package net.learn2develop.Databases;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class DatabasesActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        DBAdapter db = new DBAdapter(this);

        /*
        //---add a contact---
        db.open();
        long id = db.insertContact("Wei-Meng Lee", "weimenglee@learn2develop.net");
        id = db.insertContact("Mary Jackson", "mary@jackson.com");
        db.close();
        */
        
        
        //--get all contacts---
		addToListView(db);
        
        
        /*
        //---get a contact---
        db.open();
        Cursor c = db.getContact(2);
        if (c.moveToFirst())        
            DisplayContact(c);
        else
            Toast.makeText(this, "No contact found", Toast.LENGTH_LONG).show();
        db.close();     
        */
        
        /*
        //---update contact---
        db.open();
        if (db.updateContact(1, "Wei-Meng Lee", "weimenglee@gmail.com"))
            Toast.makeText(this, "Update successful.", Toast.LENGTH_LONG).show();
        else
            Toast.makeText(this, "Update failed.", Toast.LENGTH_LONG).show();
        db.close();
        */
        
        /*
        //---delete a contact---
        db.open();
        if (db.deleteContact(1))
            Toast.makeText(this, "Delete successful.", Toast.LENGTH_LONG).show();
        else
            Toast.makeText(this, "Delete failed.", Toast.LENGTH_LONG).show();
        db.close();
        */
        
        try {
            String destPath = "/data/data/" + getPackageName() + "/databases";
            File f = new File(destPath);
            if (!f.exists()) {            	
            	f.mkdirs();
                f.createNewFile();
            	
            	//---copy the db from the assets folder into 
            	// the databases folder---
                CopyDB(getBaseContext().getAssets().open("mydb"),
                    new FileOutputStream(destPath + "/MyDB"));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
       
        //---get all contacts---
        db.open();
        Cursor c = db.getAllContacts();
        if (c.moveToFirst()){
            do {
                DisplayContact(c);
            } while (c.moveToNext());
        }
        db.close();
    } // end of onCreate()
    
    //=================================================================
    // AddToListView()
    //  0. accepts a dbadapter
    //  2. extract all the rows
    //  4. pass all the rows to Activity.startManagingCursor()
    //  6. extract all columns from dbadapter and put them into an array
    // 10. extract all the textViews and put them in an array
    // 15. create a simple_cursor_adapter (sca) and initialize it
    // 20. create a list_view
    // 22. pass the sca to lv
    //=================================================================
    @SuppressWarnings("deprecation")
	private void addToListView(DBAdapter db) {//0 

    	Cursor c = db.getAllContacts(); // 2
    	startManagingCursor(c); // 4

    	String [] from = { // 6
    			DBAdapter.KEY_ROWID, 
    			DBAdapter.KEY_NAME, 
    			DBAdapter.KEY_EMAIL 
    			};
    	int [] to = { // 10
    			R.id.textView1, 
    			R.id.textView2, 
    			R.id.textView3 
    			};

    	SimpleCursorAdapter sca = 
    			new SimpleCursorAdapter( // 15
    					this,
    					R.layout.person,
    					c,
    					from,
    					to); 

    	ListView lv = (ListView) findViewById(android.R.id.list); // 20

    	lv.setAdapter(sca); // 22
    }
    
    //=================================================================
    // copyDB() - copy db files?
    //=================================================================
    public void CopyDB(InputStream inputStream,
    OutputStream outputStream) throws IOException {
        //---copy 1K bytes at a time---
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, length);
        }
        inputStream.close();
        outputStream.close();
    }
    
    //=================================================================
    // DisplayContact() - Display row
    //=================================================================
    public void DisplayContact(Cursor c){
    	Toast.makeText(this,
                "id: " + c.getString(0) + "\n" +
                "Name: " + c.getString(1) + "\n" +
                "Email:  " + c.getString(2),
                Toast.LENGTH_LONG).show();
    }
}// end of Activity