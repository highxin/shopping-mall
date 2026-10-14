package com.mall.mallproduct.specvalue.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall.mallproduct.specvalue.dto.SpecValueDTO;
import com.mall.mallproduct.specvalue.dto.SpecValueUpdateDTO;
import com.mall.mallproduct.specvalue.entity.SpecValue;

import java.util.List;

public interface SpecValueService extends IService<SpecValue> {

    /**
     * 删除规格值
     * @param id 规格值ID
     */
    void deleteById(Long id);

    /**
     * 更新规格值
     * @param specValueUpdateDTO 规格值更新DTO
     */
    void updateSpecValue(SpecValueUpdateDTO specValueUpdateDTO);

    /**
     * 获取规格值列表
     * @param specId 规格ID
     * @return 规格值列表
     */
    List<SpecValue> getSpecValueList(Long specId);

    /**
     * 保存规格值
     * @param specValueDTO 规格值DTO
     */
    void saveSpecValue(SpecValueDTO specValueDTO);
}