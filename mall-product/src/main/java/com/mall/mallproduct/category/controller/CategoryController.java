package com.mall.mallproduct.category.controller;

import com.mall.common.result.Result;
import com.mall.mallproduct.category.dto.CategoryDTO;
import com.mall.mallproduct.category.service.CategoryService;
import com.mall.mallproduct.category.vo.CategoryDetailVO;
import com.mall.mallproduct.category.vo.CategoryListVO;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>描述: </p>
 * <p>版权: &copy;highxin</p>
 *
 * @author highxin
 * @version 1.0.0
 * @since 2026/8/24
 */
@RestController
@RequestMapping("/product/category")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    /**
     * 新增分类
     * @param categoryDTO 分类dto
     * @return 新增分类结果
     */
    @PostMapping("/save")
    public Result<String> save(@Valid @RequestBody CategoryDTO categoryDTO){
        categoryService.saveCategory(categoryDTO);
        return Result.success("新增分类成功");
    }

    /**
     * 获取分类树
     * @return 分类树
     */
    @GetMapping("/tree")
    public Result<List<CategoryListVO>> tree(){
        return Result.success(categoryService.tree());
    }

    /**
     * 删除分类
     * @param id 分类id
     * @return 删除分类结果
     */
    @DeleteMapping("/delete/{id}")
    public Result<String> delete(@PathVariable Long id){
        categoryService.deleteCategory(id);
        return Result.success("删除分类成功");
    }

    /**
     * 更新分类
     * @param categoryDTO 分类dto
     * @return 更新分类结果
     */
    @PutMapping("/update")
    public Result<String> update(@Valid @RequestBody CategoryDTO categoryDTO){
        categoryService.updateCategory(categoryDTO);
        return Result.success("更新分类成功");
    }

    /**
     * 获取分类详情
     * @param id 分类id
     * @return 分类详情
     */
    @GetMapping("/{id}")
    public Result<CategoryDetailVO> getById(@PathVariable Long id){
        CategoryDetailVO vo = categoryService.getDetail(id);
        return Result.success(vo);
    }
}
