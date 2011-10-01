package com.darwinsys.tabbytext;

import java.util.Set;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
    			// retrieve the Contact
    			Bundle ret = data.getExtras();
    			if (ret != null) {
    				Set<String> keys = ret.keySet();
    				Log.d("ret", "Extras: Key size is "+ keys.size());
    				for (String key : keys) {
    					Log.d("ret", ret.get(key).toString());
    				}
    			}
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