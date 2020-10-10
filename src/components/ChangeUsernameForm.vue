<template>
  <div class="container" style="margin-top: 40px;">
    <div class="row">
      <form class="col s12" @submit.prevent="changeUsername">
        <div class="row valign-wrapper">
          <div class="input-field col s6">
            <label for="username">username</label>
            <input
                    v-model.trim="newUsername"
                    placeholder="username"
                    id="username"
                    type="text"
                    class="validate"
                    :class="{ invalid: ($v.newUsername.$dirty && !$v.newUsername.required) || ($v.newUsername.$dirty && !$v.newUsername.minLength) }"
            >
            <small
                    class="helper-text invalid"
                    v-if="$v.newUsername.$dirty && !$v.newUsername.required"
            >
              введите свое имя
            </small>
            <small
                    class="helper-text invalid"
                    v-if="$v.newUsername.$dirty && !$v.newUsername.minLength"
            >
              минимум {{ $v.newUsername.$params.minLength.min }} символов, сечас {{ newUsername.length }}
            </small>
          </div>
          <div class="col s6">
            <loader v-if="loading"/>
            <button v-else class="btn waves-effect waves-light" type="submit" name="action">изменить
              <i class="material-icons right">autorenew</i>
            </button>
          </div>
        </div>
      </form>
    </div>
  </div>

</template>

<script>
    import {required, minLength} from 'vuelidate/lib/validators'
    export default {
        name: "ChangeUsernameForm",
        props: ['username'],
        data: () => ({
            newUsername: '',
            loading: false
        }),
        validations: {
            newUsername: {required, minLength: minLength(3)},
        },
        watch: {
            username(username) {
                this.loading = false
                this.newUsername = username
            }
        },
        mounted() {
            // eslint-disable-next-line no-undef
            M.updateTextFields()
        },
        methods: {
            async changeUsername() {
                if (this.username === this.newUsername) return
                if (this.$v.$invalid) {
                    this.$v.$touch()
                    return
                }
                this.loading = true
                await this.$store.dispatch('changeUsername', this.newUsername)
                this.$emit('updateProfile')
            }
        }
    }
</script>

<style scoped>

</style>
