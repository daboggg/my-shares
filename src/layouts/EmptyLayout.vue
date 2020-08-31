<template>
    <div class="grey darken-1 valign-wrapper" style="height: 100vh">
      <router-view/>
    </div>
</template>

<script>
  import messageMixin from '../mixins/message.mixin'
    export default {
        name: "EmptyLayout",
        mixins: [messageMixin]
    }
</script>

<style scoped>

</style>