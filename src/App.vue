<template>
  <div id="app">
<!--    <h1>HEADER</h1>-->
<!--    <router-link to="/login">login</router-link>-->
<!--    <br>-->
<!--    <router-link to="/register">register</router-link>-->
<!--    <br>-->
<!--    <router-link to="/login?message=you are out">logout</router-link>-->
    <component :is="layout">
      <router-view/>
    </component>
<!--    <empty-layout/>-->
  </div>
</template>
<script>
  import EmptyLayout from './layouts/EmptyLayout'
  import MainLayout from './layouts/MainLayout'
  export default {
      computed: {
          layout () {
              return (this.$route.meta.layout || 'empty') + '-layout'
          }
      },
      components: {
          EmptyLayout, MainLayout
      }
  }
</script>
<style lang="scss">
  @import "~materialize-css/dist/css/materialize.min.css";
  @import "assets/index.css";
</style>
