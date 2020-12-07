package com.gw.print.netty;

import com.gw.print.component.SingletonComponent;
import com.gw.print.constants.ServerConstants;
import com.gw.print.controller.PrintController;
import com.gw.print.model.HttpRequest;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.util.Optional;

/**
 * 处理Http请求
 */
@ChannelHandler.Sharable
public class HttpHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {

        DefaultFullHttpResponse response = executeRequest(ctx, msg);
        String origin = Optional.ofNullable(msg.headers().get(HttpHeaderNames.ORIGIN))
                .filter(str -> !StringUtils.equals(str, "null")).orElse("");
        HttpHeaders heads = response.headers();
        // 返回内容的MIME类型
        heads.add(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON + "; charset=UTF-8");
        // 响应体的长度
        heads.add(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
        // 允许跨域访问
        heads.add(HttpHeaderNames.ACCESS_CONTROL_ALLOW_ORIGIN, origin);
        heads.add(HttpHeaderNames.ACCESS_CONTROL_REQUEST_METHOD, "GET, POST, PUT,DELETE");
        heads.add(HttpHeaderNames.ACCESS_CONTROL_ALLOW_HEADERS, "*");
        heads.add(HttpHeaderNames.ACCESS_CONTROL_ALLOW_CREDENTIALS, true);
        heads.add(HttpHeaderNames.ACCESS_CONTROL_MAX_AGE,6);
        // 响应给客户端
        ctx.write(response);
    }

    /**
     * 数据发送完毕，则关闭连接通道.
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);
        ctx.flush();
    }

    /**
     * 发生异常
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        if (null != cause) {
            cause.printStackTrace();
        }
        if (null != ctx) {
            ctx.close();
        }
    }

    /**
     * 处理请求，返回应答
     *
     * @param msg 请求信息
     * @return 应答信息
     * @throws UnsupportedEncodingException
     */
    private DefaultFullHttpResponse executeRequest(ChannelHandlerContext ctx, FullHttpRequest msg) throws UnsupportedEncodingException {
        HttpResponseStatus responseStatus = HttpResponseStatus.OK;
        String returnMsg = "";
        try {
            if(!msg.method().equals(HttpMethod.OPTIONS)){
                if(!ServerConstants.REMOTE_MODE&&!isLocal(msg)){
                    returnMsg = "{\"result\":\"unavailable\"}";
                }else if(ServerConstants.PRINT_SWITCH){
                    HttpRequest httpRequest = new HttpRequest(msg);
                    Class<PrintController> printControllerClass = PrintController.class;
                    Method invokeMethod = printControllerClass.getMethod(httpRequest.getMethod(), HttpRequest.class);
                    returnMsg = (String) invokeMethod.invoke(SingletonComponent.printController(), httpRequest);
                }
            }
        } catch (NoSuchMethodException e) {
            responseStatus = HttpResponseStatus.NOT_FOUND;
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            responseStatus = HttpResponseStatus.INTERNAL_SERVER_ERROR;
        }
        return new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, responseStatus,
                Unpooled.wrappedBuffer(returnMsg==null?"".getBytes():returnMsg.getBytes()));
    }

//    private boolean isLocal(ChannelHandlerContext ctx, FullHttpRequest httpRequest) {
//        String ip = "";
//        try {
//            String ipForwarded = httpRequest.headers().get("x-forwarded-for");
//
//            if (StringUtils.isBlank(ipForwarded) || "unknown".equalsIgnoreCase(ipForwarded)) {
//                InetSocketAddress insocket = (InetSocketAddress) ctx.channel().remoteAddress();
//                ip = insocket.getAddress().getHostAddress();
//            } else {
//                ip = ipForwarded;
//            }
//        } catch (Exception e) {
//        }
//        if ("0:0:0:0:0:0:0:1".equals(ip)) {
//            ip = "127.0.0.1";
//        }
//        return "127.0.0.1".equals(ip);
//    }

    private boolean isLocal(FullHttpRequest httpRequest) {
        String host = httpRequest.headers().get("host");
        return host.contains("127.0.0.1");
    }

}
