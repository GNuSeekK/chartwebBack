<template>
  <div id="backdrop">
    <div id="modal">
      <slot></slot>
    </div>
  </div>
</template>
<script>
export default {
  name: 'CommonModal',
}
</script>
<style scoped>

#backdrop {
  position: fixed;
  top: 30px;
  left: 0;
  width: 100%;
  height: 100vh;
  background-color: rgba(0, 0, 0, 0.5);
  z-index: 100;

}

#modal {
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
}
</style>