package org.liberty.j.wasteland.Exception;

public enum ErrorEnum {
    // 数据操作错误定义
	SUCCESS(200, "访问成功"),
	NO_PERMISSION(403,"权限不足"),
	NO_AUTH(401,"未登录"),
	NOT_FOUND(404, "未找到该资源"),
	INTERNAL_SERVER_ERROR(500, "服务器连接失败"),
	;

	/** 错误码 */
	private Integer errorCode;

	/** 错误信息 */
	private String errorMsg;

	ErrorEnum(Integer errorCode, String errorMsg) {
		this.errorCode = errorCode;
		this.errorMsg = errorMsg;
	}

    public Integer getErrorCode() {
        return errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }
}