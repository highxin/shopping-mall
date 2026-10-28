package com.mall.mallcoupon.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.mall.mallcoupon.entity.Coupon;
import com.mall.mallcoupon.entity.CouponTemplate;
import com.mall.mallcoupon.enums.CouponTypeEnum;
import com.mall.mallcoupon.enums.IssueTypeEnum;
import com.mall.mallcoupon.enums.TemplateStatusEnum;
import com.mall.mallcoupon.enums.ValidTypeEnum;
import com.mall.mallcoupon.exception.CouponException;
import com.mall.mallcoupon.mapper.CouponMapper;
import com.mall.mallcoupon.mapper.CouponTemplateMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class CouponReceiveConcurrencyTest {

    @Autowired
    private CouponReceiveService receiveService;

    @Autowired
    private CouponTemplateMapper templateMapper;

    @Autowired
    private CouponMapper couponMapper;

    @Test
    void shouldNotOversell() throws Exception {
        int stock = 100;
        int threads = 300;
        CouponTemplate template = newTemplate(stock);
        templateMapper.insert(template);

        ExecutorService pool = Executors.newFixedThreadPool(threads);
        CountDownLatch ready = new CountDownLatch(threads);
        CountDownLatch start = new CountDownLatch(1);
        List<Future<Boolean>> futures = new ArrayList<>();

        for (int i = 0; i < threads; i++) {
            long userId = 10_000L + i;
            futures.add(pool.submit(() -> {
                ready.countDown();
                start.await(5, TimeUnit.SECONDS);
                try {
                    receiveService.receive(template.getId(), userId);
                    return true;
                } catch (CouponException | DuplicateKeyException e) {
                    // 并发下唯一键冲突也是“没抢到”，不是测试失败
                    return false;
                }
            }));
        }

        ready.await(10, TimeUnit.SECONDS);
        start.countDown();

        long success = 0;
        for (Future<Boolean> future : futures) {
            if (future.get(10, TimeUnit.SECONDS)) {
                success++;
            }
        }
        pool.shutdown();

        assertThat(success).isEqualTo(stock);
        assertThat(templateMapper.selectById(template.getId()).getRemainingStock()).isZero();
        assertThat(couponMapper.selectCount(
                Wrappers.<Coupon>lambdaQuery().eq(Coupon::getTemplateId, template.getId())))
                .isEqualTo(stock);
    }

    @Test
    void sameUserShouldOnlyReceiveOnce() throws Exception {
        int threads = 50;
        CouponTemplate template = newTemplate(100);
        templateMapper.insert(template);

        ExecutorService pool = Executors.newFixedThreadPool(threads);
        CountDownLatch start = new CountDownLatch(1);
        List<Future<Boolean>> futures = new ArrayList<>();
        long userId = 99_999L;

        for (int i = 0; i < threads; i++) {
            futures.add(pool.submit(() -> {
                start.await(5, TimeUnit.SECONDS);
                try {
                    receiveService.receive(template.getId(), userId);
                    return true;
                } catch (CouponException | DuplicateKeyException e) {
                    // 并发下唯一键冲突也是“没抢到”，不是测试失败
                    return false;
                }
            }));
        }
        start.countDown();

        long success = 0;
        for (Future<Boolean> future : futures) {
            if (future.get(10, TimeUnit.SECONDS)) {
                success++;
            }
        }
        pool.shutdown();

        assertThat(success).isEqualTo(1);
        assertThat(templateMapper.selectById(template.getId()).getRemainingStock()).isEqualTo(99);
    }

    private CouponTemplate newTemplate(int stock) {
        LocalDateTime now = LocalDateTime.now();
        CouponTemplate template = new CouponTemplate();
        template.setName("并发测试券");
        template.setType(CouponTypeEnum.NO_THRESHOLD.getCode());
        template.setScopeType(1);
        template.setThresholdAmount(BigDecimal.ZERO);
        template.setDiscountAmount(new BigDecimal("1.00"));
        template.setTotalStock(stock);
        template.setRemainingStock(stock);
        template.setPerUserLimit(1);
        template.setIssueType(IssueTypeEnum.RECEIVE.getCode());
        template.setValidType(ValidTypeEnum.FIXED.getCode());
        template.setValidStartTime(now.minusDays(1));
        template.setValidEndTime(now.plusDays(30));
        template.setReceiveStartTime(now.minusDays(1));
        template.setReceiveEndTime(now.plusDays(1));
        template.setStatus(TemplateStatusEnum.PUBLISHED.getCode());
        return template;
    }
}