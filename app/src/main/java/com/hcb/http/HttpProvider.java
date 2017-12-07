package com.hcb.http;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hcb.util.LoggerUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.atomic.AtomicBoolean;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okio.Buffer;

public class HttpProvider {

    private final static Logger LOG = LoggerFactory.getLogger(HttpProvider.class);

    private final static String JSON_PACKAGE = "json_package";

    private static final int TIMEOUT_SHORT = 5 * 1000;
    private static final int RETRY_SHORT = 1;
    private static final int TIMEOUT_LONG = 90 * 1000;
    private static final int RETRY_LONG = 0;

    public interface RespStringListener {
        void onResp(String str);
    }

    private final RequestQueue mRequestQueue;
    private AtomicBoolean stoped = new AtomicBoolean(false);

    public HttpProvider(Context ctx) {
        mRequestQueue = Volley.newRequestQueue(ctx, new OkHttpStack(new OkHttpClient()));
    }

    public void terminate() {
        LoggerUtil.t(LOG, "HttpProvider terminating...");
        mRequestQueue.stop();
        stoped.set(true);
    }

    public void get(final String uri, final RespStringListener listener) {
        if (stoped.get()) {
            LoggerUtil.d(LOG, "Http died, can't GET {}", uri);
            return;
        }
        LoggerUtil.t(LOG, "GET Req. uri={}", uri);
        final long t = System.currentTimeMillis();
        StringRequest req = new StringRequest(Request.Method.GET, uri, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (stoped.get()) {
                    LoggerUtil.d(LOG, "Http died, drop RESP of {}", uri);
                    return;
                }
                LoggerUtil.d(LOG, "Got Resp of {}", uri);
                LoggerUtil.t(LOG, "Cost {}ms", System.currentTimeMillis() - t);
                if (null != listener) {
                    listener.onResp(response);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (stoped.get()) {
                    LoggerUtil.t(LOG, "Http died, drop ERR of {}", uri);
                    return;
                }
                LOG.error("Error Getting {}", uri);
                LoggerUtil.t(LOG, "Cost {}ms", System.currentTimeMillis() - t);
                if (null != listener) {
                    listener.onResp(null);
                }
            }
        });
        req.setShouldCache(false);
        req.setRetryPolicy(new DefaultRetryPolicy(TIMEOUT_SHORT, RETRY_SHORT,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(req);
    }

    public void post(final String uri, final String str, final RespStringListener listener) {
        if (stoped.get()) {
            LoggerUtil.t(LOG, "Http died, can't POST {}", uri);
            return;
        }
        LoggerUtil.t(LOG, "POST Req. uri={}", uri);
        final long t = System.currentTimeMillis();
        JsonStrRequest req = new JsonStrRequest(uri, str, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (stoped.get()) {
                    LoggerUtil.t(LOG, "Http died, drop RESP of {}", uri);
                    return;
                }
                LoggerUtil.d(LOG, "Got Resp of {}", uri);
                LoggerUtil.t(LOG, "Cost {}ms", System.currentTimeMillis() - t);
                if (null != mRequestQueue && null != listener) {
                    listener.onResp(response);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (stoped.get()) {
                    LoggerUtil.t(LOG, "Http died, drop ERR of {}", uri);
                    return;
                }
                LOG.error("Error Posting {}", uri);
                LoggerUtil.t(LOG, "Cost {}ms", System.currentTimeMillis() - t);
                if (null != listener) {
                    listener.onResp(null);
                }
            }
        });
        mRequestQueue.add(req);
    }

    public void post(final String uri, final String str, final RespStringListener listener, final PartAppender appender) {
        if (stoped.get()) {
            LoggerUtil.t(LOG, "Http died, can't POST {}", uri);
            return;
        }
        LoggerUtil.t(LOG, "POST Req. uri={}", uri);
        final long t = System.currentTimeMillis();
        PostFormRequest req = new PostFormRequest(uri, str, appender, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (stoped.get()) {
                    LoggerUtil.t(LOG, "Http died, drop RESP of {}", uri);
                    return;
                }
                LoggerUtil.d(LOG, "Got Resp of {}", uri);
                LoggerUtil.t(LOG, "Cost {}ms", System.currentTimeMillis() - t);
                if (null != mRequestQueue && null != listener) {
                    listener.onResp(response);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (stoped.get()) {
                    LoggerUtil.t(LOG, "Http died, drop ERR of {}", uri);
                    return;
                }
                LOG.error("Error Posting {}", uri);
                LoggerUtil.t(LOG, "Cost {}ms", System.currentTimeMillis() - t);
                if (null != listener) {
                    listener.onResp(null);
                }
            }
        });
        mRequestQueue.add(req);
    }

    private class JsonStrRequest extends Request<String> {

        private String txtPart;
        private Response.Listener<String> mListener;

        public JsonStrRequest(String url,
                              String txtPart,
                              Response.Listener listener,
                              Response.ErrorListener errListener) {
            super(Method.POST, url, errListener);
            this.txtPart = txtPart;
            this.mListener = listener;
            setShouldCache(false);
            setRetryPolicy(new DefaultRetryPolicy(TIMEOUT_SHORT, RETRY_SHORT,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        }

        @Override
        public byte[] getBody() throws AuthFailureError {
            if (txtPart == null) {
                return super.getBody();
            }
            return (JSON_PACKAGE + "=" + txtPart).getBytes();
        }

        @Override
        protected Response<String> parseNetworkResponse(NetworkResponse response) {
            try {
                String jsonString = new String(response.data,
                        HttpHeaderParser.parseCharset(response.headers));
                return Response.success(jsonString,
                        HttpHeaderParser.parseCacheHeaders(response));
            } catch (UnsupportedEncodingException e) {
                return Response.error(new ParseError(e));
            }
        }

        @Override
        protected void deliverResponse(String response) {
            mListener.onResponse(response);
        }

    }

    private final static String MULTIPART_FORM_DATA = "multipart/form-data";
    private final static String BOUNDARY = "----GlobalHcbAndroidClient";
    private final static String REQBODY_CONTENT_TYPE
            = MULTIPART_FORM_DATA + "; boundary=" + BOUNDARY;

    private class PostFormRequest extends Request<String> {

        private String txtPart;
        private PartAppender appender;
        private Response.Listener<String> mListener;

        public PostFormRequest(String url,
                               String txtPart,
                               PartAppender appender,
                               Response.Listener listener,
                               Response.ErrorListener errListener) {
            super(Method.POST, url, errListener);
            this.txtPart = txtPart;
            this.appender = appender;
            this.mListener = listener;
            setShouldCache(false);
            if (null != appender) {
                setRetryPolicy(new DefaultRetryPolicy(TIMEOUT_LONG, RETRY_LONG,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            } else {
                setRetryPolicy(new DefaultRetryPolicy(TIMEOUT_SHORT, RETRY_SHORT,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            }
        }

        @Override
        public String getBodyContentType() {
            return REQBODY_CONTENT_TYPE;
        }

        @Override
        public byte[] getBody() throws AuthFailureError {
            if (txtPart == null) {
                return super.getBody();
            }
            final MultipartBody.Builder builder = new MultipartBody.Builder(BOUNDARY);
            builder.setType(MultipartBody.FORM);
            builder.addFormDataPart(JSON_PACKAGE, txtPart);
            if (null != appender) {
                appender.addMoreParts(builder);
            }
            final Buffer sink = new Buffer();
            try {
                builder.build().writeTo(sink);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return sink.readByteArray();
        }

        @Override
        protected Response<String> parseNetworkResponse(NetworkResponse response) {
            try {
                String jsonString = new String(response.data,
                        HttpHeaderParser.parseCharset(response.headers));
                return Response.success(jsonString,
                        HttpHeaderParser.parseCacheHeaders(response));
            } catch (UnsupportedEncodingException e) {
                return Response.error(new ParseError(e));
            }
        }

        @Override
        protected void deliverResponse(String response) {
            mListener.onResponse(response);
        }

    }

}