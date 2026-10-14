package com.mall.mallproduct.category.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall.mallproduct.category.dto.CategoryDTO;
import com.mall.mallproduct.category.entity.Category;
import com.mall.mallproduct.category.vo.CategoryDetailVO;
import com.mall.mallproduct.category.vo.CategoryListVO;
import jakarta.validation.Valid;

import java.util.List;

/**
 * <p>描述: </p>
 * <p>版权: &copy;highxin</p>
 *
 * @author highxin
 * @version 1.0.0
 * @since 2026/8/24
 */
public interface CategoryService extends IService<Category> {

    /**
     * 检查父分类是否存在
     * @param parentId 父分类id
     * @return 是否存在
     */
    boolean checkParentCategory(Long parentId);

    /**
     * 检查同级分类是否存在
     * @param name 分类名称
     * @param parentId 父分类id
     * @return 是否存在
     */
    boolean existsSameCategoryName(String name, Long parentId);

    /**
     * 检查同级分类是否存在(排除当前分类)
     * @param name 分类名称
     * @param parentId 父分类id
     * @param id 当前分类id
     * @return 是否存在
     */
    boolean existsSameCategoryName(String name, Long parentId, Long id);

    /**
     * 获取分类树
     * @return 分类树
     */
    List<CategoryListVO> tree();

    /**
     * 删除分类
     * @param id 分类id
     */
    void deleteCategory(Long id);

    /**
     * 更新分类
     * @param categoryDTO 分类dto
     */
    void updateCategory(CategoryDTO categoryDTO);

    /**
     * 获取分类详情
     * @param id 分类id
     * @return 分类详情
     */
    CategoryDetailVO getDetail(Long id);

    /**
     * 新增分类
     * @param categoryDTO 分类dto
     */
    void saveCategory(@Valid CategoryDTO categoryDTO);
}
