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
        @click="stockChange"
    >
      검색
    </a-button>
  </div>
</template>

<script setup>
import {computed, inject, reactive, ref} from "vue";
import {useStockStore} from "@/store/stock";
import router from "@/router";

const nowStock = inject('nowStockInfo')
const stockStore = useStockStore()
const stocks = reactive([])
stockStore.getStockInfoList().then((res) => {
  stocks.push(...res)
})


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

const stockChange = () => {
  const stock = filteredStocks.value.find((stock) => stock.name === stockName.value);
  console.log(stock)
  console.log(nowStock)
  if (stock) {
    nowStock.code = stock.code;
    nowStock.name = stock.name;
  }
  router.push('/chart')
  stockName.value = '';
};
</script>

<style scoped>
.search-bar-container {
  margin: 20px 0;
}
</style>