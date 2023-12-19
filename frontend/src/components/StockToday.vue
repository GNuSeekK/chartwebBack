<template>
  <Preparing>
    <template #description>
      <span>오늘의 주식</span>은 현재
    </template>
  </Preparing>
</template>

<script setup>
import Preparing from "@/components/common/Preparing.vue";
</script>

<style scoped>
span {
  font-family: 휴먼편지체, sans-serif;
  color: #166bdf;
}
</style>
```