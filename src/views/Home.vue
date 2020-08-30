<template>
  <div>
    <p>HOME</p>
    <router-link
            class="btn waves-effect waves-light"
            type="button"
            name="action"
            to="/login?message=you are out"
    >Выйти
      <i class="material-icons right">send</i>
    </router-link>
  </div>
</template>

<script>
    export default {
        name: "Home"
    }
</script>

<style scoped>

</style>