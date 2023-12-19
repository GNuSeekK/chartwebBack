<template>
  <a-empty
      :style="{
        marginTop: '100px',
        width: '100%',
        left: '50%',
        backgroundColor: 'white',
      }"
      :image-style="{
        height: '400px',
        color: '#166bdf',
      }"
  >
    <template #description>
      <div>
        <slot name="description"></slot>
            준비중인 <span>서비스</span> 입니다.
      </div>
    </template>
  </a-empty>
</template>

<script setup>
</script>

<style scoped>
div {
  text-align: center;
  font-size: 30px;
  font-weight: bold;
  font-family: 'Noto Sans KR', sans-serif;
}

span {
  font-family: 휴먼편지체, sans-serif;
  color: #ee2424;
}
</style>