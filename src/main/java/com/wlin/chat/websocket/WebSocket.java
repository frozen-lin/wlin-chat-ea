
package com.wlin.chat.websocket;

import com.wlin.chat.handler.WebSocketMessageHandler;
import com.wlin.chat.handler.WebSocketMessageHandlerService;
import com.wlin.chat.kit.ApplicationContextHolder;
import com.wlin.chat.kit.JSONKit;
import com.wlin.chat.model.WebSocketReceiveMessage;
import com.wlin.chat.vo.WebSocketChatMessageVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@Component()
@Slf4j
@ServerEndpoint("/websocket/{userId}")
public class WebSocket {
    
    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;
        /**
     * 用户ID
     */
    private String userId;
    
    //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
    //虽然@Component默认是单例模式的，但springboot还是会为每个websocket连接初始化一个bean，所以可以用一个静态set保存起来。
    //  注：底下WebSocket是当前类名
    public static CopyOnWriteArraySet<WebSocket> webSockets =new CopyOnWriteArraySet<>();
    // 用来存在线连接用户信息
    public static ConcurrentHashMap<String,Session> sessionPool = new ConcurrentHashMap<>();
    
    /**
     * 链接成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam(value="userId")String userId) {
        try {
			this.session = session;
			this.userId = userId;
			webSockets.add(this);
			sessionPool.put(userId, session);
			log.info("【websocket消息】有新的连接，总数为:"+webSockets.size());
            WebSocketMessageHandlerService webSocketMessageHandlerService = ApplicationContextHolder.applicationContext.getBean(WebSocketMessageHandlerService.class);
            webSocketMessageHandlerService.onOpen(userId);
		} catch (Exception e) {
            log.error("连接出错了,原因:", e);
        }
    }
    
    /**
     * 链接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        try {
			webSockets.remove(this);
            Session removeSession = sessionPool.remove(this.userId);
            if (removeSession != null) {
                removeSession.close();
            }
            log.info("【websocket消息】连接断开，总数为:"+webSockets.size());
        } catch (Exception e) {
            log.error("关闭连接出错了,原因:", e);
        }
    }
    /**
     * 收到客户端消息后调用的方法
     *
     * @param message
     */
    @OnMessage
    public void onMessage(String message) {
    	log.info("【websocket消息】收到客户端消息:"+message);
        WebSocketReceiveMessage webSocketReceiveMessage = JSONKit.jsonToBean(message, WebSocketReceiveMessage.class);
        webSocketReceiveMessage.setFromUserId(userId);
        WebSocketMessageHandlerService webSocketMessageHandlerService = ApplicationContextHolder.applicationContext.getBean(WebSocketMessageHandlerService.class);
        webSocketMessageHandlerService.onMessageHandle(webSocketReceiveMessage);

    }
    
	  /** 发送错误时的处理
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("用户错误,原因:", error);
    }

    
    // 此为广播消息
    public static void sendAllMessage(WebSocketChatMessageVO webSocketChatMessageVO) {
        String message = JSONKit.toJsonString(webSocketChatMessageVO);
        log.info("【websocket消息】广播消息:"+message);
        for(WebSocket webSocket : webSockets) {
            try {
            	if(webSocket.session.isOpen()) {
            		webSocket.session.getAsyncRemote().sendText(message);
            	}
            } catch (Exception e) {
                log.error("发送消息出错了,原因:", e);
            }
        }
    }
    
    // 此为单点消息
    public static void sendOneMessage(String userId, WebSocketChatMessageVO webSocketChatMessageVO) {
        String message = JSONKit.toJsonString(webSocketChatMessageVO);
        Session session = sessionPool.get(userId);
        if (session != null&&session.isOpen()) {
            try {
            	log.info("【websocket消息】 单点消息:"+message);
                session.getAsyncRemote().sendText(message);
            } catch (Exception e) {
                log.error("发送消息出错了,原因:", e);
            }
        }
    }
    
    // 此为单点消息(多人)
    public static void sendMoreMessage(String[] userIds, WebSocketChatMessageVO webSocketChatMessageVO) {
    	for(String userId:userIds) {
    		Session session = sessionPool.get(userId);
            if (session != null&&session.isOpen()) {
                try {
                    String message = JSONKit.toJsonString(webSocketChatMessageVO);
                	log.info("【websocket消息】 单点消息:"+message);

                    session.getAsyncRemote().sendText(message);
                } catch (Exception e) {
                    log.error("发送消息出错了,原因:", e);
                }
            }
    	}
        
    }
    
}