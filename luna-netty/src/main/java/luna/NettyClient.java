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
	
	// 服务端通道
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
                     //添加一个Hanlder用来处理各种Channel状态  
                    pipeline.addLast("handlerIn", new ClientHandler());  
                     //添加一个Handler用来接收监听IO操作的  
                    pipeline.addLast("handlerOut", new OutHandler());  
             }  
         });  
        ChannelFuture f;  
        try {  
            //连接服务端  
        f = b.connect(getLocalHostIp(), 5656).sync();  
        serverChannel = f.channel();  
        serverChannel.writeAndFlush("<<<<<<<<<<<<<<<<客户端请求连接>>>>>>>>>>>>>>>>");  
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