package com.minhuizhu.http.action;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.internal.Util;
import okio.BufferedSink;
import okio.ByteString;
import okio.Okio;
import okio.Source;

/**
 * Created by rex.wei on 2016/10/4.
 */
public abstract class RequestWrapper extends RequestBody {
    private String body;

    public RequestWrapper(String body) {
        this.body = body;
    }

    public String getBody() {
        return body;
    }

    public static RequestWrapper wrapper(final MediaType contentType, final String bodyContent) {
        final ByteString content = ByteString.encodeUtf8(bodyContent);
        return new RequestWrapper(bodyContent) {
            @Override
            public MediaType contentType() {
                return contentType;
            }

            @Override
            public long contentLength() throws IOException {
                return content.size();
            }

            @Override
            public void writeTo(BufferedSink sink) throws IOException {
                sink.write(content);
            }
        };
    }

    public static RequestWrapper wrapper(final MediaType contentType, final File file) {
        if (file == null) throw new NullPointerException("request file == null");

        return new RequestWrapper(file.getAbsolutePath()) {
            @Override
            public MediaType contentType() {
                return contentType;
            }

            @Override
            public long contentLength() {
                return file.length();
            }

            @Override
            public void writeTo(BufferedSink sink) throws IOException {
                Source source = null;
                try {
                    source = Okio.source(file);
                    sink.writeAll(source);
                } finally {
                    Util.closeQuietly(source);
                }
            }
        };
    }
}
