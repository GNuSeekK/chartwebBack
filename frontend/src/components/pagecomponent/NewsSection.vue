<template>
  <div class="news-section">
    <!-- 뉴스 카드 반복 -->
    <a-card class="news-card" hoverable>
      <template #title>
        Company ABC announces record-breaking profits
      </template>
      <p>Read the full article to find out more.</p>
      <a-meta>
        <template #description>
          <a-tag color="volcano">Hot</a-tag>
          <a-tag color="orange">Trending</a-tag>
        </template>
      </a-meta>
    </a-card>
    <!-- 다른 뉴스 카드들도 이와 비슷하게 추가 -->
  </div>
</template>

<script setup>
// 뉴스 데이터 및 로직 추가
</script>

<style scoped>
.news-section {
  grid-column: span 3;
}

.news-card {
  margin-bottom: 20px;
}
</style>