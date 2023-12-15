<template>
  <div class="news-slider" @mouseover="stopAutoSlide" @mouseleave="startAutoSlide">
    <transition-group tag="div" name="slider" class="slider-wrapper">
      <!-- Use v-show to keep all elements in the DOM -->
      <div
          v-for="(news, index) in newsList"
          :key="news.title"
          class="news-card"
          v-show="index === activeIndex"
      >
        <h3>{{ news.title }}</h3>
        <img :src="news.image" :alt="news.title" class="news-image"/>
        <div class="overlay">
          <div class="arrow left-arrow" @click="prevNews">＜</div>
          <div class="arrow right-arrow" @click="nextNews">＞</div>
        </div>
      </div>
    </transition-group>
  </div>
</template>

<script setup>
import {onMounted, onUnmounted, ref} from 'vue';

const props = defineProps({
  newsList: {
    type: Array,
    required: true
  }
})
const newsList = props.newsList
const activeIndex = ref(0);
const transitionName = ref('slide-left');
let slideInterval;

const startAutoSlide = () => {
  slideInterval = setInterval(() => {
    activeIndex.value = (activeIndex.value + 1) % newsList.length;
  }, 2000);
};

const stopAutoSlide = () => {
  clearInterval(slideInterval);
};

const prevNews = () => {
  stopAutoSlide();
  transitionName.value = 'slide-right';
  activeIndex.value = (activeIndex.value - 1 + newsList.length) % newsList.length;
  startAutoSlide();
};

const nextNews = () => {
  stopAutoSlide();
  transitionName.value = 'slide-left';
  activeIndex.value = (activeIndex.value + 1) % newsList.length;
  startAutoSlide();
};

onMounted(() => {
  startAutoSlide();
});

onUnmounted(() => {
  stopAutoSlide();
});
</script>

<style scoped>
.news-slider {
  position: relative;
  margin-right: 100px;
  width: 500px;
}

.news-card {
  text-align: center;
  position: relative;
}

.news-image {
  width: 100%;
  aspect-ratio: 1 / 1;
  object-fit: cover;
  border-radius: 10px;

}

.overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.arrow {
  background-color: rgba(0, 0, 0, 0.5); /* 반투명 검정색 오버레이 */
  color: white;
  padding: 10px;
  cursor: pointer;
}

.left-arrow {
  /* 화살표 왼쪽 오버레이 */
}

.right-arrow {
  /* 화살표 오른쪽 오버레이 */
}

h3 {
  position: absolute;
  bottom: 10px;
  left: 50%;
  transform: translateX(-50%);
  background-color: rgba(0, 0, 0, 0.5);
  color: white;
  padding: 5px 10px;
  border-radius: 5px;
}
</style>