<template>
  <div class="container" style="margin-top: 40px;">
    <div class="row">
      <form class="col s12" @submit.prevent="changeEmail">
        <div class="row valign-wrapper">
          <div class="input-field col s6">
            <label for="email">email</label>
            <input
                    v-model.trim="newEmail"
                    placeholder="email"
                    id="email"
                    type="text"
                    class="validate"
                    :class="{ invalid: ($v.newEmail.$dirty && !$v.newEmail.required) || ($v.newEmail.$dirty && !$v.newEmail.email) }"
            >
            <small
                    class="helper-text invalid"
                    v-if="$v.newEmail.$dirty && !$v.newEmail.required"
            >
              введите email
            </small>
            <small
                    class="helper-text invalid"
                    v-if="$v.newEmail.$dirty && !$v.newEmail.email"
            >
              введите корректный email
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
    import {email, required} from 'vuelidate/lib/validators'
    export default {
        name: "ChangeEmailFofm",
        props: ['email'],
        data: () => ({
            newEmail: '',
            loading: false
        }),
        validations: {
            newEmail: {email, required},
        },
        watch: {
            email(email) {
                this.loading = false
                this.newEmail = email
            }
        },
        mounted() {
            // eslint-disable-next-line no-undef
            M.updateTextFields()
        },
        methods: {
            async changeEmail() {
                if (this.email === this.newEmail) return
                if (this.$v.$invalid) {
                    this.$v.$touch()
                    return
                }
                this.loading = true
                await this.$store.dispatch('changeEmail', this.newEmail)
                this.$emit('updateProfile')
            }
        }
    }
</script>

<style scoped>

</style>
