package pl.edu.agh.servicetracker.request;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MockRequestService {

    private static Random RANDOM = new Random();

    public static final Map<Long, Item> ITEMS_MAP = new HashMap<Long, Item>();

    public static final Map<Long, ServiceRequest> SERVICE_REQUESTS_MAP = new LinkedHashMap<Long, ServiceRequest>();

    static {
        ITEMS_MAP.put(1234L, new Item(1234L, "NEC L102W LED", "Video projectors", "3.27a"));
        ITEMS_MAP.put(1235L, new Item(1235L, "Mac mini", "Computers", "4.22"));
        ITEMS_MAP.put(1236L, new Item(1236L, "DELL U2412M", "Monitors", "3.11"));
        SERVICE_REQUESTS_MAP.put(2198L, new ServiceRequest(2198L, getItemById(1236L), "Dead pixels", new Date(),
                new Date(), RequestStatus.RESOLVED, "Monitor replaced"));
        SERVICE_REQUESTS_MAP.put(2256L, new ServiceRequest(2256L, getItemById(1235L), "Power cable missing",
                new Date(), new Date(), RequestStatus.ASSIGNED));
        SERVICE_REQUESTS_MAP.put(2314L, new ServiceRequest(2314L, getItemById(1234L), "The projector is not working",
                new Date(), new Date(), RequestStatus.NEW));
    }

    public static Item getItemById(Long id) {
        return ITEMS_MAP.get(id);
    }

    public static ServiceRequest getRequestById(Long id) {
        return SERVICE_REQUESTS_MAP.get(id);

    }

    public static Collection<ServiceRequest> getRequestsByUser(String username) {
        return SERVICE_REQUESTS_MAP.values();
    }

    public static void sendRequest(Item item, String description) {
        long id = 2500L + RANDOM.nextInt(500);
        SERVICE_REQUESTS_MAP.put(id, new ServiceRequest(id, item, description, new Date(), new Date(), RequestStatus.NEW));

    }
}
