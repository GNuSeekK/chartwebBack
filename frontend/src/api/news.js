import {localAxios} from "@/utils/http-commons.js";

const axios = localAxios();

const getNewsList = async (keyword) => {
  try {
    const response = await axios.get('/news?keyword=' + keyword);
    return response;
  } catch (error) {
    console.error('getStockInfoList:', error);
    throw error;
  }
};

export default { getNewsList };