package com.wlin.chat.handler;

import com.wlin.chat.enums.MessageTypeEnum;
import com.wlin.chat.model.WebSocketReceiveMessage;
import com.wlin.chat.vo.WebSocketChatMessageVO;
import com.wlin.chat.websocket.WebSocket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Enumeration;
import java.util.List;


@Component
public class WebSocketMessageHandlerService {


    @Autowired
    List<WebSocketMessageHandler> webSocketMessageHandlerList;

    public void onMessageHandle(WebSocketReceiveMessage webSocketReceiveMessage) {
        for (WebSocketMessageHandler webSocketMessageHandler : webSocketMessageHandlerList) {
            if (webSocketMessageHandler.supports(webSocketReceiveMessage)) {
                webSocketMessageHandler.handleMessage(webSocketReceiveMessage);
            }
        }
    }

    public void onOpen(String loginUserId) {
        WebSocketChatMessageVO webSocketChatMessageVO = new WebSocketChatMessageVO();
        webSocketChatMessageVO.setMessageType(MessageTypeEnum.System);
        webSocketChatMessageVO.setMessage("[" + loginUserId + "]" + "加入聊天室");
        Enumeration<String> userIds = WebSocket.sessionPool.keys();
        while (userIds.hasMoreElements()) {
            String userId = userIds.nextElement();
            if(userId.equals(loginUserId)){
                continue;
            }
            WebSocket.sendOneMessage(userId, webSocketChatMessageVO);
        }
    }
}
