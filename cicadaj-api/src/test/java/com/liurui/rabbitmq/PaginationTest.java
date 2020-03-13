package com.liurui.rabbitmq;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author liu-rui
 * @date 2019/10/22 上午9:50
 * @description
 * @since
 */

public class PaginationTest {
    @Test
    public void test() {
        Pagination pagination = new Pagination(0, 10, 0, null);

        Assert.assertEquals(0, pagination.getTotalPages());
        pagination = new Pagination(0, 10, 1, null);

        Assert.assertEquals(1, pagination.getTotalPages());
        pagination = new Pagination(0, 10, 10, null);

        Assert.assertEquals(1, pagination.getTotalPages());
        pagination = new Pagination(0, 10, 11, null);

        Assert.assertEquals(2, pagination.getTotalPages());
        pagination = new Pagination(0, 10, 20, null);

        Assert.assertEquals(2, pagination.getTotalPages());
    }
}
