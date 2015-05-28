package com.bjhit.video.common.logging;

import java.util.LinkedList;
import java.util.List;

import com.bjhit.video.common.logging.Logger.Message;

/**
 * 
 * @description
 * @project bjhit-video
 * @author guanxianchun
 * @Create 2015-2-11 下午3:55:33
 * @version 1.0
 */
public class BufferLogSink implements Logger.LogSink {

    private List<Message> messages = new LinkedList<Message>();

    @Override
    public void postMessage(Message msg) {
        messages.add(msg);
    }

    public List<Message> getMessages() {
        return messages;
    }
}
