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
import android.content.Context;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.json.JSONException;
import org.json.JSONObject;
import pl.edu.agh.servicetracker.R;
import pl.edu.agh.servicetracker.auth.AuthPreferencesUtil;

import java.net.URI;

public class AuthService {

    public static final String EMAIL = "email";

    public static final String TOKEN = "token";

    public static final String TOKEN_VALID = "tokenValid";

    public static void generateToken(Context context, String email, UiCallback<Void> uiCallback) {

        HttpPost httpPost = new HttpPost(URI.create(context.getString(R.string.baseUri) + context.getString(R
                .string.postNewToken)));

        httpPost.setHeader(EMAIL, email);

        new HttpRequestTask<>(uiCallback).execute(httpPost);
    }

    public static void isTokenValid(Activity activity, UiCallback<Boolean> uiCallback) {
        isTokenValid(activity, AuthPreferencesUtil.getToken(activity), uiCallback);
    }

    public static void isTokenValid(Context context, String token, UiCallback<Boolean> uiCallback) {

        HttpGet httpGet = new HttpGet(URI.create(context.getString(R.string.baseUri) + context.getString(R.string
                .getTokenValidity)));
        httpGet.setHeader(TOKEN, token);

        new HttpRequestTask<Boolean>(uiCallback) {
            @Override
            public Boolean buildResult(String jsonString) throws JSONException {
                JSONObject jsonObject = new JSONObject(jsonString);
                return jsonObject.getBoolean(TOKEN_VALID);
            }
        }.execute(httpGet);
    }

    public static void invalidateToken(Activity activity, UiCallback<Void> uiCallback) {

        HttpDelete httpDelete = new HttpDelete(URI.create(activity.getString(R.string.baseUri) + activity.getString(R
                .string.deleteToken)));
        httpDelete.setHeader(TOKEN, AuthPreferencesUtil.getToken(activity));

        new HttpRequestTask<>(uiCallback).execute(httpDelete);
    }

}
