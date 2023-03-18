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

package cn.enaium.repository;

import cn.enaium.model.entity.GoodsFetcher;
import cn.enaium.model.entity.Order;
import cn.enaium.model.entity.OrderFetcher;
import cn.enaium.model.entity.OrderTable;
import lombok.val;
import org.babyfish.jimmer.spring.repository.JRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Enaium
 */
@Repository
public interface OrderRepository extends JRepository<Order, Long> {
    default Optional<Order> findByOrderNo(String orderNo) {
        val orders = sql().createQuery(OrderTable.$).where(OrderTable.$.orderNo().eq(orderNo)).select(OrderTable.$.fetch(OrderFetcher.$.allScalarFields().goods(GoodsFetcher.$.allScalarFields()))).execute();
        if (orders.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(orders.get(0));
        }
    }
}
