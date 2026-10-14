package com.mall.mallproduct.spu.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mall.common.result.Result;
import com.mall.mallproduct.spu.dto.SpuDTO;
import com.mall.mallproduct.spu.dto.SpuQueryDTO;
import com.mall.mallproduct.spu.dto.SpuUpdateDTO;
import com.mall.mallproduct.spu.service.SpuService;
import com.mall.mallproduct.spu.vo.SpuVO;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/**
 * <p>描述: </p>
 * <p>版权: &copy;highxin</p>
 *
 * @author highxin
 * @version 1.0.0
 * @since 2026/9/2
 */
@RestController
@RequestMapping("/product/spu")
public class SupController {

    private final SpuService spuService;

    public SupController(SpuService spuService) {
        this.spuService = spuService;
    }

    @PostMapping("/save")
    public Result<String> save(@Valid @RequestBody SpuDTO spuDTO) {
        spuService.saveSpu(spuDTO);
        return Result.success("商品新增成功");
    }

    @GetMapping("/page")
    public Result<IPage<SpuVO>> page(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            SpuQueryDTO spuQueryDTO
    ){
        IPage<SpuVO> page = spuService.pageSpu(pageNum, pageSize, spuQueryDTO);
        return Result.success(page);
    }

    @GetMapping("/{id}")
    public Result<SpuVO> detail(@PathVariable Long id) {
        SpuVO spuVO = spuService.getSpuDetail(id);
        return Result.success(spuVO);
    }

    @PostMapping("/update")
    public Result<String> update(@Valid @RequestBody SpuUpdateDTO spuUpdateDTO) {
        spuService.updateSpu(spuUpdateDTO);
        return Result.success("商品更新成功");
    }

    @DeleteMapping("/{id}")
    public Result<String> delete(@PathVariable Long id) {
        spuService.deleteSpu(id);
        return Result.success("商品删除成功");
    }
}
