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
            <button class="btn waves-effect waves-light" type="submit" name="action">изменить
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
            newUsername: ''
        }),
        validations: {
            newUsername: {required, minLength: minLength(3)},
        },
        watch: {
            username(username) {
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
                await this.$store.dispatch('changeUsername', this.newUsername)
            }
        }
    }
</script>

<style scoped>

</style>
