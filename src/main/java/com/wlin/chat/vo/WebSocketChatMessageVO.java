package com.wlin.chat.vo;

import com.wlin.chat.enums.MessageTypeEnum;
import lombok.Data;

/**
 * <program> wlin-chat-ea </program>
 * <description>  </description>
 *
 * @author : wlin
 * @date : 2024-05-22 21:47
 **/
@Data
public class WebSocketChatMessageVO {
    private MessageTypeEnum messageType;
    private String message;
}
