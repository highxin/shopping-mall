package com.mall.mallproduct.spec.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall.mallproduct.spec.dto.SpecDTO;
import com.mall.mallproduct.spec.dto.SpecUpdateDTO;
import com.mall.mallproduct.spec.entity.Spec;

import java.util.List;

/**
 * <p>描述: </p>
 * <p>版权: &copy;highxin</p>
 *
 * @author highxin
 * @version 1.0.0
 * @since 2026/9/10
 */
public interface SpecService extends IService<Spec> {

    /**
     * 删除规格
     * @param id 规格ID
     */
    void deleteById(Long id);

    /**
     * 更新规格
     * @param specUpdateDTO 规格dto
     */
    void updateSpec(SpecUpdateDTO specUpdateDTO);

    /**
     * 获取规格列表
     * @param spuId spuID
     * @return 规格列表
     */
    List<Spec> getSpecList(Long spuId);

    /**
     * 保存规格
     * @param specDTO 规格
     */
    void saveSpec(SpecDTO specDTO);
}
