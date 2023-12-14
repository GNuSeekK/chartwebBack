<template>
  <div class="search-bar-container">
    <a-auto-complete
        v-model:value="stockName"
        :options="stockNames"
        placeholder="주식 이름을 입력하세요."
        :style="{
          width: '250px',
        }"
        size="large"
    />
    <a-button
        type="primary"
        :style="{
          marginLeft: '20px',
          backgroundColor: 'black',
          width: '80px',
        }"
        size="large"
    >
      검색
    </a-button>
  </div>
</template>

<script setup>
import {computed, reactive, ref, watch} from "vue";

const stocks = reactive([
  {
    name: '삼성전자',
    code: '005930',
  },
  {
    name: 'SK하이닉스',
    code: '000660',
  },
  {
    name: 'LG화학',
    code: '051910',
  }
])

const stockName = ref('');
const filteredStocks = computed(() => {
  return stocks.filter((stock) => {
    return stock.name.includes(stockName.value);
  });
});
const stockNames = computed(() => {
  return filteredStocks.value.map((stock) => {
    return {
      value: stock.name,
    };
  });
});
</script>

<style scoped>
.search-bar-container {
  margin: 20px 0;
}
</style>