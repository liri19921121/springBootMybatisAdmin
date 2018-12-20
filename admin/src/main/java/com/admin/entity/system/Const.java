package com.admin.entity.system;

/**
 * 这是一个常量的类
 * @author tyro
 *
 */
public class Const {
	public final static String SESSION_USER="SESSION_USER";
	public final static String SESSION_ALL_MENU="SESSION_ALL_MENU";
	public final static String SESSION_QX="QX";
	public final static String GLOBAL_SESSION="GLOBAL_SESSION";
	
	//每页显示的数量
	public final static int PAGE_SIZE=15;

	/**
	 * 操作类型: out 支出，in 收入
	 */
	public static  final int OPERATE_STATE_OUT=0;
	public static  final int OPERATE_STATE__IN=1;

	/**
	 * 是否入账：Y--已入账  N--没入账
	 */
	public static  final int INTO_ACCOUNT_Y=1;
	public static  final int INTO_ACCOUNT_N=0;
	
}
