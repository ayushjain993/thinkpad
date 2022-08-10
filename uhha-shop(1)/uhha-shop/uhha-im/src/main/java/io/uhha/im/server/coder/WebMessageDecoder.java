package io.uhha.im.server.coder;

import io.uhha.common.im.constant.IMConstant;
import io.uhha.common.im.constant.ChannelAttr;
import io.uhha.im.server.model.Pong;
import io.uhha.im.server.model.SentBody;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.uhha.im.server.model.proto.SentBodyProto;

import java.io.InputStream;
import java.util.List;

public class WebMessageDecoder extends MessageToMessageDecoder<BinaryWebSocketFrame> {

    @Override
    protected void decode(ChannelHandlerContext context, BinaryWebSocketFrame frame, List<Object> list) throws Exception {

        context.channel().attr(ChannelAttr.PING_COUNT).set(null);

        ByteBuf buffer = frame.content();

        byte type = buffer.readByte();

        if (IMConstant.DATA_TYPE_PONG == type) {
            list.add(Pong.getInstance());
            return;
        }

        InputStream inputStream = new ByteBufInputStream(buffer);

        SentBodyProto.Model bodyProto = SentBodyProto.Model.parseFrom(inputStream);

        SentBody body = new SentBody();
        body.setKey(bodyProto.getKey());
        body.setTimestamp(bodyProto.getTimestamp());
        body.putAll(bodyProto.getDataMap());

        list.add(body);
    }
}
