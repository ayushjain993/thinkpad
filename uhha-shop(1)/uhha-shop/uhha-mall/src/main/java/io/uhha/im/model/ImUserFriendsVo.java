package io.uhha.im.model;

import lombok.Data;

@Data
public class ImUserFriendsVo {

    //用户id
    private String id;
    //用户friendId
    private String img;
    private String name;
    private String lastMessage;
    private String dept;
    private Long readNum;
    private Integer type;
    private boolean online;

}
