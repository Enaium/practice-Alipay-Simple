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

package cn.enaium.service;

import cn.enaium.configuration.AlipayConfiguration;
import com.alipay.api.AlipayApiException;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradeCloseRequest;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @author Enaium
 */
@Service
@AllArgsConstructor
public class OrderService {
    private final AlipayConfiguration configuration;

    public String createOrder(String out_trade_no, String total_amount, String subject) throws JsonProcessingException, AlipayApiException {
        val client = new DefaultAlipayClient(configuration.gateway, configuration.appId, configuration.privateKey, "json", StandardCharsets.UTF_8.name(), configuration.alipayPublicKey, configuration.signType);
        val request = new AlipayTradePagePayRequest();
        request.setNotifyUrl(configuration.notifyUrl);
        request.setReturnUrl(configuration.returnUrl);
        request.setBizContent(new ObjectMapper().writeValueAsString(Map.of(
                "out_trade_no", out_trade_no,
                "total_amount", total_amount,
                "subject", subject,
                "product_code", "FAST_INSTANT_TRADE_PAY"
        )));
        val response = client.pageExecute(request);
        return response.getBody();
    }

    public void closeOrder(String out_trade_no) throws JsonProcessingException, AlipayApiException {
        val client = new DefaultAlipayClient(configuration.gateway, configuration.appId, configuration.privateKey, "json", StandardCharsets.UTF_8.name(), configuration.alipayPublicKey, configuration.signType);
        val request = new AlipayTradeCloseRequest();
        request.setBizContent(new ObjectMapper().writeValueAsString(Map.of(
                "out_trade_no", out_trade_no
        )));
        client.execute(request);
    }

    public void refund(String out_trade_no, BigDecimal amount) throws JsonProcessingException, AlipayApiException {
        val client = new DefaultAlipayClient(configuration.gateway, configuration.appId, configuration.privateKey, "json", StandardCharsets.UTF_8.name(), configuration.alipayPublicKey, configuration.signType);
        AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
        request.setBizContent(new ObjectMapper().writeValueAsString(Map.of(
                "out_trade_no", out_trade_no,
                "refund_amount", amount.toString()
        )));
        client.execute(request);
    }
}
