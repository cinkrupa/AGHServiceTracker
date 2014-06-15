package pl.edu.agh.servicetracker.request;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

public class MockRequestService {

    public static final Long MOCK_ID = 123L;

    public static Item getItemById(Long id) {
        return new Item(id, "NEC L102W LED", "Video projectors", "3.27a");
    }

    public static ServiceRequest getRequestById(Long id) {
        return new ServiceRequest(id, getItemById(MOCK_ID), "The projector is not working", new Date(), new Date(), RequestStatus.NEW);

    }

    public static Collection<ServiceRequest> getRequestsByUser(String username) {
        return Arrays.asList(new ServiceRequest[] { getRequestById(MOCK_ID) });
    }

    public static void sendRequest(Item item, String description) {

    }
}
