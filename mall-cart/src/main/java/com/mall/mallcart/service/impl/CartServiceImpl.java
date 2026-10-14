package com.mall.mallcart.service.impl;

import com.mall.common.constants.RedisConstant;
import com.mall.common.exception.BusinessException;
import com.mall.common.result.Result;
import com.mall.mallcart.dto.CartAddDTO;
import com.mall.mallcart.dto.CartUpdateDTO;
import com.mall.mallcart.feign.ProductFeignClient;
import com.mall.mallcart.service.CartService;
import com.mall.mallcart.vo.CartItemVO;
import com.mall.mallcart.vo.SkuDetailVO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final ProductFeignClient productFeignClient;

    private final RedisTemplate<String,Object> redisTemplate;

    

    @Override
    public List<CartItemVO> getCartList(Long userId) {

        String cartKey =
                "cart:" + userId;
        List<Object> cartList =
                redisTemplate.opsForHash().values(cartKey);

        List<CartItemVO> voList = new ArrayList<>();
        for(Object obj:cartList){
            CartItemVO item = (CartItemVO) obj;
            if(item.getQuantity()<=0){
                redisTemplate.opsForHash().delete(cartKey, userId.toString());
            }else{
                voList.add(item);
            }
        }
        return voList;
    }

    @Override 
    public void addCart(CartAddDTO dto,Long userId) { 
 
        String cartKey = "cart:"+userId; 
        String hashKey = dto.getSkuId().toString(); 
 
        // 检查购物车是否已存在该商品 
        CartItemVO oldItem = 
                (CartItemVO) redisTemplate.opsForHash().get(cartKey, hashKey); 
        if (oldItem != null) { 
            // 如果已存在，更新数量 
            oldItem.setQuantity(oldItem.getQuantity() + dto.getQuantity()); 
            if(oldItem.getQuantity()<=0){
                oldItem.setQuantity(0);
            }
            // 更新Redis 
            redisTemplate.opsForHash().put(cartKey, hashKey, oldItem); 
            return;
        }
 
        //1.查询SKU 
        Result<SkuDetailVO> result = 
                productFeignClient.getSkuDetail(dto.getSkuId()); 
        if(result.getData()==null){ 
            throw new BusinessException("商品不存在"); 
        } 
        SkuDetailVO sku = result.getData(); 
        //2.组装购物车数据 
        CartItemVO item = new CartItemVO(); 
        item.setSkuId(sku.getId()); 
        item.setSkuName(sku.getName()); 
        item.setPrice(sku.getPrice()); 
        item.setQuantity(dto.getQuantity());
        item.setChecked(true);
 
        redisTemplate.opsForHash().put(cartKey, hashKey, item); 
 
    }

    @Override
    public void updateCart(CartUpdateDTO dto,Long userId) {
        String cartKey =
                "cart:" + userId;
        String hashKey =
                dto.getSkuId().toString();
        CartItemVO item = (CartItemVO) redisTemplate.opsForHash()
                .get(cartKey, hashKey);
        if(item == null){
            throw new BusinessException(
                    "购物车商品不存在");
        }
        // 修改数量
        item.setQuantity(dto.getQuantity());
        // 更新Redis
        redisTemplate.opsForHash().put(cartKey, hashKey, item);

    }

    @Override
    public void deleteCart(List<Long> skuIds,Long userId) {
        if (skuIds == null || skuIds.isEmpty()) {
            return;
        }
        String cartKey =
                "cart:" + userId;
        String[] fields =
                skuIds.stream()
                        .map(String::valueOf)
                        .toArray(String[]::new);
        redisTemplate.opsForHash()
                .delete(cartKey, (Object[]) fields);
    }

    @Override 
    public void clear(Long userId){
        if(userId==null){
            throw new BusinessException("用户未登录");
        }
        String cartKey = RedisConstant.CART_PREFIX + userId;
        redisTemplate.delete(cartKey);
    }

    @Override
    public void checked(Long skuId,Boolean checked,Long userId){
        String cartKey = RedisConstant.CART_PREFIX + userId;
        String hashKey = skuId.toString();
        CartItemVO item = (CartItemVO)redisTemplate.opsForHash().get(cartKey,hashKey);
        if(item == null){
            throw new BusinessException("商品不存在");
        }
        item.setChecked(checked);
        redisTemplate.opsForHash().put(cartKey,hashKey,item);

    }

    @Override
    public List<CartItemVO> checkedList(Long userId){
    String cartKey = RedisConstant.CART_PREFIX + userId;
    List<Object> values = redisTemplate.opsForHash().values(cartKey);

    return values.stream()
            .map(item -> (CartItemVO)item)
            .filter(CartItemVO::getChecked)
            .collect(Collectors.toList());
    }

}