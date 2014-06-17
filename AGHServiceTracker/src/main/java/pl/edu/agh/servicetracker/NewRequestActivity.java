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
