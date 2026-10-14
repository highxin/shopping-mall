package com.mall.mallorder.feign;

import com.mall.common.result.Result;
import com.mall.mallorder.vo.AddressVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("mall-user")
public interface AddressFeignClient {

    @GetMapping("/user/address/{id}")
    Result<AddressVO> get(@PathVariable Long id);

}