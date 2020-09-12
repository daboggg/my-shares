<template>
  <div class="app-loader">
    <div class="preloader-wrapper active">
      <div class="spinner-layer spinner-red-only">
        <div class="circle-clipper left">
          <div class="circle"></div>
        </div>
        <div class="gap-patch">
          <div class="circle"></div>
        </div>
        <div class="circle-clipper right">
          <div class="circle"></div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
    export default {
        name: "Loader"
    }
</script>

<style scoped>
  .app-loader {
    display: flex;
    width: 100%;
    justify-content: center;
    padding-top: 2rem
  }
</style>