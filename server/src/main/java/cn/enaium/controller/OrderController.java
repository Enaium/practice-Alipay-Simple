/*
 * MIT License
 *
 * Copyright (c) 2023 Enaium
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package cn.enaium.controller;

import cn.enaium.model.entity.GoodsFetcher;
import cn.enaium.model.entity.Order;
import cn.enaium.model.entity.OrderDraft;
import cn.enaium.model.entity.OrderFetcher;
import cn.enaium.model.result.Result;
import cn.enaium.model.type.TradeStatus;
import cn.enaium.repository.GoodsRepository;
import cn.enaium.repository.OrderRepository;
import cn.enaium.service.OrderService;
import com.alipay.api.AlipayApiException;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import lombok.val;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

/**
 * @author Enaium
 */
@RestController
@Transactional
@RequestMapping("/order")
@AllArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final GoodsRepository goodsRepository;
    private final OrderRepository orderRepository;

    /**
     * place an order
     *
     * @param id goods id
     */
    @PostMapping("/place")
    public Result<Object> place(@RequestParam int id) {
        val goods = goodsRepository.findById(id);
        if (goods.isPresent()) {
            val outTradeNo = UUID.randomUUID().toString();
            orderRepository.insert(OrderDraft.$.produce(order -> {
                order.setStatus(TradeStatus.WAIT_BUYER_PAY);
                order.setGoodsId(goods.get().id());
                order.setOrderNo(outTradeNo);
                order.setTotalAmount(goods.get().perice());
                order.setCreateTime(LocalDateTime.now());
                order.setUpdateTime(LocalDateTime.now());
            }));
            return Result.Builder.success();
        } else {
            return Result.Builder.fail();
        }
    }

    /**
     * pay an order
     *
     * @param orderNo order number
     */
    @PostMapping("/pay")
    public Result<String> pay(@RequestParam String orderNo) throws AlipayApiException, JsonProcessingException {
        val order = orderRepository.findByOrderNo(orderNo);
        if (order.isPresent()) {
            return Result.Builder.success(orderService.createOrder(orderNo, order.get().goods().perice().toString(), order.get().goods().name()));
        }
        return Result.Builder.fail(Result.Status.ORDER_DOESNT_EXIST);
    }

    /**
     * get all order
     *
     * @param page current number of pages
     * @param size number of pages each page
     */
    @PostMapping("/all")
    public Result<Page<Order>> all(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {
        return Result.Builder.success(orderRepository.findAll(PageRequest.of(page, size),
                OrderFetcher.$
                        .allScalarFields()
                        .goods(
                                GoodsFetcher.$
                                        .allScalarFields()
                        )
        ));
    }

    /**
     * get order info
     *
     * @param orderNo order number
     */
    @PostMapping("/info")
    public Result<Optional<Order>> info(@RequestParam String orderNo) {
        return Result.Builder.success(orderRepository.findByOrderNo(orderNo));
    }

    /**
     * close an order
     *
     * @param orderNo order id
     */
    @PostMapping("/close")
    public Result<Object> close(@RequestParam String orderNo) throws AlipayApiException, JsonProcessingException {
        val order = orderRepository.findByOrderNo(orderNo);

        if (order.isEmpty()) {
            return Result.Builder.fail(Result.Status.ORDER_DOESNT_EXIST);
        }

        orderService.closeOrder(orderNo);
        orderRepository.update(OrderDraft.$.produce(o -> o.setId(order.get().id()).setStatus(TradeStatus.TRADE_CLOSED)));
        return Result.Builder.success();
    }

    /**
     * refund an order
     *
     * @param orderNo order id
     */
    @PostMapping("/refund")
    public Result<Object> refund(@RequestParam String orderNo) throws AlipayApiException, JsonProcessingException {
        val order = orderRepository.findByOrderNo(orderNo);

        if (order.isEmpty()) {
            return Result.Builder.fail(Result.Status.ORDER_DOESNT_EXIST);
        }

        orderService.refund(orderNo, order.get().totalAmount());
        orderRepository.update(OrderDraft.$.produce(o -> o.setId(order.get().id()).setStatus(TradeStatus.TRADE_FINISHED)));
        return Result.Builder.success();
    }
}
