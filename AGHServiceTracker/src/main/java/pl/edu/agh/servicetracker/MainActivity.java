package pl.edu.agh.servicetracker;

import android.app.Activity;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import pl.edu.agh.servicetracker.request.Item;
import pl.edu.agh.servicetracker.request.MockRequestService;


public class MainActivity extends Activity {

    public static final String ITEM_ID = "item_id";

    private Button newRequestButton;

    private Button scanQRCodeButton;

    private Button myRequestsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initFields();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initFields() {
        newRequestButton = (Button) findViewById(R.id.new_request);
        scanQRCodeButton = (Button) findViewById(R.id.scan_qr_code);
        myRequestsButton = (Button) findViewById(R.id.my_requests);
        newRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, NewRequestActivity.class));
            }
        });
        scanQRCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new IntentIntegrator(MainActivity.this).initiateScan();
            }
        });
        myRequestsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ServiceRequestListActivity.class));
            }
        });

    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanResult != null) {
            // handle scan result
            try {
                long itemId = Long.parseLong(scanResult.getContents().split(" ")[1].split(":")[1]);
                Intent newRequestIntent = new Intent(MainActivity.this, NewRequestActivity.class);
                newRequestIntent.putExtra(ITEM_ID, itemId);
                startActivity(newRequestIntent);
            } catch(NullPointerException | ArrayIndexOutOfBoundsException | NumberFormatException e) {
                Log.d("SCAN_RESULT", "INVALID FORMAT");
            }
        }
    }
}
