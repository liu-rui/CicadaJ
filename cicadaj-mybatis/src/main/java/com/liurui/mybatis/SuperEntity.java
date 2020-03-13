package com.liurui.mybatis;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author liu-rui
 * @date 16:23
 * @description  实体基础类
 * @since 0.1.0
 */
@Data
public class SuperEntity implements Serializable {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -4801865210961587582L;


    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
