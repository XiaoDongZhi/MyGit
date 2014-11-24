package com.zhidong.service;

import org.apache.thrift.TException;

public class ThriftServiceImpl implements ThriftService.Iface {

	@Override
	public int add(int a, int b) throws TException {
		return a + b;
	}

}
