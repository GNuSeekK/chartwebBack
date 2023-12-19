import {localAxios} from "@/utils/http-commons.js";

const axios = localAxios();

const getStockInfoList = async () => {
  try {
    const response = await axios.get('/stock/info-list');
    return response.data;
  } catch (error) {
    console.error('getStockInfoList:', error);
    throw error;
  }
};

const fetchStockPrices = async (code, start, end) => {
  try {
    const response = await localAxios().get(`/stock/price`, {
      params: { code, start, end }
    });
    return response.data;
  } catch (error) {
    console.error('Error fetching stock prices:', error);
    throw error;
  }
};
export default { getStockInfoList, fetchStockPrices };