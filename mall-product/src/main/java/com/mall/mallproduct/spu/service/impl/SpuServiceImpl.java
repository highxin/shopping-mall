package com.mall.mallproduct.spu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall.common.exception.BusinessException;
import com.mall.mallproduct.category.entity.Category;
import com.mall.mallproduct.category.service.CategoryService;
import com.mall.mallproduct.product.entity.Brand;
import com.mall.mallproduct.product.service.BrandService;
import com.mall.mallproduct.spu.dto.SpuDTO;
import com.mall.mallproduct.spu.dto.SpuQueryDTO;
import com.mall.mallproduct.spu.dto.SpuUpdateDTO;
import com.mall.mallproduct.spu.entity.Spu;
import com.mall.mallproduct.spu.mapper.SpuMapper;
import com.mall.mallproduct.spu.service.SpuService;
import com.mall.mallproduct.spu.vo.SpuVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * <p>描述: </p>
 * <p>版权: &copy;highxin</p>
 *
 * @author highxin
 * @version 1.0.0
 * @since 2026/9/2
 */
@Service
public class SpuServiceImpl extends ServiceImpl<SpuMapper, Spu> implements SpuService {
    private final BrandService brandService;
    private final CategoryService categoryService;

    public SpuServiceImpl(BrandService brandService, CategoryService categoryService) {
        this.brandService = brandService;
        this.categoryService = categoryService;
    }

    @Override
    public void saveSpu(SpuDTO spuDTO) {
        // 校验品牌是否存在
        checkBrandAndCategoryExist(spuDTO.getBrandId(), spuDTO.getCategoryId());

        Spu spu = new Spu();
        BeanUtils.copyProperties(spuDTO, spu);
        save(spu);
    }

    @Override
    public IPage<SpuVO> pageSpu(Integer page, Integer size, SpuQueryDTO queryDTO) {
        // 1.创建分页
        Page<Spu> SpuPage = new Page<>(page, size);

        LambdaQueryWrapper<Spu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(
                queryDTO.getName() != null,
                Spu::getName,
                queryDTO.getName()
        );
        queryWrapper.eq(
                queryDTO.getBrandId() != null,
                Spu::getBrandId,
                queryDTO.getBrandId()
        );
        queryWrapper.eq(
                queryDTO.getCategoryId() != null,
                Spu::getCategoryId,
                queryDTO.getCategoryId()
        );
        queryWrapper.eq(
                queryDTO.getStatus() != null,
                Spu::getStatus,
                queryDTO.getStatus()
        );
        // 2. 分页查询
        IPage<Spu> result = page(SpuPage, queryWrapper);
        // 3. Entity 转 VO
        return result.convert(spu -> {
            SpuVO spuVO = new SpuVO();
            BeanUtils.copyProperties(spu, spuVO);
            return spuVO;
        });
    }

    @Override
    public SpuVO getSpuDetail(Long id) {
        Spu spu = getById(id);
        if (spu == null) {
            throw new BusinessException("SPU不存在");
        }
        SpuVO spuVO = new SpuVO();
        BeanUtils.copyProperties(spu, spuVO);
        return spuVO;
    }

    @Override
    public void updateSpu(SpuUpdateDTO spuUpdateDTO) {
        Spu spu = getById(spuUpdateDTO.getId());
        if (spu == null) {
            throw new BusinessException("SPUId不存在");
        }
        // 校验品牌和分类是否存在
        checkBrandAndCategoryExist(spuUpdateDTO.getBrandId(), spuUpdateDTO.getCategoryId());

        BeanUtils.copyProperties(spuUpdateDTO, spu);
        updateById(spu);
    }

    @Override
    public void deleteSpu(Long id) {
        Spu spu = getById(id);
        if (spu == null) {
            throw new BusinessException("SPU不存在");
        }
        removeById(id);
    }

    /**
     * 校验品牌和分类是否存在
     * @param brandId 品牌ID
     * @param categoryId 分类ID
     */
    private void checkBrandAndCategoryExist(Long brandId, Long categoryId) {
        Brand brand = brandService.getById(brandId);
        if (brand == null) {
            throw new BusinessException("品牌不存在");
        }
        // 校验分类是否存在
        Category category = categoryService.getById(categoryId);
        if (category == null) {
            throw new BusinessException("分类不存在");
        }
    }

}