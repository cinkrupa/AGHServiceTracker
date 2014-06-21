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
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import pl.edu.agh.servicetracker.auth.AuthPreferencesUtil;
import pl.edu.agh.servicetracker.auth.UserCredentials;
import pl.edu.agh.servicetracker.request.MockRequestService;
import pl.edu.agh.servicetracker.validation.Form;
import pl.edu.agh.servicetracker.validation.validator.NonEmptyValidator;


public class ProvideTokenActivity extends Activity {

    private EditText token;

    private Button authenticateButton;

    private Button resendTokenButton;

    private Button changeEmailButton;

    private Form form;

    private Crouton invalidTokenMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provide_token);
        initFields();
        initListeners();
    }



    private void initFields() {
        token = (EditText) findViewById(R.id.token);
        authenticateButton = (Button) findViewById(R.id.authenticateButton);
        resendTokenButton = (Button) findViewById(R.id.resendTokenButton);
        changeEmailButton = (Button) findViewById(R.id.changeEmailButton);
        form = new Form(this);
        form.field(token).validator(new NonEmptyValidator());
    }

    private void initListeners() {
        authenticateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateAndSubmit();
            }
        });
        resendTokenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resendToken();
            }
        });
        changeEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeEmail();
            }
        });
    }

    private void validateAndSubmit() {
        if(invalidTokenMessage != null) {
            invalidTokenMessage.cancel();
        }
        if (form.isValid()) {
            String email = AuthPreferencesUtil.getEmail(this);
            if(email == null) {
                changeEmail();
            }
            final ProgressDialog progressDialog = ProgressDialog.show(this, "", getString(R.string.loading));
            final UserCredentials userCredentials = new UserCredentials(email, token.getText().toString());

            new AsyncTask<Void, Void, Boolean>() {
                @Override
                protected Boolean doInBackground(Void... params) {
                    return MockRequestService.isTokenValid(userCredentials);
                }

                @Override
                protected void onPostExecute(Boolean isTokenValid) {
                    progressDialog.dismiss();
                    if(isTokenValid) {
                        AuthPreferencesUtil.saveUserCredentials(ProvideTokenActivity.this, userCredentials);
                        startActivity(new Intent(ProvideTokenActivity.this, MainActivity.class));
                    } else {
                        invalidTokenMessage = Form.showErrorMessage(ProvideTokenActivity.this, token, getString(R.string.invalid_token));
                    }
                }
            }.execute();
        }
    }

    private void resendToken() {
        final String email = AuthPreferencesUtil.getEmail(this);
        if(email == null) {
            changeEmail();
        }
        final ProgressDialog progressDialog = ProgressDialog.show(this, "", getString(R.string.sending));
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                MockRequestService.sendTokenRequest(email);
                AuthPreferencesUtil.onTokenSent(ProvideTokenActivity.this, email);
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                progressDialog.dismiss();
            }
        }.execute();
    }

    private void changeEmail() {
        startActivity(new Intent(this, SendTokenActivity.class));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.provide_token, menu);
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

    @Override
    public void onDestroy() {
        form.onDestroy();
        super.onDestroy();
    }
}
