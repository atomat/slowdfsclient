package com.hcb168.slowdfs.util;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

public class MyUtil {
	private static final ObjectMapper objectMapper = new ObjectMapper();
	private static final SecureRandom secureRandom = new SecureRandom();

	/**
	 * 根据JSON转换成对象
	 * 
	 * @param jsonStr
	 * @param tClass
	 * @return
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static <T> T getObjectByJson(String jsonStr, Class<T> tClass)
			throws JsonParseException, JsonMappingException, IOException {
		T obj = objectMapper.readValue(jsonStr, tClass);
		return obj;
	}

	/**
	 * 转换json串为Map对象
	 * 
	 * @param jsonStr
	 * @return
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 */
	@SuppressWarnings("unchecked")
	public static <K, V> Map<K, V> getMapByJsonStr(String jsonStr)
			throws JsonParseException, JsonMappingException, IOException {
		Map<K, V> map = new HashMap<K, V>();
		map = getObjectByJson(jsonStr, map.getClass());
		return map;
	}

	/**
	 * 随机化一个数组
	 * 
	 * @param array
	 *            数组类型不能是基本类型，必须是类和基本类型的包装类
	 * @return
	 */
	public static <T> T[] randomizeArray(T[] array) {
		int len = array.length;
		for (int i = 0; i < len; i++) {
			int iR = secureRandom.nextInt(len);
			T tmp = array[i];
			array[i] = array[iR];
			array[iR] = tmp;
		}
		return array;
	}
}
