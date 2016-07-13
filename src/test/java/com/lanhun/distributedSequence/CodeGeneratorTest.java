package com.lanhun.distributedSequence;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.util.StopWatch;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.util.Pool;

public class CodeGeneratorTest {
	public static void main(String[] args) {
		String host = "192.168.1.195";
		int port = 26379;
		String master="mymaster";
		String password="ffzx6102";
		
		host="127.0.0.1";
		port=6379;
		//
		
		Set<String> sentinels=new HashSet<String>();
		sentinels.add(host+":"+port);
		
		Pool<Jedis> jedisPool =createJedisPool(host, port);//createSenJedisPool(master, sentinels,password);//createJedisPool(host, port);
		
		
		
		
		CodeGenerator generator = new CodeGeneratorImpl();
		((CodeGeneratorImpl) generator).setJedisPool(jedisPool);
		
		for (int i = 0; i < 10; i++) {
			test(generator);
		}
	}

	private static void test(CodeGenerator generator) {
		List<String> codes = new ArrayList<String>();
		Set<String> codesDistinck=new HashSet<String>();
		long t = System.currentTimeMillis();
		String code = null;
		StopWatch watch=new StopWatch();
		for (int i = 0; i < 1; i++) {
			watch.start("code generate");
			code = generator.generate("O2O");
			watch.stop();
			System.out.println(watch.prettyPrint());
			codes.add(code);
			codesDistinck.add(code);
			//System.out.println(code);
		}
		t = System.currentTimeMillis() - t;
		//System.out.println("take" + t + " ms");
		//System.out.println("total " + codes.size() + " codes");
		//System.out.println("uniq total " + codesDistinck.size() + " codes");
	}

	private static JedisPool createJedisPool(String host, int port) {
		JedisPool jedisPool = new JedisPool(host, port);
		return jedisPool;
	}
	
	private static JedisSentinelPool createSenJedisPool(String master, Set<String> sentinels,String password) {
		JedisSentinelPool jedisPool = new JedisSentinelPool(master, sentinels,password);
		return jedisPool;
	}

}
