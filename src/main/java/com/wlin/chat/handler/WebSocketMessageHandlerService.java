package com.wlin.chat.handler;

import com.fasterxml.jackson.databind.ser.std.StdKeySerializers;
import com.wlin.chat.enums.MessageTypeEnum;
import com.wlin.chat.model.WebSocketReceiveMessage;
import com.wlin.chat.task.DelayQueueManager;
import com.wlin.chat.vo.WebSocketChatMessageVO;
import com.wlin.chat.websocket.WebSocket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.stream.Collectors;


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
        List<String> userIdList = new ArrayList<>();
        while (userIds.hasMoreElements()) {
            String userId = userIds.nextElement();
            if(userId.equals(loginUserId)){
                continue;
            }
            userIdList.add(userId);
            WebSocket.sendOneMessage(userId, webSocketChatMessageVO);
        }
        if(!userIdList.isEmpty()){
            WebSocketChatMessageVO loginMessage = new WebSocketChatMessageVO();
            loginMessage.setMessageType(MessageTypeEnum.System);
            loginMessage.setMessage("[" + String.join("、", userIdList) + "]" + "已在聊天室中");
            WebSocket.sendOneMessage(loginUserId, loginMessage);
        }
    }
}
