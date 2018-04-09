package com.ttn.bluebell.durable.model.common;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Fault {

    @JsonProperty
    String tid;

    @JsonProperty
    List<Message> messages;

    public Fault(String message, String code) {
        messages = new ArrayList<>(1);
        messages.add(new Message(message,code,Collections.emptyMap()));
    }

    public Fault(String tid,String message, String code) {
        this.tid = tid;
        messages = new ArrayList<>(1);
        messages.add(new Message(message,code,Collections.emptyMap()));
    }
    public Fault(String message, String code,Map<String,Object> argsMaps) {
        messages = new ArrayList<>(1);
        messages.add(new Message(message,code,argsMaps));
    }

    public Fault(String tid,String message, String code,Map<String,Object> argsMaps) {
        this.tid = tid;
        messages = new ArrayList<>(1);
        messages.add(new Message(message,code,argsMaps));
    }

    public List<Message> getMessages() {
        return messages;
    }

    public String getTid() {
        return tid;
    }

    public static class Message {
        private String message;
        private String code;
        private Map<String,Object> argsMap = Collections.emptyMap();

        public Message(
                @JsonProperty("message") String message,
                @JsonProperty("code") String code,
                @JsonProperty("argsMap") Map<String,Object> argsMap
        ) {
            this.message = message;
            this.code = code;
            this.argsMap = argsMap;
        }

        public String getMessage() {
            return message;
        }

        public String getCode() {
            return code;
        }

        public Map<String, Object> getArgsMap() {
            return argsMap;
        }
    }
}
