package pl.edu.agh.servicetracker;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class RequestListActivity extends Activity {

    private ListView requestList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_list);

        requestList = (ListView) findViewById(R.id.request_list);
        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                new String[] {"Request #000001", "Request #000002", "Request #000003"}
        );
        requestList.setAdapter(adapter);
    }

}
