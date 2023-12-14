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

export default { getStockInfoList };