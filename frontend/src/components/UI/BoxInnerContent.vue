<script setup>

</script>

<template>
  <div class="box-inner-content">
    <slot name="title"></slot>
    <div class="input-box-wrapper">
      <slot name="input"></slot>
    </div>
    <slot>
    </slot>
  </div>
</template>

<style scoped>
.box-inner-content {
  padding: 20px 28px;
}

</style>

