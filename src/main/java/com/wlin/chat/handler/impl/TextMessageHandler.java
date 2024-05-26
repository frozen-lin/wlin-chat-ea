package com.wlin.chat.handler.impl;

import com.wlin.chat.enums.MessageTypeEnum;
import com.wlin.chat.enums.ReceiveMessageTypeConst;
import com.wlin.chat.handler.WebSocketMessageHandler;
import com.wlin.chat.model.WebSocketReceiveMessage;
import com.wlin.chat.task.DelayQueueManager;
import com.wlin.chat.task.DelayTask;
import com.wlin.chat.task.TaskBase;
import com.wlin.chat.vo.WebSocketChatMessageVO;
import com.wlin.chat.websocket.WebSocket;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    DelayQueueManager delayQueueManager;
    @Override
    public boolean supports(WebSocketReceiveMessage webSocketReceiveMessage) {
        return ReceiveMessageTypeConst.Text.equals(webSocketReceiveMessage.getMessageType());
    }

    @Override
    public void handleMessage(WebSocketReceiveMessage webSocketReceiveMessage){
        String fromUserId = webSocketReceiveMessage.getFromUserId();
        WebSocketChatMessageVO webSocketChatMessageVO = new WebSocketChatMessageVO();
        webSocketChatMessageVO.setMessageType(MessageTypeEnum.User);
        webSocketChatMessageVO.setFromUserId(fromUserId);
        webSocketChatMessageVO.setMessage(webSocketReceiveMessage.getMessageData());
        Enumeration<String> userIds = WebSocket.sessionPool.keys();
        while (userIds.hasMoreElements()) {
            String userId = userIds.nextElement();
            if(userId.equals(fromUserId)){
                continue;
            }
            WebSocket.sendOneMessage(userId, webSocketChatMessageVO);
        }
        //有人回复了 清空之前的任务，重新处理任务
        delayQueueManager.clear();
        delayQueueManager.put(new DelayTask(new TaskBase("message", fromUserId, (delayTask) -> {
            WebSocketChatMessageVO noUserReplyMessage = new WebSocketChatMessageVO();
            noUserReplyMessage.setMessageType(MessageTypeEnum.AI);
            noUserReplyMessage.setMessage("暂时无人回复哦~");
            WebSocket.sendOneMessage(fromUserId, noUserReplyMessage);
        }), 1 * 60 * 1000));
    }
}
