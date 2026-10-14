package com.mall.mallproduct.specvalue.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("product_spec_value")
public class SpecValue {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 所属规格ID
     */
    private Long specId;

    /**
     * 规格值
     * 例如：红色、128GB
     */
    private String value;

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