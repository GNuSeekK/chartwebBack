<script setup>

</script>

<template>
  <div class="button">
    <div class="text">
      <slot></slot>
    </div>
  </div>
</template>

<style scoped>
.button {
  background-color: #0d3181;
  color: #dadee0;
  display: flex;
  justify-content: left;
}

.button:focus,
.button:hover {
  background-color: #1f4694;
}

.text {
  font-size: 1.25rem;
  font-weight: 600;
  padding: 0.5rem 1rem;
  text-align: right;
  flex-basis: 80%;
}
</style>