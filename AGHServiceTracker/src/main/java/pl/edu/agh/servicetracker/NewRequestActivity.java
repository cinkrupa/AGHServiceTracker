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
import android.os.Bundle;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import pl.edu.agh.servicetracker.request.Item;
import pl.edu.agh.servicetracker.service.RequestService;
import pl.edu.agh.servicetracker.service.UiCallback;

public class NewRequestActivity extends Activity {

    NewRequestFragment newRequestFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_request);
        newRequestFragment = (NewRequestFragment) getFragmentManager().findFragmentById(R.id.new_request);
        processIntent(getIntent());
    }

    private void processIntent(Intent intent) {
        long itemId = intent.getLongExtra(MainActivity.ITEM_ID, -1);
        if(itemId != -1) {
            RequestService.getItemById(this, itemId, new UiCallback<Item>() {

                @Override
                public void onSuccess(Item result) {
                    newRequestFragment.setFieldValues(result);
                }

                @Override
                public void onError() {
                    Crouton.makeText(NewRequestActivity.this, NewRequestActivity.this.getString(R.string
                            .connection_error), Style.ALERT).show();
                }
            });
        }
    }
}
