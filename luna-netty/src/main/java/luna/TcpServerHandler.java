package luna;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class TcpServerHandler extends ChannelInboundHandlerAdapter {  
	// �ͻ���ͨ��
	Channel clientChannel;
    @Override  
    public void channelRead(ChannelHandlerContext ctx, Object msg)  
            throws Exception {  
        System.out.println("<<<<<<<<<<<<<<<<�յ��ͻ�����Ϣ :"+ msg);  
        ctx.channel().writeAndFlush("<<<<<<<<<<������Ѿ����գ�" + msg);  
    }  
    @Override  
    public void channelActive(ChannelHandlerContext ctx) throws Exception {  
        // TODO Auto-generated method stub  
        System.out.println("ͨ���Ѿ�����>>>>>>>>");  
        clientChannel = ctx.channel();  
    }  
      @Override  
   public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {  
        System.out.println("exception is general");  
    }  
}  