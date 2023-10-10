<template>
  <p class="nav-link text-white">{{text}}</p>
</template>
<script>
export default {
  name: 'p-link-button',
  props: ['text']
}
</script>
<style scoped>
.ul-layout > li {
  list-style: none;
}

.div-flex a {
  text-decoration: none;
  color: white;
}

p:hover,
p:active {
  border-color: #30bdad;
  background-color: #30bdad;
}
</style>