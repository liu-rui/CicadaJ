package com.liurui.rabbitmq;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;

import static com.liurui.rabbitmq.ReturnDataCodeEnum.SERVER_ERROR;
import static com.liurui.rabbitmq.ReturnDataCodeEnum.SUCCESS;

/**
 * @param <T> 数据类型
 * @author： 刘锐
 * @Date: 2018/12/06 18:41
 * @Description: 返回数据类型
 * @since 0.1
 */
@ApiModel("返回值")
@Getter
@ToString
public class ReturnData<T> implements Serializable {
    private static final long serialVersionUID = -5163980836046019923L;

    /**
     * 结果代码
     */
    @ApiModelProperty("结果代码")
    private int code;

    /**
     * 消息
     */
    @ApiModelProperty("消息")
    private String message;

    /**
     * 数据
     */
    @ApiModelProperty("数据")
    private T data;

    /**
     * 当前数据结构的版本号, 版本升级意味着json数据结构变化
     */
    @ApiModelProperty("当前数据结构的版本号, 版本升级意味着json数据结构变化")
    private String version = "1.0";


    /**
     * 可选, 接口接到请求到返回数据花销的毫秒数, 建议填写
     */
    @ApiModelProperty("可选, 接口接到请求到返回数据花销的毫秒数")
    private long elapsed;


    /**
     * 构造函数
     */
    public ReturnData() {

    }

    public ReturnData(ReturnDataCode returnDataCode) {
        this(returnDataCode.getCode(), returnDataCode.getMessage(), null);
    }

    public ReturnData(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }


    public void setElapsed(long elapsed) {
        this.elapsed = elapsed;
    }

    /**
     * 是否成功
     *
     * @return 是否成功
     */
    @ApiModelProperty("是否成功")
    public boolean isSuccess() {
        return SUCCESS.getCode() == this.code;
    }

    /**
     * 成功
     *
     * @return 成功的数据返回结果
     */
    public static ReturnData success() {
        return new ReturnData(SUCCESS);
    }

    /**
     * 成功
     *
     * @param data 数据
     * @param <T>  数据类型
     * @return 成功的数据返回结果
     */
    public static <T> ReturnData<T> success(T data) {
        return new ReturnData<T>(SUCCESS.getCode(), SUCCESS.getMessage(), data);
    }

    /**
     * 服务器端异常
     *
     * @return 服务器端异常的数据返回结果
     */
    public static ReturnData serverError() {
        return new ReturnData(SERVER_ERROR);
    }

    /**
     * 服务器端异常
     *
     * @param message 消息
     * @return 服务器端异常的数据返回结果
     */
    public static ReturnData serverError(String message) {
        return error(SERVER_ERROR.getCode(), message);
    }

    /**
     * 错误
     *
     * @param code 状态码结构
     * @return 失败的数据返回结果
     */
    public static ReturnData error(ReturnDataCode code) {
        if (code == null) {
            throw new IllegalStateException("code");
        }
        return error(code.getCode(), code.getMessage());
    }

    /**
     * 错误
     *
     * @param code    状态码
     * @param message 消息
     * @return 失败的数据返回结果
     */
    public static ReturnData error(int code, String message) {
        if (SUCCESS.getCode() == code) {
            throw new IllegalArgumentException("错误码不能与成功码相同");
        }
        return new ReturnData(code, message, null);
    }


    /**
     * 错误
     *
     * @param returnData 返回数据
     * @return 失败的数据返回结果
     */
    public static ReturnData error(ReturnData returnData) {
        if (returnData == null) {
            throw new IllegalArgumentException();
        }
        return error(returnData.code, returnData.message);
    }
}
