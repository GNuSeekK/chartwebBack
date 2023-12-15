<template>
  <div class="news-item">
    <img :src="props.news.imgLink" alt="News Image"/>

    <div class="contents-wrapper">
      <div class="news-content">
        <h3>{{ props.news.title }}</h3>
        <p>{{ props.news.description }}</p>
      </div>
      <div class="news-reactions">
        <span>👍</span>
        <span>❤️</span>
      </div>
    </div>
  </div>
</template>

<script setup>
const props = defineProps({
  news: {
    type: Object,
    required: true
  }
})
</script>

<style lang="scss" scoped>
.news-item {
  display: flex;
  justify-content: left;
  align-items: center;
  margin-bottom: 16px;
  border: 2px solid #eee;
  border-radius: 20px;
  padding: 16px;
  min-width: 600px;
}

.news-content {
  margin-bottom: 8px;

  h3 {
    margin-top: 30px;
    font-weight: bold;
    font-size: 20px;
    color: #333;
  }
}

.news-tags span {
  background: #eee;
  padding: 4px 8px;
  margin-right: 8px;
  border-radius: 16px;
  font-size: 14px;
}

.news-reactions span {
  font-size: 24px;
  margin-left: 8px;
  float: right;
}

.contents-wrapper {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  margin-left: 20px;
  width: 100%;
  height: 180px;
}
</style>