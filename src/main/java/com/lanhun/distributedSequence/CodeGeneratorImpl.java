package com.lanhun.distributedSequence;

import java.util.Date;
import java.util.List;

import redis.clients.jedis.Jedis;
import redis.clients.util.Pool;

public class CodeGeneratorImpl implements CodeGenerator {

	private Pool<Jedis> jedisPool;

	public void setJedisPool(Pool<Jedis> jedisPool) {
		this.jedisPool = jedisPool;
	}

	public String generate(String prefix) {
		return generate(prefix, "yyyyMMdd");
	}

	public static String format(Long i, int len) {
		return String.format("%0" + len + "d", i);

	}

	public String generate(String prefix, int len) {
		return generate(prefix, "yyyyMMdd", len);
	}

	public String generate(String prefix, String format, int len) {
		String storeType = "seq:" + prefix;
		Jedis jedis = jedisPool.getResource();
		if (jedis.setnx(storeType, 0 + "") > 0) {
			jedis.expire(storeType, 3600 * 24);
		}
		Long i = jedis.incr(storeType);
		jedis.close();
		List<String> time = jedis.time();
		long t = Long.parseLong(time.get(0) + "000");
		long m = Long.parseLong(time.get(1));
		t = t + m / 1000;
		return String.format("%s%s%0" + len + "d", prefix,
				DateUtils.format(format, new Date(t)), i);
	}

	public String generate(String prefix, String format) {
		return generate(prefix, format);
	}
}
