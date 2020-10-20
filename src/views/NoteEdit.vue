<template>
  <div class="container">
    <h5 class="center-align">Изменить записку:</h5>
    <div v-if="template" ref="template" style="opacity: 0%"></div>
    <loader v-if="loading"/>
    <form v-else @submit.prevent="editNote">
      <div style="margin-top: 30px" class="row z-depth-2">
        <div class="input-field col s12">
          <i class="material-icons prefix">title</i>
          <label class="active" for="note_title">Тема записки</label>
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
          <label class="active" for="note_text">Текст записки</label>
          <textarea
                  :key="style.height"
                  :style="style"
                  ref="txtarea"
                  v-model="textNote"
                  id="note_text"
                  class="materialize-textarea asd"
          ></textarea>
        </div>
      </div>
      <div style="margin-top: 30px">
        <button @click="$router.push('/notebook')" class="btn waves-effect waves-light left" type="button">
          <i class="material-icons left">close</i>
          отмена
        </button>
        <button  :disabled="isButtonDisabled" class="btn waves-effect waves-light right" type="submit" name="action">изменить
          <i class="material-icons right">create</i>
        </button>
      </div>
    </form>
  </div>
</template>

<script>
    import {required, minLength} from 'vuelidate/lib/validators'

    export default {
        name: "NoteEdit",
        data: () => ({
            style: {
                height: '1px'
            },
            titleNote: '',
            textNote: '',
            note: [],
            loading: true,
            template: true
        }),
        validations: {
            titleNote: {required, minLength: minLength(3)},
        },
        async mounted() {
            // eslint-disable-next-line no-undef
            M.updateTextFields()
            this.note = await this.$store.dispatch('getNote', this.$route.params.id)
            this.titleNote = this.note.title
            this.textNote = this.note.text

            // вычисляем и устанавливаем высоту textarea
            const d = this.$refs.template
            d.innerHTML = this.textNote
            this.style.height = (this.$refs.template.clientHeight * 1.7) + 'px'

            this.template = false
            this.loading = false
        },
        computed:{
            isButtonDisabled() {
                if (this.titleNote === this.note.title) {
                    if (this.textNote === this.note.text) {
                        return true
                    } else return false
                } else return false
            }
        },
        methods: {
            editNote() {
                if (this.$v.$invalid) {
                    this.$v.$touch();
                    return;
                }
                const formData = {
                    title: this.titleNote,
                    text: this.textNote || 'no content',
                    id: this.note.id
                }
                this.$store.dispatch('editNote', formData)
            }
        }
    }
</script>

<style scoped>

</style>
