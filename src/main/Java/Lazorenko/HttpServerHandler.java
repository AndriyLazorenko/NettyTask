package Lazorenko;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpRequest;
import org.apache.commons.validator.routines.UrlValidator;
import java.nio.charset.StandardCharsets;

import static io.netty.handler.codec.http.HttpHeaders.Names.*;
import static io.netty.handler.codec.http.HttpResponseStatus.*;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;


/**
 * ServerHandler class that processes the request and throws responses
 * @author andriylazorenko
 */

public class HttpServerHandler extends SimpleChannelInboundHandler<HttpRequest> {

    /**
     * <strong>Please keep in mind that this method will be renamed to
     * {@code messageReceived(ChannelHandlerContext, I)} in 5.0.</strong>
     * <p>
     * Is called for each message of type {@link I}.
     *
     * @param ctx the {@link ChannelHandlerContext} which this {@link SimpleChannelInboundHandler}
     *            belongs to
     * @param msg the message to handle
     * @throws Exception is thrown if an error occurred
     */


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpRequest msg) throws Exception {

        long startTransfer = System.nanoTime();

        if (HttpHeaders.is100ContinueExpected(msg)) {
            ctx.write(new DefaultFullHttpResponse(HTTP_1_1, CONTINUE));
        }

        boolean keepAlive = HttpHeaders.isKeepAlive(msg);

        FullHttpResponse response = createResponse(msg);

        if (!keepAlive) {
            ctx.write(response).addListener(ChannelFutureListener.CLOSE);
            } else {
                response.headers().set(CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
                ctx.writeAndFlush(response);
            }

        long endTransfer = System.nanoTime();
        long timeInterval = endTransfer-startTransfer;

        Status status = Status.getInstance();
        status.update(ctx,msg,response,timeInterval);
    }

    /**
     * Class responsible for performing the tasks given by Hamstercoders and, specifically,
     * creating responses with respect to server requests. A simple protocol in fact
     * @param req - <code>HttpRequest</code> object received by netty
     * @return response - <code>FullHttpResponse</code> object ready for deployment on server
     */

    private FullHttpResponse createResponse (HttpRequest req){

        //Variables block
        String uri = req.getUri();
        FullHttpResponse response;

        //Creation of scenarios depending on input
        int scenario = 0;
        if (uri.equals("/hello")) scenario = 1;
        else if (uri.contains("/redirect?url=")) scenario = 2;
        else if (uri.equals("/status")) scenario = 3;

        switch (scenario) {

            case 1:

                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                byte[] CONTENT = new byte[]{ 'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd' };
                response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(CONTENT));
                response.headers().set(CONTENT_TYPE, "text/plain");
                response.headers().set(CONTENT_LENGTH, response.content().readableBytes());
                break;

            case 2:

                //Statistics might be bugged because of auto-redirect in Chromium browser.
                //Input modification
                String modifiedUri = uri.replaceAll("%3C","<").replaceAll("%3E", ">");
                String url = modifiedUri.substring(modifiedUri.indexOf("<")+1,modifiedUri.lastIndexOf(">"));

                //Url validation block
                String[] schemes = {"http","https"}; // DEFAULT schemes = "http", "https", "ftp"
                UrlValidator urlValidator = new UrlValidator(schemes);
                if (urlValidator.isValid(url)) {

                   //Redirection block
                    response = new DefaultFullHttpResponse(HTTP_1_1, TEMPORARY_REDIRECT);
                    response.headers().set(LOCATION, url);
                    response.headers().set(CONTENT_LENGTH, response.content().readableBytes());
                } else {

                    //Incorrect url handling block
                    System.out.println("url is invalid");
                    response = new DefaultFullHttpResponse(HTTP_1_1, CONTINUE);
                }
                break;

            case 3:

                String responseString = Status.getInstance().toString();
                response = new DefaultFullHttpResponse(HTTP_1_1, OK,
                        Unpooled.copiedBuffer(responseString, StandardCharsets.UTF_8));
                response.headers().set(CONTENT_TYPE, "text/plain");
                response.headers().set(CONTENT_LENGTH, response.content().readableBytes());
                break;

            default:
                response = new DefaultFullHttpResponse(HTTP_1_1, CONTINUE);
        }

        return response;
    }
}
