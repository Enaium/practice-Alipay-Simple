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
import {goodsAll} from "@/util/request";
import {ref} from "vue";
import {IPage, IGoods} from "@/util/model";
import http from "@/util/http";

const goods = ref(<IPage<IGoods>>{})

goodsAll().then(r => {
  goods.value = r.metadata
})

const buy = (id: number) => {
  http.post("/order/place", {id}).then(r => {
    if (r.data.code === 200) {
      window.$router.push({name: "order"})
    }
  })
}
</script>

<template>
  <n-list bordered>
    <n-list-item v-for="g in goods.content">
      <n-space justify="space-between" align="center">
        <div>{{ g.name }}</div>
        <n-button type="info" @click="buy(g.id)">Buy</n-button>
      </n-space>
    </n-list-item>
  </n-list>
</template>

<style scoped>

</style>
