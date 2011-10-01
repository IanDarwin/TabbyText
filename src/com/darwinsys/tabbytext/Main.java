package com.darwinsys.tabbytext;

import java.util.Set;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Contacts.People;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Main extends Activity {
	private static final int REQ_GET_CONTACT = 1;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Button b = (Button) findViewById(R.id.contactChoose);
        b.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View arg0) {
				Uri uri = Uri.parse("content://contacts/people");
				Intent intent = new Intent(Intent.ACTION_PICK, uri);
				startActivityForResult(intent, REQ_GET_CONTACT);
			}
		});
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if (requestCode == REQ_GET_CONTACT) {
    		switch(resultCode) {
    		case Activity.RESULT_OK:
    			// The Contacts API is about the most complex to use.
    			// First we have to retrieve the Contact, since we only get its URI from the Intent
    			Uri resultUri = data.getData(); // e.g., content://contacts/people/123
    			Cursor cont = getContentResolver().query(resultUri, null, null, null, null);
    			if (!cont.moveToNext()) {	// expect 001 row(s)
    				Toast.makeText(this, "Cursor contains no data", Toast.LENGTH_LONG).show(); 
        			return;
    			}
    			int columnIndexForId = cont.getColumnIndex(ContactsContract.Contacts._ID);
    			String contactId = cont.getString(columnIndexForId);
    			int columnIndexForHasPhone = cont.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER);
    			boolean hasAnyPhone = Boolean.parseBoolean(cont.getString(columnIndexForHasPhone));
    			if (!hasAnyPhone) {
    				Toast.makeText(this, "Selected contact has no phone numbers ", Toast.LENGTH_LONG).show(); 
    				return;
    			}
    			// Now we have to do another query to actually get the numbers!
    			Cursor numbers = getContentResolver().query(
    					ContactsContract.CommonDataKinds.Phone.CONTENT_URI, 
    					null, 
    					ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + contactId, // "selection", 
    					null, null);
    			while (numbers.moveToNext()) {
    				String aNumber = numbers.getString(numbers.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
    				System.out.println(aNumber);
    				return;
    			}
    			Toast.makeText(this, "Selected contact has phone numbers but we didn't find them!", Toast.LENGTH_LONG).show(); 
    			break;
    		case Activity.RESULT_CANCELED:
    			// nothing to do here
    			break;
    		default:
    			Toast.makeText(this, "Unexpected resultCode: " + resultCode, Toast.LENGTH_LONG).show(); 
    			break;
    		}
    	}
    	super.onActivityResult(requestCode, resultCode, data);
    }
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater inf = getMenuInflater();
		inf.inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.settings:
			startActivity(new Intent(this, SettingsActivity.class));
			return true;
		case R.id.about:
			startActivity(new Intent(this, AboutActivity.class));
			return true;
		}
		return false;
	}
}