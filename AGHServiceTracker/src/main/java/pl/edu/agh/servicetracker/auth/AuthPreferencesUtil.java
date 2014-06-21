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

package pl.edu.agh.servicetracker.auth;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class AuthPreferencesUtil {

    private static final String PREFERENCES_FILE_NAME = "auth_preferences";

    private static final String EMAIL = "email";

    private static final String TOKEN = "token";

    private static final String TOKEN_SENT = "token_sent";

    public static UserCredentials getUserCredentials(Activity activity) throws UserNotInitializedException {
        SharedPreferences preferences = activity.getSharedPreferences(PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
        UserCredentials userCredentials = new UserCredentials(preferences.getString(EMAIL, null), preferences.getString(TOKEN,
                null));
        if (userCredentials.getEmail() == null || userCredentials.getToken() == null) {
            throw new UserNotInitializedException();
        }
        return userCredentials;
    }

    public static void saveUserCredentials(Activity activity, UserCredentials userCredentials) {
        SharedPreferences preferences = activity.getSharedPreferences(PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor preferencesEditor = preferences.edit();
        preferencesEditor.putString(EMAIL, userCredentials.getEmail());
        preferencesEditor.putString(TOKEN, userCredentials.getToken());
        preferencesEditor.remove(TOKEN_SENT);
        preferencesEditor.commit();
    }

    public static boolean isTokenSent(Activity activity) {
        SharedPreferences preferences = activity.getSharedPreferences(PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
        return preferences.getBoolean(TOKEN_SENT, false) && preferences.getString(EMAIL, null) != null;
    }

    public static String getEmail(Activity activity) {
        SharedPreferences preferences = activity.getSharedPreferences(PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
        return preferences.getString(EMAIL, null);
    }

    public static void onTokenSent(Activity activity, String email) {
        SharedPreferences preferences = activity.getSharedPreferences(PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor preferencesEditor = preferences.edit();
        preferencesEditor.remove(TOKEN);
        preferencesEditor.putString(EMAIL, email);
        preferencesEditor.putBoolean(TOKEN_SENT, true);
        preferencesEditor.commit();
    }

    public static void clear(Activity activity) {
        SharedPreferences preferences = activity.getSharedPreferences(PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor preferencesEditor = preferences.edit();
        preferencesEditor.remove(EMAIL);
        preferencesEditor.remove(TOKEN);
        preferencesEditor.remove(TOKEN_SENT);
        preferencesEditor.commit();
    }

}
