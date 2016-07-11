package com.lanhun.distributedSequence;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.util.Pool;

public class CodeGeneratorTest {
	public static void main(String[] args) {
		String host = "127.0.0.1";
		int port = 6379;

		List<String> codes = new ArrayList<String>();
		Pool<Jedis> jedisPool = createJedisPool(host, port);
		
		CodeGenerator generator = new CodeGeneratorImpl();
		((CodeGeneratorImpl) generator).setJedisPool(jedisPool);
		long t = System.currentTimeMillis();
		String code = null;
		for (int i = 0; i < 1; i++) {
			code = generator.generate("O2O");
			codes.add(code);
			System.out.println(code);
		}
		t = System.currentTimeMillis() - t;
		System.out.println("take" + t + " ms");
		System.out.println("total " + codes.size() + " codes");
	}

	private static JedisPool createJedisPool(String host, int port) {
		JedisPool jedisPool = new JedisPool(host, port);
		return jedisPool;
	}
	
	private static JedisSentinelPool createSenJedisPool(String master, Set<String> sentinels) {
		JedisSentinelPool jedisPool = new JedisSentinelPool("mymaster", sentinels);
		return jedisPool;
	}

}
