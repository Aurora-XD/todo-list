package com.thoughtworks.todo_list.repository.utils;

import android.util.Log;

import com.google.gson.Gson;
import com.thoughtworks.todo_list.repository.user.entity.User;

import java.io.IOException;
import java.util.Objects;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HttpUtil {

    private static final String PATH = "https://twc-android-bootcamp.github.io/fake-data/data/user.json";

    public static User getUserFromNetwork() throws IOException {
        Response response = new OkHttpClient().newCall(new Request.Builder().url(PATH).build()).execute();
        Gson gson = new Gson();
        User user = gson.fromJson(Objects.requireNonNull(response.body()).string(), User.class);
        Log.d("TAG", "getUserFromNetwork: "+user.getName()+user.getPassword());
        return user;
    }
}
