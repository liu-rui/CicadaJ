package com.liurui;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * @author: 刘锐
 * @date: 18-12-10 下午8:21
 * @description: 分页信息
 * @since 0.1
 */
@ApiModel("分页")
@Getter
@ToString
public class Pagination<T> implements Serializable {

    private static final long serialVersionUID = -5163980836046020921L;

    /**
     * 页索引
     */
    @ApiModelProperty("页索引")
    private int pageIndex;

    /**
     * 页大小
     */
    @ApiModelProperty("页大小")
    private int pageSize;

    /**
     * 总个数
     */
    @ApiModelProperty("总个数")
    private long totalItems;

    /**
     * 总页数
     */
    @ApiModelProperty("总页数")
    private int totalPages;

    /**
     * 元素列表
     */
    @ApiModelProperty("元素列表")
    private List<T> items;

    /**
     * 构造函数
     */
    public Pagination() {
    }

    /**
     * 构造函数
     *
     * @param pageIndex  页索引
     * @param pageSize   页大小
     * @param totalItems 总个数
     * @param items      元素列表
     */
    public Pagination(int pageIndex, int pageSize, long totalItems, List<T> items) {
        if (pageSize <= 0) {
            throw new IllegalArgumentException("pageSize必须大于0");
        }
        int totalPages = totalItems == 0
                ? 0
                : (int) Math.ceil(1D * totalItems / pageSize);

        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
        this.totalItems = totalItems;
        this.totalPages = totalPages;
        this.items = items;
    }

    /**
     * 构造函数
     *
     * @param pageIndex  页索引
     * @param pageSize   页大小
     * @param totalItems 总个数
     * @param totalPages 总页数
     * @param items      元素列表
     */
    public Pagination(int pageIndex, int pageSize, long totalItems, int totalPages, List<T> items) {
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
        this.totalItems = totalItems;
        this.totalPages = totalPages;
        this.items = items;
    }
}
