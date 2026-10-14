package com.mall.mallproduct.spec.controller;

import com.mall.common.result.Result;
import com.mall.mallproduct.spec.dto.SpecDTO;
import com.mall.mallproduct.spec.dto.SpecUpdateDTO;
import com.mall.mallproduct.spec.entity.Spec;
import com.mall.mallproduct.spec.service.SpecService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>描述: </p>
 * <p>版权: &copy;highxin</p>
 *
 * @author highxin
 * @version 1.0.0
 * @since 2026/9/10
 */
@RestController
@RequestMapping("/product/spec")
public class SpecController {

    private final SpecService specService;
    public SpecController(SpecService specService) {
        this.specService = specService;
    }

    @PostMapping("/save")
    public Result<String> saveSpec(@Valid @RequestBody SpecDTO specDTO) {
        specService.saveSpec(specDTO);
        return Result.success("保存成功");
    }

    @DeleteMapping("/{id}")
    public Result<String> deleteSpec(@PathVariable Long id) {
        specService.deleteById(id);
        return Result.success("删除成功");
    }

    @PostMapping("/update")
        public Result<String> updateSpec(@Valid @RequestBody SpecUpdateDTO specUpdateDTO) {
        specService.updateSpec(specUpdateDTO);
        return Result.success("更新成功");
    }

    @GetMapping("/list/{spuId}")
    public Result<List<Spec>> getSpecList(@PathVariable Long spuId) {
        return Result.success(specService.getSpecList(spuId));
    }



}
