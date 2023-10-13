<script setup>

</script>

<template>
  <div class="mini-round-div">
    <slot></slot>
  </div>
</template>

<style scoped>
.mini-round-div {
  border: 1px solid #ebebeb;
  background-color: #fff;
  color: #606060;
  display: inline-block;
  min-width: 75px;
  height: 26px;
  margin-top: 7px;
  padding: 0 15px 0 15px;
  border-radius: 15px;
  box-sizing: border-box;
  font-size: 0.9rem;
  line-height: 25px;
  vertical-align: top;
  text-decoration: none;
  box-shadow: 0 1px 2px 0 rgba(0,0,0,.2);
}


* {
  font-size: 1.2rem;
}
</style>