<template>
  <div class="chart-wrapper">
    <div class="chart-container" ref="chartContainer">
    <Line :data="chartData" :options="chartOptions" />
    </div>
  </div>
</template>

<script setup>
import {Line} from 'vue-chartjs'
import {Chart, registerables} from 'chart.js';
import {computed, inject, onMounted, watch} from 'vue';
import {useStockStore} from '@/store/stock';

Chart.register(...registerables);

const props = defineProps({
  selectedRange: String,
});

let startDate = "20210201";
let endDate = "20210630";
const rangeChange = async () => {
  const newVal = props.selectedRange;
  let today = new Date();
  endDate = today.toLocaleDateString().replaceAll('.', '').replaceAll(' ', '');
  if (newVal === '1w') {
    startDate = new Date(today.setDate(today.getDate() - 7)).toLocaleDateString().replaceAll('.', '').replaceAll(' ', '');
  } else if (newVal === '1m') {
    startDate = new Date(today.setMonth(today.getMonth() - 1)).toLocaleDateString().replaceAll('.', '').replaceAll(' ', '');
  } else if (newVal === '1y') {
    startDate = new Date(today.setFullYear(today.getFullYear() - 1)).toLocaleDateString().replaceAll('.', '').replaceAll(' ', '');
  } else if (newVal === 'all') {
    startDate = new Date(today.setFullYear(today.getFullYear() - 20)).toLocaleDateString().replaceAll('.', '').replaceAll(' ', '');
  }
  await loadChartData();
}
const nowStockInfo = inject('nowStockInfo')
const stockStore = useStockStore();
const loadChartData = async () => {
  await stockStore.loadStockPrices(nowStockInfo.code, startDate, endDate)
};

const maxDataLimit = 500;

const labels = computed(() => {
  const stockPriceList = stockStore.getStockPriceList();
  if (stockPriceList.length > maxDataLimit) {
    const interval = Math.floor(stockPriceList.length / maxDataLimit);
    return stockPriceList.filter((_, index) => index % interval === 0).map((stockPrice) => stockPrice.date);
  }
  return stockStore.getStockPriceList().map((stockPrice) => stockPrice.date);
});

const datas = computed(() => {
  const stockPriceList = stockStore.getStockPriceList();
  if (stockPriceList.length > maxDataLimit) {
    const interval = Math.floor(stockPriceList.length / maxDataLimit);
    return stockPriceList.filter((_, index) => index % interval === 0).map((stockPrice) => stockPrice.close);
  }
  return stockStore.getStockPriceList().map((stockPrice) => stockPrice.close);
});

const chartData = computed(() => {
  return {
    labels: labels.value,
    datasets: [
      {
        label: '종가',
        data: datas.value,
        borderColor: '#fc5151',
        backgroundColor: 'rgba(252,81,81,0.3)',
        fill: true,
        yAxisID: 'y1',
      }
    ],
  };
});

const chartOptions = computed(() => {
  return {
    responsive: true,
    maintainAspectRatio: false,
    scales: {
      x: {
        grid: {
          display: false,
        },
        ticks: {
          maxTicksLimit: 10,
        },
      },
      y1: {
        type: 'linear',
        display: true,
        position: 'left',
        title: {
          display: true,
          text: '종가',
        },
        grid: {
          display: false,
        },
      }
    },
    interaction: {
      mode: 'index',
      axis: 'x',
      intersect: false,
    },
    plugins: {
      title: {
        display: true,
        text: `${nowStockInfo.name}`,
        font: {
          size: 30,
        }
      },
    },
  };
});

onMounted(loadChartData);
watch( () => [nowStockInfo.code], loadChartData);
watch( () => props.selectedRange, rangeChange);

// 드래그 이벤트 핸들러 추가
</script>

<style scoped>
.chart-container {
  position: absolute;
  width: 100%;
  height: 100%;
  padding-left: 10%;
  padding-right: 10%;
}

.chart-wrapper {
  margin-top: 50px;
  width: 100%;
  position: relative;
  padding-bottom: 30%;
  background-color: white;
}
</style>