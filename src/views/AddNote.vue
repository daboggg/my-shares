<template>
  <div class="container">
    <h5 class="center-align">Создать записку:</h5>
    <form @submit.prevent="createNote">
      <div style="margin-top: 30px" class="row z-depth-2">
        <div class="input-field col s12">
          <i class="material-icons prefix">title</i>
          <label for="note_title">Тема записки</label>
          <input
                  v-model="titleNote"
                  id="note_title"
                  type="text"
                  class="validate"
                  :class="{ invalid: ($v.titleNote.$dirty && !$v.titleNote.required) || ($v.titleNote.$dirty && !$v.titleNote.minLength) }"
          >
          <small
                  class="helper-text invalid"
                  v-if="$v.titleNote.$dirty && !$v.titleNote.required"
          >
            введите тему записки
          </small>
          <small
                  class="helper-text invalid"
                  v-if="$v.titleNote.$dirty && !$v.titleNote.minLength"
          >
            минимум {{ $v.titleNote.$params.minLength.min }} символов, сечас {{ titleNote.length }}
          </small>
        </div>
      </div>
      <div style="margin-top: 30px" class="row z-depth-2">
        <div class="input-field col s12">
          <i class="material-icons prefix">textsms</i>
          <label for="note_text">Текст записки</label>
          <textarea
                  v-model="textNote"
                  id="note_text"
                  class="materialize-textarea"
          ></textarea>
        </div>
      </div>
      <div style="margin-top: 30px">
        <button @click="$router.push('/notebook')" class="btn waves-effect waves-light left" type="button">
          <i class="material-icons left">close</i>
          отмена
        </button>
        <button class="btn waves-effect waves-light right" type="submit" name="action">создать
          <i class="material-icons right">create</i>
        </button>
      </div>
    </form>
  </div>
</template>

<script>
    import {required, minLength} from 'vuelidate/lib/validators'
    export default {
        name: "AddNote",
        data: () =>({
            titleNote: '',
            textNote: ''
        }),
        validations: {
            titleNote: {required, minLength: minLength(3)},
        },
        methods: {
            async createNote() {
                if (this.$v.$invalid) {
                    this.$v.$touch()
                    return
                }
                const formData = {
                    title: this.titleNote,
                    text: this.textNote || 'no content'
                }
                await this.$store.dispatch('createNote',formData)
                this.$router.push("/notebook")
            }
        }
    }
</script>

<style scoped>

</style>
