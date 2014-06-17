package pl.edu.agh.servicetracker;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import pl.edu.agh.servicetracker.request.MockRequestService;
import pl.edu.agh.servicetracker.request.RequestStatus;
import pl.edu.agh.servicetracker.request.ServiceRequest;

import java.text.DateFormat;

/**
 * A fragment representing a single ServiceRequest detail screen.
 * This fragment is either contained in a {@link ServiceRequestListActivity}
 * in two-pane mode (on tablets) or a {@link ServiceRequestDetailActivity}
 * on handsets.
 */
public class ServiceRequestDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private ServiceRequest serviceRequest;

    TextView requestName;

    TextView requestItem;

    TextView requestDescription;

    TextView requestDateCreated;

    TextView requestLastUpdated;

    TextView requestStatus;

    TextView requestResponseLabel;

    TextView requestResponse;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ServiceRequestDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {

            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            long itemId = getArguments().getLong(ARG_ITEM_ID);
            if (itemId != -1L) {
                serviceRequest = MockRequestService.getRequestById(itemId);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_servicerequest_detail, container, false);
        initFields(rootView);
        if (serviceRequest != null) {
            setFieldValues(serviceRequest);
        }
        return rootView;
    }


    private void initFields(View rootView) {
        requestName = (TextView) rootView.findViewById(R.id.servicerequest_name);
        requestItem = (TextView) rootView.findViewById(R.id.servicerequest_item);
        requestDescription = (TextView) rootView.findViewById(R.id.servicerequest_description);
        requestDateCreated = (TextView) rootView.findViewById(R.id.servicerequest_date_created);
        requestLastUpdated = (TextView) rootView.findViewById(R.id.servicerequest_last_modified);
        requestStatus = (TextView) rootView.findViewById(R.id.servicerequest_status);
        requestResponseLabel = (TextView) rootView.findViewById(R.id.servicerequest_response_label);
        requestResponse = (TextView) rootView.findViewById(R.id.servicerequest_response);
    }

    private void setFieldValues(ServiceRequest serviceRequest) {
        requestName.setText(serviceRequest.toString());
        requestItem.setText(serviceRequest.getItem().toString());
        requestDescription.setText(serviceRequest.getDescription());
        requestDateCreated.setText(DateFormat.getInstance().format(serviceRequest.getDateCreated()));
        requestLastUpdated.setText(DateFormat.getInstance().format(serviceRequest.getLastModified()));
        requestStatus.setText(serviceRequest.getStatus().name());
        if (serviceRequest.getStatus() == RequestStatus.RESOLVED) {
            requestResponse.setText(serviceRequest.getDescription());
        } else {
            requestResponse.setVisibility(View.GONE);
            requestResponseLabel.setVisibility(View.GONE);
        }
    }
}
