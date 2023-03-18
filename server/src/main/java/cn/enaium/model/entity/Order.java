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

package cn.enaium.model.entity;

import org.babyfish.jimmer.sql.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author Enaium
 */
@Entity
@Table(name = "t_order")
public interface Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id();

    int goodsId();

    @OneToOne
    Goods goods();

    @Column(name = "order_no")
    String orderNo();

    @Column(name = "total_amount")
    BigDecimal totalAmount();

    @Column(name = "status")
    String status();

    @Column(name = "create_time")
    LocalDateTime createTime();

    @Column(name = "update_time")
    LocalDateTime updateTime();
}
