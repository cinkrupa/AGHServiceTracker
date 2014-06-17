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
import android.widget.EditText;
import pl.edu.agh.servicetracker.request.Item;
import pl.edu.agh.servicetracker.request.MockRequestService;
import pl.edu.agh.servicetracker.validation.FormUtils;

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
            Item item = MockRequestService.getItemById(itemId);
            newRequestFragment.setFieldValues(item);
        }
    }
}
