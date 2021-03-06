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

package pl.edu.agh.servicetracker.request;

import java.text.DateFormat;
import java.util.Date;

public class ServiceRequest {

    private Long id;

    private Item item;

    private String description;

    private Date dateCreated;

    private Date lastModified;

    private RequestStatus status;

    private String response;

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

    public void setId(Long id) {
        this.id = id;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    @Override
    public String toString() { return DateFormat.getInstance().format(dateCreated) + " - " + item.getName(); }
}
