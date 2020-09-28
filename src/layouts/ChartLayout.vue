<template>
  <router-view/>
</template>

<script>
    import messageMixin from '../mixins/message.mixin'
    export default {
        name: "ChartLayout",
        mixins: [messageMixin]
    }
</script>

<style scoped>

</style>