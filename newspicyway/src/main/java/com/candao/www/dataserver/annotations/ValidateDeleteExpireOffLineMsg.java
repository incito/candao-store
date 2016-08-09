package com.candao.www.dataserver.annotations;

import java.lang.reflect.Method;
public class ValidateDeleteExpireOffLineMsg {
    public static DeleteExpireOffLineMsg parseDeleteExpireOffLineMsg(Class<?> clazz, String methodName, Class<?>... parameterTypes) throws NoSuchMethodException {
        // 根据方法名，取得方法，如果有则返回
        Method method = clazz.getMethod(methodName, parameterTypes);
        if (method != null && method.isAnnotationPresent(DeleteExpireOffLineMsg.class)) {
            DeleteExpireOffLineMsg deleteExpireOffLineMsg = method.getAnnotation(DeleteExpireOffLineMsg.class);
            if (null != deleteExpireOffLineMsg)
                return deleteExpireOffLineMsg;
        }
        return null;
    }

    public static DeleteExpireOffLineMsg parseDeleteExpireOffLineMsg(Method method) throws NoSuchMethodException {
        // 根据方法名，取得方法，如果有则返回
        if (method != null && method.isAnnotationPresent(DeleteExpireOffLineMsg.class)) {
            DeleteExpireOffLineMsg deleteExpireOffLineMsg = method.getAnnotation(DeleteExpireOffLineMsg.class);
            if (null != deleteExpireOffLineMsg)
                return deleteExpireOffLineMsg;
        }
        return null;
    }

    public static void main(String[] args) {
    }
}
