package com.mall.mallcoupon.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mall.common.result.Result;
import com.mall.mallcoupon.dto.CouponTemplateCreateDTO;
import com.mall.mallcoupon.dto.CouponTemplateUpdateDTO;
import com.mall.mallcoupon.service.CouponTemplateService;
import com.mall.mallcoupon.vo.CouponTemplateVO;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * <p>描述: </p>
 * <p>版权: &copy;highxin</p>
 *
 * @author highxin
 * @version 1.0.0
 * @since 2026/10/26
 */
@RestController
@RequestMapping("/coupon/admin/template")
@AllArgsConstructor
public class CouponAdminController {
    private final CouponTemplateService templateService;

    @PostMapping("/create")
    public Result<Long> create(@Valid @RequestBody CouponTemplateCreateDTO dto) {
        return Result.success(templateService.create(dto));
    }

    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id,
                               @Valid @RequestBody CouponTemplateUpdateDTO dto) {
        // 以路径参数为准，避免 body 里的 id 和路径不一致
        dto.setId(id);
        templateService.update(id, dto);
        return Result.success(null);
    }

    @PostMapping("/{id}/publish")
    public Result<Void> publish(@PathVariable Long id) {
        templateService.publish(id);
        return Result.success(null);
    }

    @PostMapping("/{id}/offline")
    public Result<Void> offline(@PathVariable Long id) {
        templateService.offline(id);
        return Result.success(null);
    }

    @GetMapping("/page")
    public Result<IPage<CouponTemplateVO>> page(
            @RequestParam(defaultValue = "1") long pageNum,
            @RequestParam(defaultValue = "10") long pageSize,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) String name) {
        return Result.success(templateService.page(pageNum, pageSize, status, name));
    }

    @GetMapping("/{id}")
    public Result<CouponTemplateVO> detail(@PathVariable Long id) {
        return Result.success(templateService.detail(id));
    }
}
