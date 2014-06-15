package pl.edu.agh.servicetracker;

import android.app.Activity;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.EditText;
import pl.edu.agh.servicetracker.request.Item;
import pl.edu.agh.servicetracker.request.MockRequestService;

public class NFCRequestActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc_request);
    }

    public void onResume() {
        super.onResume();
        processIntent();

    }

    private void processIntent() {
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
            Parcelable[] rawMsgs = getIntent().getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            if (rawMsgs != null) {
                NdefMessage[] msgs = new NdefMessage[rawMsgs.length];
                for (int i = 0; i < rawMsgs.length; i++) {
                    msgs[i] = (NdefMessage) rawMsgs[i];
                }
            }
        }
        //process the msgs array

        Long itemId = MockRequestService.MOCK_ID;
        Item item = MockRequestService.getItemById(itemId);
        setFieldValues(item);

    }

    private void setFieldValues(Item item) {
        EditText name = (EditText) findViewById(R.id.name);
        EditText category = (EditText) findViewById(R.id.category);
        EditText location = (EditText) findViewById(R.id.location);
        name.setText(item.getName());
        category.setText(item.getCategory());
        location.setText(item.getLocation());
        name.setEnabled(false);
        category.setEnabled(false);
        location.setEnabled(false);
    }

}
