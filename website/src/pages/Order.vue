<!--
  - MIT License
  -
  - Copyright (c) 2023 Enaium
  -
  - Permission is hereby granted, free of charge, to any person obtaining a copy
  - of this software and associated documentation files (the "Software"), to deal
  - in the Software without restriction, including without limitation the rights
  - to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
  - copies of the Software, and to permit persons to whom the Software is
  - furnished to do so, subject to the following conditions:
  -
  - The above copyright notice and this permission notice shall be included in all
  - copies or substantial portions of the Software.
  -
  - THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
  - IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
  - FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
  - AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
  - LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
  - OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
  - SOFTWARE.
  -
  -->

<script setup lang="ts">
import {h, ref} from "vue";
import {IOrder, IPage, TradeStatus} from "@/util/model";
import {orderAll} from "@/util/request";
import {NButton, NButtonGroup, NSpace, NTag} from "naive-ui";
import http from "@/util/http";

const columns = [
  {
    title: "ID",
    key: "id"
  },
  {
    title: "Goods",
    key: "goods",
    render(order: IOrder) {
      return order.goods.name
    }
  },
  {
    title: "Order",
    key: "orderNo"
  },
  {
    title: "Total Amount",
    key: "totalAmount"
  },
  {
    title: "Status",
    key: "status",
    width: 100,
    render(order: IOrder) {
      let type: "default" | "error" | "primary" | "info" | "success" | "warning" = "default"
      switch (order.status) {
        case TradeStatus.WAIT_BUYER_PAY:
          type = "warning"
          break
        case TradeStatus.TRADE_CLOSED:
          type = "error"
          break
        case TradeStatus.TRADE_SUCCESS:
          type = "primary"
          break
        case TradeStatus.TRADE_FINISHED:
          type = "info"
          break
      }
      return h(NTag, {type}, order.status)
    }
  },
  {
    title: "Create Time",
    key: "createTime"
  },
  {
    title: "Update Time",
    key: "updateTime"
  },
  {
    title: "Action",
    width: 100,
    render(order: IOrder) {
      switch (order.status) {
        case TradeStatus.WAIT_BUYER_PAY:
          return h(NButtonGroup,
              {style: "width:100%"},
              [
                h(NButton, {
                  type: "primary",
                  onClick() {
                    pay(order.orderNo)
                  }
                }, "Pay"),
                h(NButton, {
                  type: "error",
                  onClick() {
                    close(order.orderNo)
                  }
                }, "Close")
              ]);
        case TradeStatus.TRADE_CLOSED:
          return h(NTag, {type: "error"}, order.status)
        case TradeStatus.TRADE_SUCCESS:
          return h(NButton, {
            type: "primary",
            style: "width:100%",
            onClick() {
              refund(order.orderNo)
            }
          }, "Refund");
        case TradeStatus.TRADE_FINISHED:
          return h(NTag, {type: "info"},  order.status);
      }
    }
  }
]

const order = ref(<IPage<IOrder>>{})

orderAll().then(r => {
  order.value = r.metadata
})

const pay = (orderNo: string) => {
  http.post("/order/pay", {orderNo}).then(r => {
    if (r.data.code === 200) {
      const alipayForm = document.getElementsByTagName('alipay')
      if (alipayForm.length) {
        document.body.removeChild(alipayForm[0])
      }
      const div = document.createElement('alipay')
      div.innerHTML = r.data.metadata
      document.body.appendChild(div)
      document.forms[0].setAttribute('target', '_blank')
      document.forms[0].submit()
    }
  })
}

const close = (orderNo: string) => {
  window.$dialog.warning({
    title: 'Warning',
    content: 'Do you want to close the order',
    positiveText: 'Yes',
    negativeText: 'No',
    onPositiveClick: () => {
      http.post("/order/close", {orderNo}).then(r => {
        if (r.data.code === 200) {
          window.$message.success("Close successfully")
        }
      })
    }
  })
}

const refund = (orderNo: string) => {
  window.$dialog.warning({
    title: 'Warning',
    content: 'Do you want to refund',
    positiveText: 'Yes',
    negativeText: 'No',
    onPositiveClick: () => {
      http.post("/order/refund", {orderNo}).then(r => {
        if (r.data.code === 200) {
          window.$message.success("Refund successfully")
        }
      })
    }
  })
}
</script>

<template>
  <n-data-table :columns="columns" :data="order.content" :single-line="false">

  </n-data-table>
</template>

<style scoped>

</style>
