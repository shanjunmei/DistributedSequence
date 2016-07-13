package com.lanhun.distributedSequence;
/**
 * 
* @ClassName: CodeGenerator 
* @Description: 编码生成，容量算法，长度减去固定前缀，默认都追加yyyyMMdd 
* @author 李淼淼  445052471@qq.com
* @date 2016年7月13日 下午12:33:22
 */
public interface CodeGenerator {

	public String generate(String prefix);
	
	public String generate(String prefix,int len);
	
	public String generate(String prefix,String format,int len);
	
	public String generate(String prefix,String format);

}
