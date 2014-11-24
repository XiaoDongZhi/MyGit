namespace java com.zhidong.service  // defines the namespace  
   
typedef i32 int  //typedefs to get convenient names for your types

service ThriftService {  
	int add(1:int a,2:int b),
}