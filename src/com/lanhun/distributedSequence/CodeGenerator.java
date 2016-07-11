package com.lanhun.distributedSequence;

public interface CodeGenerator {

	public String generate(String prefix);
	
	public String generate(String prefix,int len);
	
	public String generate(String prefix,String format,int len);
	
	public String generate(String prefix,String format);

}
