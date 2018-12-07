package com.forte.util.exception;

/**
 * 参数数量不符异常
 *
 * @author ForteScarlet <[163邮箱地址]ForteScarlet@163.com>
 */
public class ParameterSizeException extends RuntimeException {
    public ParameterSizeException() {
        super("参数数量与方法参数数量不符！");
    }
}
