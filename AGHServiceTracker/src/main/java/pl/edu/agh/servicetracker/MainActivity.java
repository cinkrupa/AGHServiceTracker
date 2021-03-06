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
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import pl.edu.agh.servicetracker.auth.AuthPreferencesUtil;
import pl.edu.agh.servicetracker.service.AuthService;
import pl.edu.agh.servicetracker.service.UiCallback;


public class MainActivity extends Activity {

    public static final String ITEM_ID = "item_id";

    private Button newRequestButton;

    private Button scanQRCodeButton;

    private Button myRequestsButton;

    private Crouton errorMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String token = AuthPreferencesUtil.getToken(this);
        if (token != null) {

            final ProgressDialog progressDialog = ProgressDialog.show(this, "",
                    getString(R.string.checking_token_validity));
            AuthService.isTokenValid(this, new UiCallback<Boolean>() {
                @Override
                public void onSuccess(Boolean result) {
                    progressDialog.dismiss();
                    if (result != null && result) {
                        setContentView(R.layout.activity_main);
                        initFields();
                    } else {
                        startActivity(new Intent(MainActivity.this, SendTokenActivity.class));
                    }
                }

                @Override
                public void onError(String message) {
                    progressDialog.dismiss();
                    errorMessage = Crouton.makeText(MainActivity.this, String.format("%s: %s",
                            MainActivity.this.getString(R.string.connection_error), message), Style.ALERT);
                    errorMessage.show();
                }
            });
        } else {
            if (AuthPreferencesUtil.isTokenSent(this)) {
                startActivity(new Intent(MainActivity.this, ProvideTokenActivity.class));
            } else {
                startActivity(new Intent(MainActivity.this, SendTokenActivity.class));
            }
        }
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
        if (id == R.id.invalidate_token) {

            if (errorMessage != null) {
                errorMessage.cancel();
                errorMessage = null;
            }

            final ProgressDialog progressDialog = ProgressDialog.show(this, "", getString(R.string.sending));

            AuthService.invalidateToken(this, new UiCallback<Void>() {
                @Override
                public void onSuccess(Void result) {
                    progressDialog.dismiss();
                    AuthPreferencesUtil.clear(MainActivity.this);
                    startActivity(new Intent(MainActivity.this, SendTokenActivity.class));
                }

                @Override
                public void onError(String message) {
                    progressDialog.dismiss();
                    errorMessage = Crouton.makeText(MainActivity.this, String.format("%s: %s",
                            MainActivity.this.getString(R.string.connection_error), message), Style.ALERT);
                    errorMessage.show();
                }
            });
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
            } catch (NullPointerException | ArrayIndexOutOfBoundsException | NumberFormatException e) {
                Log.d("SCAN_RESULT", "INVALID FORMAT");
            }
        }
    }
}
