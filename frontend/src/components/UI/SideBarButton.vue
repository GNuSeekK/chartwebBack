<script setup>

</script>

<template>
<div class="button">
  <div class="icon">
    <slot name="default"></slot>
  </div>
  <div class="text">
    <slot name="text"></slot>
  </div>
</div>
</template>

<style scoped>

.button {
  background-color: #2090a9;
  color: #dadee0;
  display: flex;
  justify-content: left;
}

.button:focus,
.button:hover {
  background-color: #30b2cc;
}

.icon {
  font-size: 1.25rem;
  font-weight: 600;
  padding: 0.5rem 1rem;
  text-align: left;
  flex-basis: 20%;
}
.text {
  font-size: 1.25rem;
  font-weight: 600;
  padding: 0.5rem 1rem;
  text-align: left;
  flex-basis: 80%;
}
</style>