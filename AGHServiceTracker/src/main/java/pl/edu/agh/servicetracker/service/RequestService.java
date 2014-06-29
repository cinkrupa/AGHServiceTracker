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

package pl.edu.agh.servicetracker.service;

import android.app.Activity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import pl.edu.agh.servicetracker.R;
import pl.edu.agh.servicetracker.auth.AuthPreferencesUtil;
import pl.edu.agh.servicetracker.request.Item;
import pl.edu.agh.servicetracker.request.RequestStatus;
import pl.edu.agh.servicetracker.request.ServiceRequest;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RequestService {

    public static void getItemById(Activity activity, Long id, UiCallback<Item> uiCallback) {

        HttpGet httpGet = new HttpGet(URI.create(activity.getString(R.string.baseUri) + String.format(activity
                .getString(R.string.getAsset), id)));
        httpGet.setHeader(AuthService.TOKEN, AuthPreferencesUtil.getToken(activity));

        new HttpRequestTask<Item>(uiCallback) {
            @Override
            public Item buildResult(String jsonString) throws JSONException {
                JSONObject jsonObject = new JSONObject(jsonString);
                Item item = new Item();
                JSONObject asset = jsonObject.getJSONObject("asset");
                item.setId(asset.getLong("id"));
                item.setName(asset.getString("name"));
                item.setCategory(asset.getString("category"));
                item.setLocation(asset.getString("location"));
                JSONArray typicalBreakdownsArray = jsonObject.getJSONArray("typicalBreakDowns");
                List<String> typicalBreakdowns = new ArrayList<String>();
                for (int i = 0; i < typicalBreakdownsArray.length(); ++i) {
                    typicalBreakdowns.add(typicalBreakdownsArray.getString(i));
                }
                item.setTypicalBreakdowns(typicalBreakdowns);
                return item;
            }
        }.execute(httpGet);
    }

    public static void getRequestsByUser(Activity activity, UiCallback<List<ServiceRequest>> uiCallback) {
        HttpGet httpGet = new HttpGet(URI.create(activity.getString(R.string.baseUri) + activity.getString(R.string
                .getIssues)));
        httpGet.setHeader(AuthService.TOKEN, AuthPreferencesUtil.getToken(activity));

        new HttpRequestTask<List<ServiceRequest>>(uiCallback) {
            @Override
            public List<ServiceRequest> buildResult(String jsonString) throws JSONException {
                JSONArray jsonArray = new JSONArray(jsonString);
                List<ServiceRequest> serviceRequests = new ArrayList<ServiceRequest>();
                for (int i = 0; i < jsonArray.length(); ++i) {
                    ServiceRequest serviceRequest = new ServiceRequest();
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    {
                        Item item = new Item();
                        JSONObject asset = jsonObject.getJSONObject("asset");
                        item.setName(asset.getString("name"));
                        item.setCategory(asset.getString("category"));
                        item.setLocation(asset.getString("location"));
                        serviceRequest.setItem(item);
                    }
                    serviceRequest.setDescription(jsonObject.getString("breakDown"));
                    serviceRequest.setDateCreated(new Date(jsonObject.getLong("dateCreated")));
                    serviceRequest.setLastModified(new Date(jsonObject.getLong("lastModified")));
                    serviceRequest.setStatus(RequestStatus.valueOf(jsonObject.getString("issueStatus")));
                    serviceRequest.setResponse(jsonObject.getString("response"));
                    serviceRequests.add(serviceRequest);
                }
                return serviceRequests;
            }
        }.execute(httpGet);
    }

    public static void sendRequest(Activity activity, Item item, String description, UiCallback<Void> uiCallback) {

        HttpPost httpPost = new HttpPost(URI.create(activity.getString(R.string.baseUri) + activity.getString(R
                .string.postIssue)));
        httpPost.setHeader(AuthService.TOKEN, AuthPreferencesUtil.getToken(activity));
        httpPost.setHeader(HttpRequestTask.CONTENT_TYPE, HttpRequestTask.APPLICATION_JSON);
        try {
            JSONObject jsonObject = new JSONObject();
            JSONObject asset = new JSONObject();
            asset.put("id", item.getId());
            asset.put("name", item.getName());
            asset.put("category", item.getCategory());
            asset.put("location", item.getLocation());
            jsonObject.put("asset", asset);
            jsonObject.put("breakDown", description);

            StringEntity entity = new StringEntity(jsonObject.toString(), HTTP.UTF_8);
            entity.setContentType(HttpRequestTask.APPLICATION_JSON);
            httpPost.setEntity(entity);
            new HttpRequestTask<>(uiCallback).execute(httpPost);

        } catch (JSONException | UnsupportedEncodingException e) {
            uiCallback.onError(e.getMessage());
        }

    }
}
