<template>
  <NewsSlider :newsList="newsList" :style="{
    marginTop: '80px',
  }"/>
  <section class="news-section">
    <div class="news-header">
      최신 뉴스
    </div>
    <NewsItem v-for="item in news" :key="item.title" :news="item" />
    <!-- More NewsItem components -->
  </section>
</template>

<script setup>
import NewsItem from '../items/NewsItem.vue'
import NewsSlider from "@/components/items/NewsSlider.vue";
import {ref} from "vue";

const news = ref([
  {
    title: '오늘 삼성 전자의 주가가 10% 상승했습니다.',
    description: '삼성 전자에서는 5G 통신에 대한 기대감이 커지면서 주가가 상승했습니다.',
    url: '뉴스 링크',
    imgLink: 'https://picsum.photos/200/200',
  },
  {
    title: '한국 시장의 물가가 2% 상승했습니다.',
    description: '코로나19로 인한 경기침체로 물가가 하락할 것으로 예상되었으나, 실제로는 상승했습니다.',
    url: '뉴스 링크',
    imgLink: 'https://picsum.photos/200/200',
  },
  // More news items
])

const newsList = ref([
  {
    title: '오늘 삼성 전자의 주가가 10% 상승했습니다.',
    image: 'https://picsum.photos/200/200',
  },
  {
    title: '한국 시장의 물가가 2% 상승했습니다.',
    image: 'https://picsum.photos/200/200',
  },
  // More news items
])
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