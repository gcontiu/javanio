package poc.netty.server.discarder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.ReferenceCountUtil;

/**
 * Created by anghelc on 24/03/16.
            */
    public class DiscardServerHandler extends ChannelHandlerAdapter {

//        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) { // (2)
            ctx.write(msg);
            ctx.flush();

            // -----------------------------------
//            ByteBuf inputBuffer = (ByteBuf) msg;
//            try {
//                while(inputBuffer.isReadable()) {
//                    System.out.println((char) inputBuffer.readableBytes());
//                    System.out.flush();
//                }
//            } finally {
//                ReferenceCountUtil.release(msg);
//            }

            // -----------------------------------

//            // Discard the received data silently.
//                    ((ByteBuf) msg).release(); // (3)
        }



        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { // (4)
            // Close the connection when an exception is raised.
            cause.printStackTrace();
            ctx.close();
        }

}
