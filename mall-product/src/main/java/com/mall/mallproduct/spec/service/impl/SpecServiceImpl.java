package com.mall.mallproduct.spec.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.mall.common.exception.BusinessException;
import com.mall.mallproduct.spec.dto.SpecDTO;
import com.mall.mallproduct.spec.dto.SpecUpdateDTO;
import com.mall.mallproduct.spec.service.SpecService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall.mallproduct.spec.entity.Spec;
import com.mall.mallproduct.spec.mapper.SpecMapper;
import com.mall.mallproduct.spu.entity.Spu;
import com.mall.mallproduct.spu.mapper.SpuMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * <p>描述: </p>
 * <p>版权: &copy;highxin</p>
 *
 * @author highxin
 * @version 1.0.0
 * @since 2026/9/10
 */
@Service
public class SpecServiceImpl extends ServiceImpl<SpecMapper, Spec> implements SpecService {
    private final SpuMapper spuMapper;


    public SpecServiceImpl(SpuMapper spuMapper) {
        this.spuMapper = spuMapper;
    }


    @Override
    public void deleteById(Long id) {
        Spec spec = getById(id);
        if (spec == null) {
            throw new BusinessException("规格不存在");
        }
        removeById(id);
    }

    @Override
    public void updateSpec(SpecUpdateDTO specUpdateDTO) {
        Spec spec = getById(specUpdateDTO.getId());
        if (spec == null) {
            throw new BusinessException("规格不存在");
        }
        BeanUtils.copyProperties(specUpdateDTO, spec);
        updateById(spec);
    }

    @Override
    public List<Spec> getSpecList(Long spuId) {
        LambdaQueryWrapper<Spec> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Spec::getSpuId, spuId);
        return list(wrapper);
    }

    @Override
    public void saveSpec(SpecDTO specDTO) {
        // 查询SPU是否存在
        Spu spu = spuMapper.selectOne(
                Wrappers.lambdaQuery(Spu.class).eq(Spu::getId, specDTO.getSpuId()));
        if (spu == null) {
            throw new BusinessException("SPU不存在");
        }
        // 保存规格
        Spec spec = new Spec();
        BeanUtils.copyProperties(specDTO, spec);
        save(spec);
    }
}