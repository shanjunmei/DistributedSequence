package com.lanhun.distributedSequence;

import java.util.Date;
import java.util.List;

import redis.clients.jedis.Jedis;
import redis.clients.util.Pool;

/**
 * 
 * @ClassName: CodeGeneratorImpl
 * @Description: 基于redis的编码生成实现
 * @author 李淼淼 445052471@qq.com
 * @date 2016年7月13日 上午11:14:43
 */
public class CodeGeneratorImpl implements CodeGenerator {

	private Pool<Jedis> jedisPool;

	public void setJedisPool(Pool<Jedis> jedisPool) {
		this.jedisPool = jedisPool;
	}

	/**
	 * (非 Javadoc)
	 * <p>
	 * Title: generate
	 * </p>
	 * <p>
	 * Description:自动追加yyyyMMdd
	 * </p>
	 * 
	 * @param prefix
	 * @return
	 * @see com.lanhun.distributedSequence.CodeGenerator#generate(java.lang.String)
	 */
	public String generate(String prefix) {
		return generate(prefix, "yyyyMMdd");
	}

	public static String format(Long i, int len) {
		return String.format("%0" + len + "d", i);

	}

	/**
	 * (非 Javadoc)
	 * <p>
	 * Title: generate
	 * </p>
	 * <p>
	 * Description:自动追加yyyyMMdd
	 * </p>
	 * 
	 * @param prefix
	 * @param len
	 * @return
	 * @see com.lanhun.distributedSequence.CodeGenerator#generate(java.lang.String,
	 *      int)
	 */
	public String generate(String prefix, int len) {
		return generate(prefix, "yyyyMMdd", len);
	}

	/**
	 * (非 Javadoc)
	 * <p>
	 * Title: generate
	 * </p>
	 * <p>
	 * Description: 可优化点，时间获取，重置方式
	 * </p>
	 * 
	 * @param prefix
	 * @param format
	 * @param len
	 * @return
	 * @see com.lanhun.distributedSequence.CodeGenerator#generate(java.lang.String,
	 *      java.lang.String, int)
	 */
	public String generate(String prefix, String format, int len) {
		String storeType = "seq:" + prefix;
		Jedis jedis = null;
		boolean hasClose = false;
		int ranLen = len;
		if (prefix != null) {
			ranLen = ranLen - prefix.length();
		}
		if (format != null) {
			ranLen = ranLen - format.length();
		}
		if (ranLen < 1) {
			throw new RuntimeException("unexpect len");
		}
		try {
			jedis = jedisPool.getResource();
			/*
			 * if (jedis.setnx(storeType, 0 + "") > 0) { jedis.expire(storeType,
			 * 3600 * 24); }
			 */
			Long i = jedis.incr(storeType);
			String random = format(i, ranLen);
			if (random.length() > ranLen) {
				jedis.del(storeType);
				return generate(prefix, format, len);
			}
			if (format == null || format.trim().length() == 0) {
				return String.format("%s%s", prefix, random);
			} else {
				List<String> time = jedis.time();
				jedis.close();
				hasClose = true;
				long t = Long.parseLong(time.get(0) + "000");
				long m = Long.parseLong(time.get(1));
				t = t + m / 1000;
				return String.format("%s%s%s", prefix, DateUtils.format(format, new Date(t)), random);
			}

		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			if (!hasClose) {
				if (jedis != null) {
					jedis.close();
				}
			}
		}
	}

	/**
	 * (非 Javadoc)
	 * <p>
	 * Title: generate
	 * </p>
	 * <p>
	 * Description:默认四位随机数
	 * </p>
	 * 
	 * @param prefix
	 * @param format
	 * @return
	 * @see com.lanhun.distributedSequence.CodeGenerator#generate(java.lang.String,
	 *      java.lang.String)
	 */
	public String generate(String prefix, String format) {
		int len = 4;
		if (prefix != null) {
			len = len + prefix.length();
		}
		if (format != null) {
			len = len + format.length();
		}
		return generate(prefix, format, len);
	}
}
