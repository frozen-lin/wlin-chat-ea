package com.wlin.chat.model;

import lombok.Data;

/**
 * <program> wlin-chat-ea </program>
 * <description>  </description>
 *
 * @author : wlin
 * @date : 2024-05-22 21:53
 **/
@Data
public class WebSocketReceiveMessage {
    private String messageType;
    private String messageData;
}
