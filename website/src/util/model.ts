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

export interface IResult<T> {
    code: number
    message: string
    metadata: T
}

export interface IPage<T> {
    content: T[]
    totalPages: number
    totalElements: number
    size: number
    number: number
}

export interface IGoods {
    id: number
    name: string
    perice: number
}

export interface IOrder {
    id: number
    goodsId: number
    goods: IGoods
    orderNo: string
    totalAmount: string
    status: string
    createTime: number
    updateTime: number
}

export enum TradeStatus {
    WAIT_BUYER_PAY = "WAIT_BUYER_PAY",
    TRADE_CLOSED = "TRADE_CLOSED",
    TRADE_SUCCESS = "TRADE_SUCCESS",
    TRADE_FINISHED = "TRADE_FINISHED"
}