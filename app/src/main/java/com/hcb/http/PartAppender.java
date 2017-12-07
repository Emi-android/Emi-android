package com.hcb.http;

import okhttp3.MultipartBody;

public interface PartAppender {

    void addMoreParts(MultipartBody.Builder builder);
}
