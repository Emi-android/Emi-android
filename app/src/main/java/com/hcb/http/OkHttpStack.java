package com.hcb.http;

import com.android.volley.toolbox.HurlStack;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.OkHttpClient;
import okhttp3.OkUrlFactory;

public class OkHttpStack extends HurlStack {

    private final OkUrlFactory okFactory;

    public OkHttpStack(OkHttpClient client) {
        this.okFactory = new OkUrlFactory(client);
    }

    @Override
    protected HttpURLConnection createConnection(URL url) throws IOException {
        return okFactory.open(url);
    }

}
