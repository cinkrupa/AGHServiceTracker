/*
 * Copyright (C) 2014  Marcin Krupa
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package pl.edu.agh.servicetracker;

import android.app.Activity;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.widget.EditText;
import org.json.JSONException;
import org.json.JSONObject;
import pl.edu.agh.servicetracker.request.Item;
import pl.edu.agh.servicetracker.request.MockRequestService;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

public class NFCRequestActivity extends Activity {

    NewRequestFragment newRequestFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc_request);
        newRequestFragment = (NewRequestFragment) getFragmentManager().findFragmentById(R.id.new_request);
    }

    public void onResume() {
        super.onResume();
        Item item = processIntent(getIntent());
        if(item != null) {
            newRequestFragment.setFieldValues(item);
        }

    }

    private Item processIntent(Intent intent) {
        if(intent != null) {
            NdefMessage[] ndefMessages = null;
            if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
                for (Parcelable parcelable : intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)) {
                    NdefMessage ndefMessage = (NdefMessage) parcelable;
                    Log.d("NDEF", "ndefMessage.toString: " + ndefMessage.toString());
                    for (NdefRecord ndefRecord : ndefMessage.getRecords()) {
                        try {
                            Log.d("NDEF", "ndefRecord.toString: " + ndefRecord.toString());
                            Log.d("NDEF", "ndefRecord.getPayload: " + new String(ndefRecord.getPayload(), "UTF-8"));
                            Log.d("NDEF", "ndefRecord.getType: " + new String(ndefRecord.getType()));
                            Log.d("NDEF", "ndefRecord.toMimeType: " + ndefRecord.toMimeType());
                            if(getString(R.string.ndef_mime_type).equals(ndefRecord.toMimeType())) {
                                JSONObject jsonObject = new JSONObject(new String(ndefRecord.getPayload(), "UTF-8"));
                                Long itemId = jsonObject.getLong(getString(R.string.item_id));
                                if(itemId != null) {
                                    return MockRequestService.getItemById(itemId);
                                }

                            }
                        } catch(UnsupportedEncodingException | JSONException e) {
                            Log.d("NDEF", e.getClass().getSimpleName() + ": " + e.getMessage());
                        }
                    }
                }
            }

        }
        return null;
    }



}
