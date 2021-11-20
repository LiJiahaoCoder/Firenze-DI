package com.jiahao.restful.server;

import com.jiahao.restful.helper.Dispatcher;
import com.jiahao.restful.helper.HttpMethodFactory;
import com.jiahao.restful.helper.Serializer;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

public class HttpServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
  private final Dispatcher dispatcher;

  public HttpServerHandler() {
    this.dispatcher = new Dispatcher();
  }

  @Override
  protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws InvocationTargetException, InstantiationException, IllegalAccessException {
    String uri = request.uri();
    HttpMethod httpMethod = request.method();
    Object result = dispatcher.dispatch(uri, HttpMethodFactory.getMethod(httpMethod));

    Response responseData;
    HttpResponseStatus status;
    if (Objects.isNull(result)) {
      status = HttpResponseStatus.NOT_FOUND;
      responseData = new Response("Cannot found request uri: " + uri, status);
    } else {
      status = HttpResponseStatus.OK;
      responseData = new Response(result, status);
    }

    FullHttpResponse response = new DefaultFullHttpResponse(
            HttpVersion.HTTP_1_1,
            status,
            Unpooled.copiedBuffer(Serializer.serialize(responseData), CharsetUtil.UTF_8)
    );
    response.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/json");
    ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
  }
}
