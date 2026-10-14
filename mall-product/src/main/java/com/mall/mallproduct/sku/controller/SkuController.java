package com.mall.mallproduct.sku.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mall.common.result.Result;
import com.mall.mallproduct.sku.dto.SkuDTO;
import com.mall.mallproduct.sku.dto.SkuQueryDTO;
import com.mall.mallproduct.sku.dto.SkuStatusDTO;
import com.mall.common.dto.SkuStockDTO;
import com.mall.mallproduct.sku.dto.SkuUpdateDTO;
import com.mall.mallproduct.sku.entity.Sku;
import com.mall.mallproduct.sku.service.SkuService;
import com.mall.mallproduct.sku.vo.SkuDetailVO;
import com.mall.mallproduct.sku.vo.SkuVO;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product/sku")
public class SkuController {

    private final SkuService skuService;

    public SkuController(SkuService skuService) {
        this.skuService = skuService;
    }

    @PostMapping("/save")
    public Result<String> saveSku(@Valid @RequestBody SkuDTO skuDTO) {
        skuService.saveSku(skuDTO);
        return Result.success("保存成功");
    }

    @DeleteMapping("/{id}")
    public Result<String> deleteSku(@PathVariable Long id) {
        skuService.deleteById(id);
        return Result.success("删除成功");
    }

    @PostMapping("/update")
    public Result<String> updateSku(@Valid @RequestBody SkuUpdateDTO skuUpdateDTO) {
        skuService.updateSku(skuUpdateDTO);
        return Result.success("更新成功");
    }

    @GetMapping("/detail/{id}")
    public Result<SkuDetailVO> getSkuDetail(@PathVariable Long id) {
        return Result.success(skuService.getSkuDetail(id));
    }

    @GetMapping("/list/{spuId}")
    public Result<List<Sku>> getSkuList(@PathVariable Long spuId) {
        return Result.success(skuService.getSkuList(spuId));
    }

    @GetMapping("/page")
    public Result<IPage<SkuVO>> page(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            SkuQueryDTO skuQueryDTO
    ){

        IPage<SkuVO> page = skuService.pageSku(
                        pageNum,
                        pageSize,
                        skuQueryDTO
                );

        return Result.success(page);
    }

    @PostMapping("/updateStatus")
    public Result<String> updateStatus(@Valid @RequestBody SkuStatusDTO skuStatusDTO) {
        skuService.updateStatus(skuStatusDTO);
        return Result.success("更新状态成功");
    }

    @PostMapping("/updateStock")
    public Result<String> updateStock(@Valid @RequestBody SkuStockDTO skuStockDTO) {
        skuService.updateStock(skuStockDTO);
        return Result.success("更新SKU库存成功");
    }

    @PostMapping("/deduct")
    public Result<String> deductStock(
            @RequestBody List<SkuStockDTO> list){
        skuService.deductStock(list);
        return Result.success("扣减库存成功");
    }

    @PostMapping("/addStock")
    public Result<String> addStock(@RequestBody List<SkuStockDTO> list){
        skuService.addStock(list);
        return Result.success("回补库存成功");
    }
}