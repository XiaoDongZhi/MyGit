package luna;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;
import jdk.nashorn.internal.runtime.linker.Bootstrap;

public class NettyServer {

	private static final int BIZGROUPSIZE = Runtime.getRuntime()
			.availableProcessors() * 2;
	private static final int BIZTHREADSIZE = 100;
	private static final EventLoopGroup bossGroup = new NioEventLoopGroup(
			BIZGROUPSIZE);
	private static final EventLoopGroup workerGroup = new NioEventLoopGroup(
			BIZTHREADSIZE);

	// �����
	ServerBootstrap serverBootstrap;
	// �ͻ���
	Bootstrap clientBootstrap;

	// �����ͨ��
	Channel serverChannel;

	public void init() {
		serverBootstrap = new ServerBootstrap();
		serverBootstrap.group(bossGroup, workerGroup);
		serverBootstrap.channel(NioServerSocketChannel.class);
		// ���handler��������˵�IO����
		serverBootstrap.handler(new OutHandler());
		serverBootstrap.childHandler(new ChannelInitializer<Channel>() {

			@Override
			protected void initChannel(Channel arg0) throws Exception {
				// TODO Auto-generated method stub
				ChannelPipeline pipeline = arg0.pipeline();
				pipeline.addLast(new LengthFieldBasedFrameDecoder(
						Integer.MAX_VALUE, 0, 4, 0, 4));
				pipeline.addLast(new LengthFieldPrepender(4));
				pipeline.addLast(new StringDecoder(CharsetUtil.UTF_8));
				pipeline.addLast(new StringEncoder(CharsetUtil.UTF_8));
				// ���handler�����ͻ���Channel��״̬�仯
				pipeline.addLast(new TcpServerHandler());
			}

		});
		try {
			// ���������
			ChannelFuture cf = serverBootstrap.bind(getLocalHostIp(), 5656)
					.sync();
//			Toast.makeText(getActivity(), "TCP������������", Toast.LENGTH_SHORT)
//					.show();
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
		NettyServer nettyServer = new NettyServer();
		nettyServer.init();
	}
}