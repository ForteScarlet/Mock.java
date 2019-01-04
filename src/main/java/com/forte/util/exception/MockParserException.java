package com.forte.util.exception;

/**
 * 假数据解析异常
 * @author ForteScarlet <[163邮箱地址]ForteScarlet@163.com>
 */
public class MockParserException extends MockException{

    public MockParserException() {
    }
    public MockParserException(String message) {
        super(message);
    }

    public MockParserException(String message, Throwable cause) {
        super(message, cause);
    }

    public MockParserException(Throwable cause) {
        super(cause);
    }

    public MockParserException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
