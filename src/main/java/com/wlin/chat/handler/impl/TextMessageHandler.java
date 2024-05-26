package com.wlin.chat.handler.impl;

import com.wlin.chat.enums.MessageTypeEnum;
import com.wlin.chat.enums.ReceiveMessageTypeConst;
import com.wlin.chat.handler.WebSocketMessageHandler;
import com.wlin.chat.model.WebSocketReceiveMessage;
import com.wlin.chat.vo.WebSocketChatMessageVO;
import com.wlin.chat.websocket.WebSocket;
import org.springframework.stereotype.Component;

import java.util.Enumeration;

/**
 * <program> wlin-chat-ea </program>
 * <description>  </description>
 *
 * @author : wlin
 * @date : 2024-05-22 22:03
 **/
@Component
public class TextMessageHandler implements WebSocketMessageHandler {
    @Override
    public boolean supports(WebSocketReceiveMessage webSocketReceiveMessage) {
        return ReceiveMessageTypeConst.Text.equals(webSocketReceiveMessage.getMessageType());
    }

    @Override
    public void handleMessage(WebSocketReceiveMessage webSocketReceiveMessage){
        WebSocketChatMessageVO webSocketChatMessageVO = new WebSocketChatMessageVO();
        webSocketChatMessageVO.setMessageType(MessageTypeEnum.User);
        webSocketChatMessageVO.setFromUserId(webSocketReceiveMessage.getFromUserId());
        webSocketChatMessageVO.setMessage(webSocketReceiveMessage.getMessageData());
        Enumeration<String> userIds = WebSocket.sessionPool.keys();
        while (userIds.hasMoreElements()) {
            String userId = userIds.nextElement();
            if(userId.equals(webSocketReceiveMessage.getFromUserId())){
                continue;
            }
            WebSocket.sendOneMessage(userId, webSocketChatMessageVO);
        }

    }
}
