package com.wlin.chat.handler.impl;

import com.wlin.chat.enums.ReceiveMessageTypeConst;
import com.wlin.chat.handler.WebSocketMessageHandler;
import com.wlin.chat.model.WebSocketReceiveMessage;
import org.springframework.stereotype.Component;

/**
 * <program> wlin-chat-ea </program>
 * <description>  </description>
 *
 * @author : wlin
 * @date : 2024-05-22 22:03
 **/
@Component
public class ChatForTwoMessageHandler implements WebSocketMessageHandler {
    @Override
    public boolean supports(WebSocketReceiveMessage webSocketReceiveMessage) {
        return ReceiveMessageTypeConst.CHAT_FOR_TWO.equals(webSocketReceiveMessage.getMessageType());
    }

    @Override
    public void handleMessage() {

    }
}
