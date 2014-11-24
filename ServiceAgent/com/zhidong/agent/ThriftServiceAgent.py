from thrift.protocol import TBinaryProtocol  
from thrift.transport import TSocket  
from com.zhidong.service import ThriftService


transport = TSocket.TSocket("localhost", 9966)  
transport.open()  
protocol = TBinaryProtocol.TBinaryProtocol(transport)  
  
# Use the service we already defined  
client = ThriftService.Client(protocol) 
print client.add(1, 2)