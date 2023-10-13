<script setup>

</script>

<template>
  <div class="content-box">
    <div class="inner">
      <slot></slot>
    </div>
  </div>
</template>

<style scoped>
.content-box {
  background-color: #fff;
  position: relative;
  border-radius: 8px;
  box-shadow: 0 0 2px 3px rgb(230, 230, 230, 1);
  box-sizing: border-box;
  margin: 10px;
}

.inner {
  padding: 21px 20px 18px;
  //line-height: 20px;
  text-align: center;
}

</style>