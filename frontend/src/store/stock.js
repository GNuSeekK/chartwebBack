import {defineStore} from 'pinia'
import stockAPI from "@/api/stock";
import {ref} from "vue";

export const useStockStore = defineStore('stock', () => {
  const stockInfoList = ref([])

  const getStockInfoList = async () => {
    if (stockInfoList.value.length > 0) {
      return stockInfoList.value;
    }
    const newStockInfoList = await stockAPI.getStockInfoList();
    stockInfoList.value = newStockInfoList.stockList;
    return stockInfoList.value;
  }

  return { getStockInfoList }
})