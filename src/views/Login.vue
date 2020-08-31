<template>
  <div class="container row">
    <div class="col s10 push-s1 m8 push-m2 l6 push-l3">
      <div class="card teal">
        <div class="card-content">
          <form @submit.prevent="submitHandler">
            <div class="input-field">
              <label for="email" class="white-text">email</label>
              <input
                      v-model.trim="email"
                      placeholder="введите email"
                      id="email"
                      type="text"
                      class="validate white-text"
                      :class="{ invalid: ($v.email.$dirty && !$v.email.required) || ($v.email.$dirty && !$v.email.email) }"
              >
              <small
                      class="helper-text invalid"
                      v-if="$v.email.$dirty && !$v.email.required"
              >
                поле email не должно быть пустым
              </small>
              <small
                      class="helper-text invalid"
                      v-if="$v.email.$dirty && !$v.email.email"
              >
                введите корректный email
              </small>
            </div>
            <div class="input-field">
              <label for="last_name" class="white-text">password</label>
              <input
                      v-model.trim="password"
                      placeholder="введите пароль"
                      id="last_name"
                      type="password"
                      class="validate white-text"
                      :class="{invalid: ($v.password.$dirty && !$v.password.required) || ($v.password.$dirty && !$v.password.minLength)}"
              >
              <small
                      class="helper-text invalid"
                      v-if="$v.password.$dirty && !$v.password.required"
              >
                введите пароль
              </small>
              <small
                      class="helper-text invalid"
                      v-if="$v.password.$dirty && !$v.password.minLength"
              >
                минимум {{ $v.password.$params.minLength.min }} символов, сечас {{ password.length }}
              </small>
            </div>
            <div class="card-action left-align">
              <router-link to="/register">зарегистрироваться</router-link>
            </div>
            <div class="card-action right-align">
              <button class="btn waves-effect waves-light" type="submit" name="action">Войти
                <i class="material-icons right">send</i>
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
    import {email, required, minLength} from 'vuelidate/lib/validators'
    import {SHA256} from '../utils/sha256'
    export default {
        name: "Login",
        data: () => ({
            email: '',
            password: ''
        }),
        validations: {
            email: {email, required},
            password: {required, minLength: minLength(6)}
        },
        mounted() {
            // eslint-disable-next-line no-undef
            M.updateTextFields()
            if (this.$route.query.message) {
                if (this.$route.query.message === 'you are out') {
                    this.$store.commit('logout')
                    this.$store.commit('setMessage', 'вы вышли из приложения')
                }
            }
        },
        methods: {
            async submitHandler() {
                if (this.$v.$invalid) {
                    this.$v.$touch()
                    return
                }
                const formData = {
                    email: this.email,
                    password: SHA256(this.password)
                }

                try {
                    await this.$store.dispatch('login', formData)
                } catch (e) {
                    console.log()
                }

                this.$v.$reset()
                this.email = ''
                this.password = ''
            }
        }
    }
</script>

<style scoped>
</style>