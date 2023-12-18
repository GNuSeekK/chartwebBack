<template>
  <div class="news-slider" @mouseover="stopAutoSlide" @mouseleave="startAutoSlide">
    <div v-for="(news, index) in props.newsList" :key="news.title" class="news-card" v-show="index === activeIndex">
      <a :href="news.link" target="_blank">
        <img :src="news.image" :alt="news.title" class="news-image" />
        <h3>{{ news.title }}</h3>
      </a>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted, watch } from 'vue';

const props = defineProps({
  newsList: Array
});
const activeIndex = ref(0);
let slideInterval = null;

const startAutoSlide = () => {
  stopAutoSlide(); // Prevent multiple intervals
  slideInterval = setInterval(() => {
    activeIndex.value = (activeIndex.value + 1) % props.newsList.length;
  }, 1000);
};

const stopAutoSlide = () => {
  clearInterval(slideInterval);
};

onMounted(startAutoSlide);
onUnmounted(stopAutoSlide);

// Watch for changes in props.newsList to restart the slider
watch(() => props.newsList, () => {
  activeIndex.value = 0; // Reset to the first news item
  startAutoSlide();
}, { deep: true });
</script>

<style scoped>
.news-slider {
  position: relative;
  width: 600px; /* Adjust as needed */
  height: 500px; /* Adjust as needed */
  overflow: hidden;
  z-index: 15;
  border: 2px solid #eee;
  border-radius: 20px;
}

.news-card {
  width: 100%;
  height: 100%;
  position: absolute;
  left: 0;
  top: 0;
  opacity: 1;
  transition: opacity 0.5s ease-in-out;
  background-color: green;
  z-index: 20;
}

.news-card a {
  text-decoration: none;
}

.news-image {
  width: 100%;
  height: 100%; /* Adjust if you want to maintain aspect ratio */
  object-fit: cover; /* Adjust or remove if needed */
}

.news-card h3 {
  position: absolute;
  bottom: 0;
  left: 0;
  width: 100%;
  background-color: rgba(0, 0, 0, 0.7);
  color: #fff;
  padding: 10px;
  margin: 0;
}

/* Show only the active news card */
.news-card.v-show {
  opacity: 1;
  position: relative;
}
</style>