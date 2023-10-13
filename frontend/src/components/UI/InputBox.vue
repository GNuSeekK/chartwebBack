<script setup>

</script>

<template>
  <div class="input-box">
    <div class="icon-cell">
      <slot name="icon"></slot>
    </div>
    <slot></slot>
  </div>
</template>

<style scoped>
.input-box {
  display: table;
  table-layout: fixed;
  width: 100%;
  padding: 14px 17px 13px;
  box-sizing: border-box;
  position: relative;
  height: 100%;
  border: 1px solid #dadada;
  text-align: left;
  border-radius: 6px;
}

.icon-cell {
  display: table-cell;
  width: 50px;
  vertical-align: middle;
}
</style>