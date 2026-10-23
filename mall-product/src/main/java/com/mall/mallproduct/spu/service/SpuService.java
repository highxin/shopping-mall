package com.mall.mallproduct.spu.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mall.common.dto.ProductIndexDTO;
import com.mall.mallproduct.spu.dto.SpuDTO;
import com.mall.mallproduct.spu.dto.SpuQueryDTO;
import com.mall.mallproduct.spu.dto.SpuUpdateDTO;
import com.mall.mallproduct.spu.entity.Spu;
import com.mall.mallproduct.spu.vo.SpuVO;

import java.util.List;

/**
 * <p>描述: </p>
 * <p>版权: &copy;highxin</p>
 *
 * @author highxin
 * @version 1.0.0
 * @since 2026/9/2
 */
public interface SpuService extends IService<Spu> {

    /**
     * 保存SPU
     * @param spuDTO SPUDTO
     */
    void saveSpu(SpuDTO spuDTO);

    /**
     * 分页查询SPU
     * @param page 页码
     * @param size 每页数量
     * @return 分页结果
     */
    IPage<SpuVO> pageSpu(Integer page, Integer size, SpuQueryDTO queryDTO);

    /**
     * 获取SPU详情
     * @param id SPU ID
     * @return SPUVO
     */
    SpuVO getSpuDetail(Long id);

    /**
     * 更新SPU
     * @param spuUpdateDTO SPU更新DTO
     * @return SPUVO
     */
    void updateSpu(SpuUpdateDTO spuUpdateDTO);

    /**
     * 删除SPU
     * @param id SPU ID
     */
    void deleteSpu(Long id);

    /**
     * 查询商品索引所需的全部数据
     *
     * @return 商品索引数据列表
     */
    List<ProductIndexDTO> listForSearch();


}