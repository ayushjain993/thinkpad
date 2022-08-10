/*******************************************************************************
 * Copyright (c) 2005, 2017 springside.github.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *******************************************************************************/
package io.uhha.coin.common.api;

import java.security.SecureRandom;
import java.util.Base64;

/**
 * 封装各种生成唯一性ID算法的工具类.
 */
public class KeyGenerator {

	private static SecureRandom random = new SecureRandom();


	/**
	 * 随机生成AccessKeyId
	 */
	public static String genAccessKeyId() {
		return randomByteToString(15);
	}

	/**
	 * 随机生成accessKeySecret
	 */
	public static String genAccessKeySecret() {
		return randomByteToString(30);
	}

	private static String randomByteToString(int length){
		byte[] randomBytes = new byte[length];
		random.nextBytes(randomBytes);
		return Base64.getEncoder().encodeToString(randomBytes);
	}

	public static void main(String args[]){
		System.out.println(genAccessKeyId());
		System.out.println(genAccessKeySecret());
	}
}
