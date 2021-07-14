
package com.etocrm.memcached.pojo;


import java.io.Serializable;

public class ResultGenerator implements Serializable {

	private static final String DEFAULT_SUCCESS_MESSAGE = "SUCCESS";
	private static final long serialVersionUID = 1695213183390143942L;

	/**
	 * 创建成功Result
	 * @return
	 */
	public static Result genSuccessResult() {
		return new Result().setCode(ResultCode.SUCCESS).setMessage(DEFAULT_SUCCESS_MESSAGE);
	}
	/**
	 * 创建成功Result
	 * @return
	 */
	public static Result genAsSuccessResult(String message) {
		return new Result().setCode(ResultCode.SUCCESS).setMessage(message);
	}
	/**
	 * 创建 含 data的Result
	 * @param data
	 * @return
	 */
	public static Result genSuccessResult(Object data) {
		return new Result().setCode(ResultCode.SUCCESS).setMessage(DEFAULT_SUCCESS_MESSAGE).setData(data);
	}
	/**
	 * 创建 失败的Result ， message为传入参数
	 * @param message
	 * @return
	 */
	public static Result genFailResult(String message) {
		return new Result().setCode(ResultCode.FAIL).setMessage(message);
	}
	/**
	 * 自定义返回code ，message
	 * @param code
	 * @param message
	 * @return
	 */
	public static Result genFailResult(Integer code,String message) {
		return new Result().setCode(code).setMessage(message);
	}

	public static String getDefaultSuccessMessage() {
		return DEFAULT_SUCCESS_MESSAGE;
	}

	/**
	 * 自定义返回参数异常message
	 * @param message
	 * @return
	 */
	public static Result genParamErrorResult(String message){
		return new Result().setCode(ResultCode.PARAM_ERROR).setMessage(message);
	}
	
	/**
	* 判断result返回的是否是成功主体
	* @author      kevin
	* @param      result
	* @return      
	* @exception   
	* @date        2019/7/13 21:51
	*/
	public static boolean isSuccess(Result result) {
		return null != result && ResultCode.SUCCESS.code == result.getCode();
	}


	/**
	 * 根据ErrorCode创建Result
	 * @param set
	 * @return
	 */
    public static Result genDefaultFailMessage(ErrorCode set) {
		switch (set){
			case KEY_NOTEXITS : return new Result().setCode(ErrorCode.KEY_NOTEXITS.code).setMessage("key 不存在！");
			case KEY_EXITS : return new Result().setCode(ErrorCode.KEY_EXITS.code).setMessage("key 已存在！");
			case NULL_KEY : return new Result().setCode(ErrorCode.NULL_KEY.code).setMessage("key 不可为null！");
			case EXTRA_LONG : return new Result().setCode(ErrorCode.EXTRA_LONG.code).setMessage("key 长度过长！");
			case ENCODE_ERROR : return new Result().setCode(ErrorCode.ENCODE_ERROR.code).setMessage("key 不可为中文或其他不支持的类型！");
			case INCREMENT_ERROR : return new Result().setCode(ErrorCode.INCREMENT_ERROR.code).setMessage("增量不正确！");
			case CONNECTION_ERROR : return new Result().setCode(ErrorCode.CONNECTION_ERROR.code).setMessage("服务器连接异常！");
			case NULL_VALUE : return new Result().setCode(ErrorCode.NULL_VALUE.code).setMessage("value 不能为null！");
			case VALUE_TYPE_ERROR : return new Result().setCode(ErrorCode.VALUE_TYPE_ERROR.code).setMessage("value 类型不正确！");
			default : return new Result().setCode(ErrorCode.SUCCESS.code).setMessage("成功！");
		}
    }

    /**
	 * 根据ErrorCode创建Result
	 * @param data
	 * @return
	 */
    public static Result genDefaultFailMessage(Object data) {
		return data instanceof Enum ? genDefaultFailMessage((ErrorCode) data) :
				new Result().setCode(ErrorCode.SUCCESS.code).setMessage("成功！").setData(data);
    }



}
