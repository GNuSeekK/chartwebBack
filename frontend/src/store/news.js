import {defineStore} from 'pinia'
import newsAPI from "@/api/news";

export const useNewsStore = defineStore('news', () => {
  const getNewsList = async (keyword) => {
    const newNews = await newsAPI.getNewsList(keyword);
    return newNews.data;
  }

  return { getNewsList }
})