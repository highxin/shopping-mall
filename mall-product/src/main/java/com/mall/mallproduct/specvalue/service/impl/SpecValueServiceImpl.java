package com.mall.mallproduct.specvalue.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall.common.exception.BusinessException;
import com.mall.mallproduct.spec.entity.Spec;
import com.mall.mallproduct.spec.service.SpecService;
import com.mall.mallproduct.specvalue.dto.SpecValueDTO;
import com.mall.mallproduct.specvalue.dto.SpecValueUpdateDTO;
import com.mall.mallproduct.specvalue.entity.SpecValue;
import com.mall.mallproduct.specvalue.mapper.SpecValueMapper;
import com.mall.mallproduct.specvalue.service.SpecValueService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpecValueServiceImpl extends ServiceImpl<SpecValueMapper, SpecValue> implements SpecValueService {

    private final SpecService specService;

    public SpecValueServiceImpl(SpecService specService) {
        this.specService = specService;
    }

    @Override
    public void deleteById(Long id) {
        SpecValue specValue = getById(id);
        if (specValue == null) {
            throw new BusinessException("规格值不存在");
        }
        removeById(id);
    }

    @Override
    public void updateSpecValue(SpecValueUpdateDTO specValueUpdateDTO) {
        SpecValue existSpecValue = getById(specValueUpdateDTO.getId());
        if (existSpecValue == null) {
            throw new BusinessException("规格值不存在");
        }
        existSpecValue.setValue(specValueUpdateDTO.getValue());
        existSpecValue.setSort(specValueUpdateDTO.getSort());
        updateById(existSpecValue);
    }

    @Override
    public List<SpecValue> getSpecValueList(Long specId) {
        return lambdaQuery().eq(SpecValue::getSpecId, specId).list();
    }

    @Override
    public void saveSpecValue(SpecValueDTO specValueDTO) {
        Spec spec = specService.getById(specValueDTO.getSpecId());
        if (spec == null) {
            throw new BusinessException("规格不存在");
        }
        // 检查规格值是否存在已存在
        long count = lambdaQuery()
                .eq(SpecValue::getSpecId, specValueDTO.getSpecId())
                .eq(SpecValue::getValue, specValueDTO.getValue())
                .count();

        if(count > 0){
            throw new BusinessException("规格值已存在");
        }
        SpecValue specValue = new SpecValue();
        BeanUtils.copyProperties(specValueDTO, specValue);
        save(specValue);
    }
}