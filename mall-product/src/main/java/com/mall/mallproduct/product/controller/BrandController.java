package com.mall.mallproduct.product.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mall.common.exception.BusinessException;
import com.mall.common.result.Result;
import com.mall.mallproduct.product.dto.BrandDTO;
import com.mall.mallproduct.product.entity.Brand;
import com.mall.mallproduct.product.service.BrandService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/product/brand")
public class BrandController {


    private final BrandService brandService;


    public BrandController(BrandService brandService) {

        this.brandService = brandService;

    }


    @GetMapping("/list")
    public Result<List<Brand>> list(){
        List<Brand> list = brandService.list();
        return Result.success(list);
    }

    @PostMapping("/save")
    public Result<String> save(@Valid @RequestBody BrandDTO brandDTO){
        Brand exist = brandService.getByName(brandDTO.getName());
        if(exist != null){
            return Result.error( "品牌名称已存在");
        }
        Brand brand = new Brand();
        BeanUtils.copyProperties(brandDTO, brand);
        brandService.save(brand);
        return Result.success("保存成功");

    }

    @GetMapping("/page")
    public Result<Page<Brand>> page(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize
    ){
        Page<Brand> page = brandService.page(pageNum, pageSize);
        return Result.success(page);
    }

    @PutMapping("/update")
    public Result<String> update(@Valid @RequestBody BrandDTO brandDTO){
        Brand exist = brandService.getById(brandDTO.getId());
        if(exist == null){
            throw new BusinessException("品牌不存在");
        }
        Brand existName = brandService.getByNameAndNotId(brandDTO.getName(), brandDTO.getId());
        if(existName != null){
            throw new BusinessException("品牌名称已存在");
        }
        Brand brand = new Brand();
        BeanUtils.copyProperties(brandDTO, brand);
        brandService.updateById(brand);
        return Result.success("修改成功");
    }

    @DeleteMapping("/{id}")
    public Result<String> delete(@PathVariable Long id){
        brandService.removeById(id);
        return Result.success("删除成功");
    }

    @GetMapping("/{id}")
    public Result<Brand> detail(@PathVariable Long id){
        Brand brand = brandService.getById(id);
        if(brand == null){
            throw new BusinessException("品牌不存在");
        }
        return Result.success(brand);
    }
}