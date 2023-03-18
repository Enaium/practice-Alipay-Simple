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

import cn.enaium.configuration.AlipayConfiguration;
import cn.enaium.model.entity.OrderDraft;
import cn.enaium.repository.OrderRepository;
import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.val;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

/**
 * @author Enaium
 */
@RestController
@RequestMapping("/alipay")
@AllArgsConstructor
public class AlipayController {
    private final AlipayConfiguration configuration;
    private final OrderRepository orderRepository;

    @PostMapping("/notify")
    public void notifyURL(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException, AlipayApiException {
        val param = new HashMap<String, String>();
        httpServletRequest.getParameterMap().forEach((k, v) -> param.put(k, String.join(",", v)));
        val check = AlipaySignature.rsaCheckV1(param, configuration.alipayPublicKey, StandardCharsets.UTF_8.name(), configuration.signType);

        if (!check) {
            httpServletResponse.getWriter().write("Payment fail");
            return;
        }


        val outTradeNo = param.get("out_trade_no");
        val tradeStatus = param.get("trade_status");

        val order = orderRepository.findByOrderNo(outTradeNo);

        order.ifPresent(value -> orderRepository.update(OrderDraft.$.produce(o -> o.setId(value.id()).setStatus(tradeStatus))));

        switch (tradeStatus) {
            case "WAIT_BUYER_PAY" -> httpServletResponse.getWriter().write("Wait");
            case "TRADE_CLOSED" -> httpServletResponse.getWriter().write("Payment close");
            case "TRADE_SUCCESS" -> httpServletResponse.getWriter().write("Payment success");
            case "TRADE_FINISHED" -> httpServletResponse.getWriter().write("Payment finished");
        }

        httpServletResponse.getWriter().close();
    }
}
