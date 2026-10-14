package com.mall.mallproduct.category.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall.common.exception.BusinessException;
import com.mall.mallproduct.category.dto.CategoryDTO;
import com.mall.mallproduct.category.entity.Category;
import com.mall.mallproduct.category.mapper.CategoryMapper;
import com.mall.mallproduct.category.service.CategoryService;
import com.mall.mallproduct.category.vo.CategoryDetailVO;
import com.mall.mallproduct.category.vo.CategoryListVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>描述: </p>
 * <p>版权: &copy;highxin</p>
 *
 * @author highxin
 * @version 1.0.0
 * @since 2026/8/24
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Override
    public boolean checkParentCategory(Long parentId) {
        // 没有父分类id，默认是根分类，返回true
        if (parentId == null || parentId == 0L) {
            return true;
        }
        // 检查父分类是否存在(存在返回true)
        Category category = getById(parentId);
        return category != null;
    }

    @Override
    public boolean existsSameCategoryName(String name, Long parentId) {
        // 检查同级分类是否存在(存在返回true)
        Category category = lambdaQuery()
                .eq(Category::getParentId, parentId)
                .eq(Category::getName, name)
                .one();
        return category != null;
    }

    @Override
    public boolean existsSameCategoryName(String name, Long parentId, Long id) {
        return lambdaQuery()
                .eq(Category::getParentId, parentId)
                .eq(Category::getName, name)
                .ne(Category::getId, id)
                .one() != null;
    }

    @Override
    public void deleteCategory(Long id) {
        // 查询分类
        Category category = getById(id);
        if (category == null) {
            throw new BusinessException("分类不存在");
        }
        // 查询是否有子分类
        Long count = lambdaQuery()
                .eq(Category::getParentId, id)
                .count();
        if (count > 0) {
            throw new BusinessException("分类下有子分类，不能删除");
        }
        // 删除分类
        removeById(id);
    }

    @Override
    public void saveCategory(CategoryDTO categoryDTO) {
        // 检查父分类是否存在
        if (!checkParentCategory(categoryDTO.getParentId())) {
            throw new BusinessException("父分类不存在");
        }
        // 检查分类名称是否存在
        if (existsSameCategoryName(categoryDTO.getName(), categoryDTO.getParentId())) {
            throw new BusinessException("分类名称已存在");
        }
        Category category = new Category();
        BeanUtils.copyProperties(categoryDTO, category);
        save(category);
    }

    @Override
    public CategoryDetailVO getDetail(Long id) {
        Category category = getById(id);
        if (category == null) {
            throw new BusinessException("分类不存在");
        }
        CategoryDetailVO categoryDetailVO = new CategoryDetailVO();
        BeanUtils.copyProperties(category, categoryDetailVO);
        return categoryDetailVO;
    }

    @Override
    public void updateCategory(CategoryDTO categoryDTO) {
        // 查询分类
        Category category = getById(categoryDTO.getId());
        if (category == null) {
            throw new BusinessException("分类不存在");
        }
        // 不能自己作为自己的父类
        if(categoryDTO.getParentId() != null && categoryDTO.getParentId().equals(categoryDTO.getId())) {
            throw new BusinessException("不能自己作为自己的父类");
        }
        // 检查父分类是否存在
        if(!checkParentCategory(categoryDTO.getParentId())) {
            throw new BusinessException("父分类不存在");
        }
        // 检查同级分类是否存在
        if(existsSameCategoryName(categoryDTO.getName(), categoryDTO.getParentId(), categoryDTO.getId())) {
            throw new BusinessException("同级分类名称已存在");
        }
        // 更新分类
        BeanUtils.copyProperties(categoryDTO, category);
        updateById(category);
    }

    @Override
    public List<CategoryListVO> tree() {
        List<Category> categories = list();
        // 平铺分类树
        List<CategoryListVO> categoryListVOList = categories.stream()
                .map(category -> {
                    CategoryListVO categoryListVo = new CategoryListVO();
                    BeanUtils.copyProperties(category, categoryListVo);
                    return categoryListVo;
                })
                .toList();
        // 递归构建分类树
        return buildTree(0L, categoryListVOList);
    }

    /**
     * 递归构建分类树
     * @param parentId 父分类id
     * @param categoryList 分类列表
     * @return 子分类列表
     */
    private List<CategoryListVO> buildTree(Long parentId, List<CategoryListVO> categoryList) {
        List<CategoryListVO> children = categoryList.stream()
                .filter(category ->
                        parentId.equals(category.getParentId())
                ).toList();
        for (CategoryListVO child : children) {
            child.setChildren(buildTree(child.getId(), categoryList));
        }
        return children;
    }
}
