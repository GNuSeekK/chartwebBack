<template>
  <a-layout-content class="layout-content">
    <div class="welcome-section">
      <span class="logo-text">검색할 종목을 입력하세요</span>
      <SearchBar />
    </div>
    <div class="date-range-selector">
      <label v-for="option in dateRangeOptions" :key="option.value" class="date-range-label">
        <input
            type="radio"
            name="date-range"
            :value="option.value"
            v-model="selectedRange"
        />
        {{ option.label }}
      </label>
    </div>
    <div class="content-section">
      <Chart :selected-range="selectedRange" />
    </div>
  </a-layout-content>
</template>
<script setup>
import Chart from "@/components/chart/Chart.vue";
import {ref} from "vue";
import SearchBar from "@/components/pagecomponent/SearchBar.vue";

const dateRangeOptions = [
  { value: '1w', label: '일주일' },
  { value: '1m', label: '한 달' },
  { value: '1y', label: '일 년' },
  { value: 'all', label: '전체' }
];

const selectedRange = ref('1y');


</script>

<style scoped>
.layout-content {
  padding: 50px;
  background-color: white;
}

.welcome-section {
  text-align: center;
  margin-top: 70px;
  margin-bottom: 100px;
  font-size: 30px;
  font-weight: bold;
}

.date-range-selector {
  display: flex;
  justify-content: center;
  margin-bottom: 50px;
  font-size: 20px;
}
label {
  margin-right: 20px;
  font-family: 휴먼편지체, sans-serif;
}

</style>