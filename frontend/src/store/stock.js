import {defineStore} from 'pinia'
import stockAPI from "@/api/stock";
import {ref, reactive} from "vue";

export const useStockStore = defineStore('stock', () => {
  const stockInfoList = ref([])
  const priceRequest = reactive({
    priceList: [],
    code: null,
    start: null,
    end: null
  })

  const getStockInfoList = async () => {
    if (stockInfoList.value.length > 0) {
      return stockInfoList.value;
    }
    const newStockInfoList = await stockAPI.getStockInfoList();
    stockInfoList.value = newStockInfoList.stockList;
    return stockInfoList.value;
  }

  const loadStockPrices = async (code, start, end) => {
    priceRequest.code = code;
    priceRequest.start = start;
    priceRequest.end = end;
    const requestResult = await stockAPI.fetchStockPrices(code, start, end);
    priceRequest.priceList = requestResult['stockPriceDtoList']
  }

  const extendStockPrices = async (duration) => {
    const start = priceRequest.start - duration;
    const end = priceRequest.end - duration;
    const requestResult= await stockAPI.fetchStockPrices(priceRequest.code, start, end);
    const newPriceList = requestResult['stockPriceDtoList'];
    priceRequest.priceList = [...newPriceList, ...priceRequest.priceList];
    priceRequest.start = start;
    priceRequest.end = end;
  }

  const getStockPriceList = () => {
    return priceRequest.priceList;
  }

  const getPriceRequest = () => {
    return priceRequest;
  }

  return { getStockInfoList, loadStockPrices, extendStockPrices, getStockPriceList, getPriceRequest }
})