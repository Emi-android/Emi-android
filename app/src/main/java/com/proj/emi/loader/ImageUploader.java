package com.proj.emi.loader;



import com.proj.emi.AppConsts;
import com.proj.emi.GlobalBeans;
import com.proj.emi.loader.base.BasePostLoader;
import com.proj.emi.model.ImagePostInBody;
import com.proj.emi.model.ImagePostOutBody;
import com.proj.emi.model.base.BaseResp;
import com.hcb.http.HttpProvider;
import com.hcb.http.PartAppender;
import com.hcb.util.BitmapUtil;
import com.hcb.util.LoggerUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class ImageUploader extends BasePostLoader<ImagePostOutBody, ImagePostInBody> {

    public interface UploadReactor {
        void onResult(String url);
    }

    private final static Logger LOG = LoggerFactory.getLogger(ImageUploader.class);

    @Override
    protected Logger logger() {
        return LOG;
    }

    private String user;
    private String uri;

    private final static String PART_NAME = "file";
    private final static String MEDIA_STREAM = "application/octet-stream";

    private final static long MAX_FILE_L = 256 * 1024;// 最大上传文件 256K
    private final static int MAX_IMG_S = 960;// 图片宽高最大值

    public void upload(final String path, final UploadReactor reactor) {
        if (null == path) {
            LOG.warn("error! NULL path in calling upload()");
            return;
        }
        user = "?merchant_uuid="+GlobalBeans.getSelf().getCurUser().getUid()+"&password="+
                GlobalBeans.getSelf().getCurUser().getPassword();
        uri = AppConsts.HOST_IMAGE + "media/file_upload" + user;
        final ImagePostOutBody body = new ImagePostOutBody()
                .setFileName(PART_NAME);
        httpProvider.post(uri, buildReqJson(body), new HttpProvider.RespStringListener() {
            @Override
            public void onResp(String str) {
                dataBack(reactor, parseObj(str));
            }
        }, new ImageAppender(path));
    }

    private void dataBack(UploadReactor reactor, BaseResp resp) {
        try {
            if (isRespNoError(resp)) {
                notifyResp(reactor, ((ImagePostInBody) resp.getBody()).getUrl());
            } else {
                notifyResp(reactor, null);
            }
        } catch (Exception e) {
        }
    }

    protected void notifyResp(final UploadReactor reactor, final String url) {
        if (null != reactor) {
            reactor.onResult(url);
        }
    }

    private class ImageAppender implements PartAppender {

        private String path;

        ImageAppender(final String path) {
            this.path = path;
        }

        @Override
        public void addMoreParts(MultipartBody.Builder builder) {
            LoggerUtil.d(LOG, "addMoreParts. add part: {}, filePath: {}", PART_NAME, path);
            final File f = new File(path);
            final MediaType mt = MediaType.parse(MEDIA_STREAM);
            RequestBody rb = null;
            if (f.length() > MAX_FILE_L) {
                LoggerUtil.d(LOG, "fileSize:{}, biggerThan:{}, need compress.", f.length(), MAX_FILE_L);
                try {
                    final ByteArrayOutputStream baos = BitmapUtil.compressImageToOS(path, MAX_FILE_L, MAX_IMG_S);
                    rb = RequestBody.create(mt, baos.toByteArray());
                } catch (Exception e) {
                    LOG.error("compress FAILED. use original picture");
                }
            } else {
                LoggerUtil.d(LOG, "fileSize:{}, lessThan:{}. NO-need compress", f.length(), MAX_FILE_L);
            }
            if (null == rb) {
                rb = RequestBody.create(mt, f);
            }
            builder.addFormDataPart(PART_NAME, f.getName(), rb);
        }
    }

}