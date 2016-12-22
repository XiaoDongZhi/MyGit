package luna;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;


public class NettyClient{  
	
	// �����ͨ��
	Channel serverChannel;
	
    EventLoopGroup group = new NioEventLoopGroup();  
    public void init(){  
        Bootstrap b = new Bootstrap();  
        b.group(group);  
        b.channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true);  
        b.handler(new ChannelInitializer<SocketChannel>() {  
             @Override  
             protected void initChannel(SocketChannel ch) throws Exception {  
                 ChannelPipeline pipeline = ch.pipeline();  
                    pipeline.addLast("frameDecoder", new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));  
                    pipeline.addLast("frameEncoder", new LengthFieldPrepender(4));  
                    pipeline.addLast("decoder", new StringDecoder(CharsetUtil.UTF_8));  
                    pipeline.addLast("encoder", new StringEncoder(CharsetUtil.UTF_8));  
                     //���һ��Hanlder�����������Channel״̬  
                    pipeline.addLast("handlerIn", new ClientHandler());  
                     //���һ��Handler�������ռ���IO������  
                    pipeline.addLast("handlerOut", new OutHandler());  
             }  
         });  
        ChannelFuture f;  
        try {  
            //���ӷ����  
        f = b.connect(getLocalHostIp(), 5656).sync();  
        serverChannel = f.channel();  
        serverChannel.writeAndFlush("<<<<<<<<<<<<<<<<�ͻ�����������>>>>>>>>>>>>>>>>");  
        } catch (InterruptedException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }  
    }  
    private String getLocalHostIp()
	{
		return "127.0.0.1";
	}
    
    public static void main(String[] args) {
		NettyClient nettyClient = new NettyClient();
		nettyClient.init();
	}
}  