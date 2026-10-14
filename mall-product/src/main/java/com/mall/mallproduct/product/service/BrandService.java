package com.mall.mallproduct.product.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mall.mallproduct.product.entity.Brand;

/**
 * <p>描述: </p>
 * <p>版权: &copy;highxin</p>
 *
 * @author highxin
 * @version 1.0.0
 * @since 2026/8/22
 */

public interface BrandService extends IService<Brand> {
    /**
     * 分页查询品牌列表
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @return 品牌分页列表
     */
    Page<Brand> page(Integer pageNum, Integer pageSize);
    /**
     * 根据品牌名称查询品牌
     * @param name 品牌名称
     * @return 品牌
     */
    Brand getByName(String name);

    /**
     * 根据品牌名称和排除品牌ID查询品牌
     * @param name 品牌名称
     * @param id 排除的品牌ID
     * @return 品牌
     */
    Brand getByNameAndNotId(String name, Long id);
}
