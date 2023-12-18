<template>
  <NewsSlider :news-list="newsSliderList" :style="{
    marginTop: '50px',
    marginRight: '50px',
  }"/>
  <section class="news-section">
    <div class="news-header">
      최신 뉴스
    </div>
    <NewsItem v-for="item in filteredTwoNews" :key="item.link" :news="item" />
    <!-- More NewsItem components -->
  </section>
</template>

<script setup>
import NewsItem from '../items/NewsItem.vue'
import NewsSlider from "@/components/items/NewsSlider.vue";
import {useNewsStore} from "@/store/news"
import {computed, ref, watch} from "vue";

const newsStore = useNewsStore();

const keyword = ref('주식')
const news = ref([])
const newsSliderList = ref([])


newsStore.getNewsList(keyword.value).then((res) => {
  news.value.push(...res)
  newsSliderList.value.push(...res.slice(0, 10).map((item) => {
    return {
      title: item.title,
      image: item.imageLink,
      link: item.link,
    }
  }))
})

watch(keyword, () => {
  newsStore.getNewsList(keyword.value).then((res) => {
    news.value = res
  })
})

const filteredTwoNews = computed(() => {
  return news.value.slice(0, 2)
})



</script>

<style scoped>
.news-section {
  background: white;
  padding: 16px;
  border-radius: 8px;
}

.news-header {
  margin-bottom: 16px;
  margin-left: 16px;
  font-size: 40px;
  font-weight: bold;
}
</style>