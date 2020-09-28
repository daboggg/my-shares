<template>
  <div id="app">
    <component :is="layout">
      <router-view/>
    </component>
<!--    <empty-layout/>-->
  </div>
</template>
<script>
  import EmptyLayout from './layouts/EmptyLayout'
  import MainLayout from './layouts/MainLayout'
  import ChartLayout from "./layouts/ChartLayout";
  export default {
      computed: {
          layout () {
              return (this.$route.meta.layout || 'empty') + '-layout'
          }
      },
      components: {
          EmptyLayout, MainLayout, ChartLayout
      }
  }
</script>
<style lang="scss">
  @import "~materialize-css/dist/css/materialize.min.css";
  @import "assets/index.css";
</style>
