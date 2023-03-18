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
import {useRoute} from "vue-router";
import http from "@/util/http";
import {ref} from "vue";
import {TradeStatus} from "@/util/model";

const route = useRoute();

const status = ref()

if (route.query.out_trade_no) {
  http.post("/order/info", {orderNo: route.query.out_trade_no}).then(r => {
    if (r.data.code === 200) {
      switch (r.data.metadata.status) {
        case TradeStatus.WAIT_BUYER_PAY:
          status.value = "Wait for pay"
          break
        case TradeStatus.TRADE_SUCCESS:
          status.value = "Pay successfully"
          break
        case TradeStatus.TRADE_FINISHED:
          status.value = "Pay Finished"
          break
        case TradeStatus.TRADE_CLOSED:
          status.value = "Pay closed"
          break
      }
    }
  })
}
</script>

<template>
  <div style="display: flex;justify-content: center">
    <div style="font-size: 36px">{{ status }}</div>
  </div>
</template>

<style scoped>

</style>
