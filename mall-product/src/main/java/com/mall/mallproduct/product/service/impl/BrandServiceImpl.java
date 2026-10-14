package com.mall.mallproduct.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall.mallproduct.product.mapper.BrandMapper;
import com.mall.mallproduct.product.service.BrandService;
import com.mall.mallproduct.product.entity.Brand;
import org.springframework.stereotype.Service;

/**
 * <p>描述: </p>
 * <p>版权: &copy;highxin</p>
 *
 * @author highxin
 * @version 1.0.0
 * @since 2026/8/22
 */
@Service
public class BrandServiceImpl extends ServiceImpl<BrandMapper, Brand> implements BrandService {
    private final BrandMapper brandMapper;

    public BrandServiceImpl(BrandMapper brandMapper) {
        this.brandMapper = brandMapper;
    }

    public Page<Brand> page(Integer pageNum, Integer pageSize) {
        Page<Brand> page = new Page<>(pageNum, pageSize);
        return brandMapper.selectPage(page, null);
    }

    @Override
    public Brand getByNameAndNotId(String name, Long id) {
        LambdaQueryWrapper<Brand> queryWrapper = new LambdaQueryWrapper<Brand>()
                .eq(Brand::getName, name)
                .ne(Brand::getId, id);
        return brandMapper.selectOne(queryWrapper);
    }

    @Override
    public Brand getByName(String name) {
        LambdaQueryWrapper<Brand> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Brand::getName, name);
        return brandMapper.selectOne(queryWrapper);
    }
}
