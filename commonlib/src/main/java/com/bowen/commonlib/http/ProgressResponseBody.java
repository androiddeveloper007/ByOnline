package com.bowen.commonlib.http;

import android.os.RecoverySystem;

import com.bowen.commonlib.R;
import com.bowen.commonlib.event.FileLoadEvent;
import com.bowen.commonlib.model.RxBus;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * Created by AwenZeng on 2017/4/27.
 */

public class ProgressResponseBody extends ResponseBody {

    private ResponseBody responseBody;
    private BufferedSource bufferedSource;

    public ProgressResponseBody(ResponseBody responseBody) {
        this.responseBody = responseBody;
    }

    @Override public MediaType contentType() {
        return responseBody.contentType();
    }

    @Override public long contentLength() {
        return responseBody.contentLength();
    }

    @Override public BufferedSource source() {
        if (bufferedSource == null) {
            bufferedSource = Okio.buffer(source(responseBody.source()));
        }
        return bufferedSource;
    }

    private Source source(Source source) {
        return new ForwardingSource(source) {
            long progress = 0L;

            @Override public long read(Buffer sink, long byteCount) throws IOException {
                long bytesRead = super.read(sink, byteCount);
                progress += bytesRead != -1 ? bytesRead : 0;
                FileLoadEvent fileLoadEvent = new FileLoadEvent(responseBody.contentLength(),progress);
                RxBus.getInstance().post(fileLoadEvent);
                return bytesRead;
            }
        };
    }
}
