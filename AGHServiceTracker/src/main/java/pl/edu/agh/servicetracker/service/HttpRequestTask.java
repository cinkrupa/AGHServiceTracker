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

import android.os.AsyncTask;
import android.util.Log;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;

import java.io.IOException;

public class HttpRequestTask<T> extends AsyncTask<HttpUriRequest, Void, HttpRequestTask.ResultWrapper<T>> {

    public static final String CONTENT_TYPE = "Content-Type";

    public static final String APPLICATION_JSON = "application/json; charset=utf-8";

    private UiCallback<T> uiCallback;

    public HttpRequestTask(UiCallback<T> uiCallback) {
        this.uiCallback = uiCallback;
    }

    public T buildResult(String jsonString) throws JSONException {
        return null;
    }

    ;

    @Override
    protected final ResultWrapper<T> doInBackground(HttpUriRequest... httpUriRequests) {
        HttpUriRequest httpUriRequest = httpUriRequests[0];
        HttpStatus httpStatus = null;
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpResponse httpResponse = httpClient.execute(httpUriRequest);
            httpStatus = HttpStatus.fromStatusCode(httpResponse.getStatusLine().getStatusCode());
            if (httpResponse.containsHeader(CONTENT_TYPE) && APPLICATION_JSON.equals(httpResponse.getFirstHeader
                    (CONTENT_TYPE).getValue())) {
                return new ResultWrapper(httpStatus, buildResult(EntityUtils.toString(httpResponse.getEntity())));
            }
        } catch (IOException | JSONException e) {
            Log.d("HttpRequestTask", e.getMessage(), e);
        }
        return new ResultWrapper(httpStatus, null);
    }

    @Override
    protected final void onPostExecute(ResultWrapper<T> resultWrapper) {

        HttpStatus status = resultWrapper.getStatus();
        if (status != null) {
            if (status.getFamily() == HttpStatus.Family.SUCCESSFUL) {
                uiCallback.onSuccess(resultWrapper.getResult());
            } else {
                uiCallback.onError(String.format("%d - %s", status.getStatusCode(), status.getReasonPhrase()));
            }
        } else {
            uiCallback.onError("Unknown error");
        }
    }

    static class ResultWrapper<T> {

        private HttpStatus status;

        private T result;

        public ResultWrapper(HttpStatus status, T result) {
            this.status = status;
            this.result = result;
        }

        public HttpStatus getStatus() { return status; }

        public T getResult() { return result; }

    }

}
