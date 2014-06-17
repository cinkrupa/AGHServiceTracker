package pl.edu.agh.servicetracker.request;

import java.util.Date;

public class ServiceRequest {

    private Long id;

    private Item item;

    private String description;

    private Date dateCreated;

    private Date lastModified;

    private RequestStatus status;

    private String response;

    public ServiceRequest(Long id, Item item, String description, Date dateCreated, Date lastModified, RequestStatus status) {
        this.id = id;
        this.item = item;
        this.description = description;
        this.dateCreated = dateCreated;
        this.lastModified = lastModified;
        this.status = status;
    }

    public ServiceRequest(Long id, Item item, String description, Date dateCreated, Date lastModified,
                          RequestStatus status, String response) {
        this(id, item, description, dateCreated, lastModified, status);
        this.response = response;
    }

    public Long getId() {
        return id;
    }

    public Item getItem() {
        return item;
    }

    public String getDescription() {
        return description;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public String getResponse() { return response; }

    @Override
    public String toString() { return "#"+id; }
}
