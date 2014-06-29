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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import pl.edu.agh.servicetracker.auth.AuthPreferencesUtil;
import pl.edu.agh.servicetracker.service.AuthService;
import pl.edu.agh.servicetracker.service.UiCallback;
import pl.edu.agh.servicetracker.validation.Form;
import pl.edu.agh.servicetracker.validation.validator.AghEmailValidator;
import pl.edu.agh.servicetracker.validation.validator.EmailValidator;


public class SendTokenActivity extends Activity {

    private EditText email;

    private Button sendTokenButton;

    private Form form;

    private Crouton errorMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_token);
        initFields();
        initListeners();
    }

    private void initFields() {
        email = (EditText) findViewById(R.id.email_address);
        sendTokenButton = (Button) findViewById(R.id.sendTokenButton);
        form = new Form(this);
        form.field(email).validator(new EmailValidator()).validator(new AghEmailValidator());
    }

    private void initListeners() {
        sendTokenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateAndSubmit();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.send_token, menu);
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

    private void validateAndSubmit() {
        if (errorMessage != null) {
            errorMessage.cancel();
            errorMessage = null;
        }
        if (form.isValid()) {
            final ProgressDialog progressDialog = ProgressDialog.show(this, "", getString(R.string.sending));
            final String emailValue = email.getText().toString();

            AuthService.generateToken(this, emailValue, new UiCallback<Void>() {

                @Override
                public void onSuccess(Void result) {
                    progressDialog.dismiss();
                    AuthPreferencesUtil.onTokenSent(SendTokenActivity.this, emailValue);
                    startActivity(new Intent(SendTokenActivity.this, ProvideTokenActivity.class));
                }

                @Override
                public void onError() {
                    progressDialog.dismiss();
                    errorMessage = Crouton.makeText(SendTokenActivity.this, SendTokenActivity.this.getString(R.string
                            .connection_error), Style.ALERT);
                    errorMessage.show();
                }
            });
        }
    }
}
