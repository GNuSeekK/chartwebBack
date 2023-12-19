<template>
  <NavBar />
  <a-layout
      :style="{
        paddingTop: '200px',
        backgroundColor: 'white'
      }"
  >
    <router-view />
  </a-layout>
</template>

<script setup>
import NavBar from './components/NavBar.vue';
import {provide, reactive} from "vue";

const nowStockInfo = reactive(
    {
      code: '005930',
      name: '삼성전자',
    }
)
provide('nowStockInfo', nowStockInfo)
</script>

<style>
/* 전역 스타일링 */
body {
  margin: 0;
  font-family: 'Roboto', sans-serif;
}

.logo-text {
  font-family: 휴먼편지체, sans-serif;
}
</style>