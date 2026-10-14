package com.mall.mallproduct.spec.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("product_spec")
public class Spec {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 所属SPU
     */
    private Long spuId;

    /**
     * 规格名称
     * 例如：颜色、内存
     */
    private String name;

    /**
     * 排序
     */
    private Integer sort;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    /**
     * 逻辑删除
     */
    @TableLogic
    private Integer deleted;
}