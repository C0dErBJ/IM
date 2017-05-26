package com.AlphaZ.aspect;

import com.AlphaZ.constant.StatusCode;
import com.AlphaZ.entity.api.ResponseModel;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

/**
 * ProjectName: AlphaZ
 * PackageName: com.AlphaZ.com.AlphaZ.util
 * User: C0dEr
 * Date: 2017/2/24
 * Time: 上午10:46
 * Description:This is a class of com.AlphaZ.com.AlphaZ.util
 */
public class ExceptionHandler {
    protected org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(this.getClass());

    public Object around(ProceedingJoinPoint point) {
        Object responseModel = null;
        try {
            responseModel = point.proceed();
        } catch (Throwable throwable) {
            if (((MethodSignature) point.getSignature()).getReturnType().getName().equals(ResponseModel.class.getName())) {
                responseModel = new ResponseModel();
                ((ResponseModel) responseModel).statusCode = StatusCode.FAIL;
                ((ResponseModel) responseModel).message = ((MethodSignature) point.getSignature()).getMethod() + " 操作发生异常";
                ((ResponseModel) responseModel).data = throwable.getMessage();
            }
            logger.info(this.getClass().getName() + ": 操作发生异常");
            throwable.printStackTrace();
            //手动设置事物回滚，防止异常被吃掉无法触发回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return responseModel;
    }
}
