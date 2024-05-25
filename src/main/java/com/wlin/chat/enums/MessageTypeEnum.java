package com.wlin.chat.enums;

/**
 * <program> wlin-chat-ea </program>
 * <description>  </description>
 *
 * @author : wlin
 * @date : 2024-05-22 21:49
 **/
public enum MessageTypeEnum {
    /**
     *
     */
    System("系统消息"),
    User("用户消息"),
    ;

    MessageTypeEnum(String desc) {
        this.desc = desc;
    }
    public final String desc;
}
