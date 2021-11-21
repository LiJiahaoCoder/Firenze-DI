package com.jiahao.restful.server;

import com.jiahao.restful.helper.Dispatcher;
import com.jiahao.restful.util.Utils;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;

public class HttpServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
  private final Dispatcher dispatcher;

  public HttpServerHandler() {
    this.dispatcher = new Dispatcher();
  }

  @Override
  protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws InvocationTargetException, IllegalAccessException {
    String uri = request.uri();
    String dataString = request.content().toString(StandardCharsets.UTF_8);
    HttpMethod httpMethod = request.method();

    Response responseData = null;
    HttpResponseStatus status = HttpResponseStatus.OK;

    try {
      Object result = dispatcher.dispatch(uri, HttpMethodFactory.getMethod(httpMethod), dataString);
      responseData = new Response(result, status);
    } catch (RuntimeException exception) {
      status = HttpResponseStatus.NOT_FOUND;
      responseData = new Response("Cannot found request uri: " + uri, status);
    }

    FullHttpResponse response = new DefaultFullHttpResponse(
            HttpVersion.HTTP_1_1,
            status,
            Unpooled.copiedBuffer(Utils.serialize(responseData), CharsetUtil.UTF_8)
    );
    response.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/json");
    ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
  }
}
