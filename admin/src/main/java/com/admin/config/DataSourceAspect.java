package com.admin.config;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;


/**
 * 
 * @author Administrator
 *
 */
//@Aspect
//@Component
//@Order(-1)	//保证该AOP在@transactional之前执行
public class DataSourceAspect {

	 /**
     * 使用空方法定义切点表达式
     */
    @Pointcut("execution(* com.otcbi.admin.service..*.*(..))")
    public void declareJointPointExpression() {
    }

    @Before("declareJointPointExpression()")
    public void setDataSourceKey(JoinPoint point){

        //根据连接点所属的类实例，动态切换数据源
//        if (point.getTarget() instanceof UsrAccountWithdrawService
//                ) {
//            DatabaseContextHolder.setDatabaseType(DatabaseType.SECONDDB);
//        } else {//连接点所属的类实例是（当然，这一步也可以不写，因为defaultTargertDataSource就是该类所用的mytestdb）
//            DatabaseContextHolder.setDatabaseType(DatabaseType.FIRSTDB);
//        }
    }
}
