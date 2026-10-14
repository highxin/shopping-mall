package com.mall.mallproduct.specvalue.controller;

import com.mall.common.result.Result;
import com.mall.mallproduct.specvalue.dto.SpecValueDTO;
import com.mall.mallproduct.specvalue.dto.SpecValueUpdateDTO;
import com.mall.mallproduct.specvalue.entity.SpecValue;
import com.mall.mallproduct.specvalue.service.SpecValueService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product/spec-value")
public class SpecValueController {

    private final SpecValueService specValueService;

    public SpecValueController(SpecValueService specValueService) {
        this.specValueService = specValueService;
    }

    @PostMapping("/save")
    public Result<String> saveSpecValue(@Valid @RequestBody SpecValueDTO specValueDTO) {
        specValueService.saveSpecValue(specValueDTO);
        return Result.success("保存成功");
    }

    @DeleteMapping("/{id}")
    public Result<String> deleteSpecValue(@PathVariable Long id) {
        specValueService.deleteById(id);
        return Result.success("删除成功");
    }

    @PostMapping("/update")
    public Result<String> updateSpecValue(@Valid @RequestBody SpecValueUpdateDTO specValueDTO) {
        specValueService.updateSpecValue(specValueDTO);
        return Result.success("更新成功");
    }

    @GetMapping("/list/{specId}")
    public Result<List<SpecValue>> getSpecValueList(@PathVariable Long specId) {
        return Result.success(specValueService.getSpecValueList(specId));
    }
}