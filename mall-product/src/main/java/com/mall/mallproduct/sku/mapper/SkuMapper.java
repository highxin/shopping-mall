package com.mall.mallproduct.sku.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mall.mallproduct.sku.vo.SkuDetailVO;
import com.mall.mallproduct.sku.dto.SkuQueryDTO;
import com.mall.mallproduct.sku.entity.Sku;
import com.mall.mallproduct.sku.vo.SkuVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SkuMapper extends BaseMapper<Sku> {
    IPage<SkuVO> selectSkuPage(
            Page<SkuVO> page,
            @Param("query") SkuQueryDTO skuQueryDTO
    );

    List<SkuDetailVO> selectSkuDetail(Long id);

    int deductStock(@Param("skuId") Long id,@Param("quantity") Integer quantity);

    int addStock(@Param("skuId") Long id,@Param("quantity") Integer quantity);
}