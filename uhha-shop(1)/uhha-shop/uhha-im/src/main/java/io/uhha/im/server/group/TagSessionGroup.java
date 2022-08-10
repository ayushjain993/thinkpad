package io.uhha.im.server.group;


import io.netty.channel.Channel;
import io.uhha.common.im.constant.ChannelAttr;

public class TagSessionGroup extends SessionGroup {

    @Override
    protected String getKey(Channel channel){
        return channel.attr(ChannelAttr.TAG).get();
    }
}
