package com.admin.util;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * @since 2017-08-18
 * @author tyro
 *
 */
public class Tools {

	/**
	 * 返回随机数
	 * 
	 * @param n
	 *            个数
	 * @return
	 */
	public static String random(int n) {
		if (n < 1 || n > 10) {
			throw new IllegalArgumentException("cannot random " + n
					+ " bit number");
		}
		Random ran = new Random();
		if (n == 1) {
			return String.valueOf(ran.nextInt(10));
		}
		int bitField = 0;
		char[] chs = new char[n];
		for (int i = 0; i < n; i++) {
			while (true) {
				int k = ran.nextInt(10);
				if ((bitField & (1 << k)) == 0) {
					bitField |= 1 << k;
					chs[i] = (char) (k + '0');
					break;
				}
			}
		}
		return new String(chs);
	}

	/**
	 * 指定范围的随机数
	 * 
	 * @param min
	 *            最小值
	 * @param max
	 *            最大值
	 * @return
	 */
	public static int getRandomNum(int min, int max) {
		Random random = new Random();
		int s = random.nextInt(max) % (max - min + 1) + min;
		return s;
	}

	/**
	 * 检测字符串是否不为空(null,"","null")
	 * 
	 * @param s
	 * @return 不为空则返回true，否则返回false
	 */
	public static boolean notEmpty(String s) {
		return s != null && !"".equals(s) && !"null".equals(s);
	}

	/**
	 * 检测字符串是否为空(null,"","null")
	 * 
	 * @param s
	 * @return 为空则返回true，不否则返回false
	 */
	public static boolean isEmpty(String s) {
		return s == null || "".equals(s) || "null".equals(s);
	}

	/**
	 * 检测是否为数字
	 * 
	 * @param s
	 * @return
	 */
	public static boolean isNumber(String s) {
		try {
			Integer.parseInt(s);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 *  * 使用Introspector进行转换  map 转 对象
	 * @param map
	 * @param beanClass
	 * @return
	 * @throws Exception
	 */
	public static Object mapToObject2(Map<String, Object> map, Class<?> beanClass)
			throws Exception {
		if (map == null)
			return null;

		Object obj = beanClass.newInstance();

		BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
		PropertyDescriptor[] propertyDescriptors = beanInfo
				.getPropertyDescriptors();
		for (PropertyDescriptor property : propertyDescriptors) {
			Method setter = property.getWriteMethod();
			if (setter != null) {
				setter.invoke(obj, map.get(property.getName()));
			}
		}

		return obj;
	}

	
	public static String sha256HMAC(String content, String secret) {
		try {
			Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
			SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
			sha256_HMAC.init(secret_key);
			byte[] bytes = sha256_HMAC.doFinal(content.getBytes());

			StringBuffer buf = new StringBuffer();
			String stmp;
			for (int n = 0; bytes != null && n < bytes.length; n++) {
				stmp = Integer.toHexString(bytes[n] & 0XFF);
				if (stmp.length() == 1) {
					buf.append('0');
				}
				buf.append(stmp);
			}
			return buf.toString();
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * 使用reflect进行转换  ，map 转 对象
	 * @param map
	 * @param beanClass
	 * @return
	 * @throws Exception
	 */
	public static Object mapToObject(Map<String, Object> map, Class<?> beanClass)
			throws Exception {
		if (map == null)
			return null;

		Object obj = beanClass.newInstance();

		Field[] fields = obj.getClass().getDeclaredFields();
		for (Field field : fields) {
			int mod = field.getModifiers();
			if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
				continue;
			}

			field.setAccessible(true);
			field.set(obj, map.get(field.getName()));
		}

		return obj;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> List<T> mapListToObjList(List<ParameterMap> list, Class<T> beanClass) throws Exception{
		if(list ==null){
			return null;
		}
		List<T> objList = new ArrayList<T>();
		for(ParameterMap pm:list){
			T t = (T) mapToObject(pm, beanClass);
			objList.add(t);
		}
		return objList;
	}

}
