<template>
  <div class="container">
    <div style="margin-top: 30px;" class="row">
      <loader v-if="loading"/>
      <p v-else-if="!notes.length" class="center-align">Записей нет</p>
      <div v-else class="col s6 m4 l3" v-for="(note, i) in notes" :key="i">
        <div class="card" @click="$router.push('/note/'+ note.id)">
          <div style="padding: 10px;" class="card-content">
            <span class="card-title truncate"><h6><b>{{note.title}}</b></h6></span>
            <p class="truncate"><i>{{note.text}}</i></p>
          </div>
<!--          <div class="card-action">-->
<!--            <a href="#">This is a link</a>-->
<!--            <a href="#">This is a link</a>-->
<!--          </div>-->
        </div>
      </div>
    </div>
    <router-link to="/addNote" class="right btn-floating btn-large waves-effect waves-light teal"><i class="material-icons">add</i></router-link>
  </div>
</template>

<script>
    export default {
        name: "Notebook",
        data: () => ({
            notes: [],
            loading: true
        }),
        async mounted() {
            this.notes = await this.$store.dispatch('getNotes')
            this.loading = false
        }
    }
</script>

<style scoped>

</style>
