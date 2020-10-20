<template>
  <div class="container">
    <div style="margin-top: 30px" class="row">
      <loader v-if="loading"/>
      <div v-else class="col s12 m10 offset-m1 l8 offset-l2">
        <div class="card grey lighten-4">
          <div class="card-content">
            <span class="card-title center-align"><b><h4>{{note.title}}</h4></b></span>
            <div class="divider"></div>
            <h5 style="margin: 35px auto 15px auto">{{note.text}}</h5>
          </div>
          <div class="card-action" style="padding: 0; display: flex; justify-content: space-between">
            <button style="margin: 10px auto;" class="btn-flat red-text" type="button" @click="$router.push('/notebook')">назад</button>
            <button style="margin: 10px auto;" class="btn-flat red-text" type="button" @click="deleteNone(note.id)">удалить</button>
            <button style="margin: 10px auto;" class="btn-flat red-text" type="button" @click="$router.push('/noteEdit/' + note.id)">изменить</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
    export default {
        name: "Note",
        data: () => ({
            note: [],
            loading: true
        }),
        async mounted() {
            this.note = await this.$store.dispatch('getNote', this.$route.params.id)
            this.loading = false
        },
        methods: {
            async deleteNone(id) {
                await this.$store.dispatch('deleteNote',id)
            }
        }
    }
</script>

<style scoped>

</style>
