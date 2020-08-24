<template>
  <div id="app">
    <h1>HEADER</h1>
    <router-link to="/login">login</router-link>
    <br>
    <router-link to="/register">register</router-link>
    <br>
    <router-link to="/login?message=you are out">logout</router-link>
    <router-view/>
  </div>
</template>
<script>
  export default {
  }
</script>
<style lang="scss">
  @import "~materialize-css/dist/css/materialize.min.css";
  @import "assets/index.css";
</style>
