<template>
  <div id="backdrop" @click="closeModal"></div>
    <div id="modal">
      <slot></slot>
    </div>
</template>
<script setup>
import { inject } from 'vue';
const closeModal = inject('loginModalChange');
</script>
<style scoped>

#backdrop {
  position: fixed;
  top: 30px;
  left: 0;
  width: 100%;
  height: 100vh;
  background-color: rgba(0, 0, 0, 0.5);
  z-index: 8;
}

#modal {
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  z-index: 100;
}
</style>