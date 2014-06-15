package pl.edu.agh.servicetracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import pl.edu.agh.servicetracker.request.Item;
import pl.edu.agh.servicetracker.request.MockRequestService;

public class NewRequestActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_request);

        Intent intent = getIntent();
        Long itemId = intent.getLongExtra(MainActivity.ITEM_ID, -1);
        if(itemId != -1) {
            Item item = MockRequestService.getItemById(itemId);
            setFieldValues(item);
        }
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
