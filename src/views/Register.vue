<template>
  <div class="container row">
    <div class="col s10 push-s1 m8 push-m2 l6 push-l3">
      <div class="card teal">
        <div class="card-content">
          <form @submit.prevent="submitHandler">
            <div class="input-field">
              <label for="username" class="white-text">username</label>
              <input
                      v-model.trim="username"
                      placeholder="введите свое имя"
                      id="username"
                      type="text"
                      class="validate white-text"
                      :class="{ invalid: ($v.username.$dirty && !$v.username.required) || ($v.username.$dirty && !$v.username.minLength) }"
              >
              <small
                      class="helper-text invalid"
                      v-if="$v.username.$dirty && !$v.username.required"
              >
                введите свое имя
              </small>
              <small
                      class="helper-text invalid"
                      v-if="$v.username.$dirty && !$v.username.minLength"
              >
                минимум {{ $v.username.$params.minLength.min }} символов, сечас {{ username.length }}
              </small>
            </div>
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
              <router-link to="/login">войти</router-link>
            </div>
            <div class="card-action right-align">
              <button class="btn waves-effect waves-light" type="submit" name="action">зарегистрироваться
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
    import {SHA256} from '../utils/sha256'
    import {email, required, minLength} from 'vuelidate/lib/validators'

    export default {
        name: "Register",
        data: () => ({
            username: '',
            email: '',
            password: ''
        }),
        validations: {
            username: {required, minLength: minLength(3)},
            email: {email, required},
            password: {required, minLength: minLength(6)}
        },
        mounted() {
            // eslint-disable-next-line no-undef
            M.updateTextFields()
        },
        methods: {
            async submitHandler() {
                if (this.$v.$invalid) {
                    this.$v.$touch()
                    return
                }
                const formData = {
                    username: this.username,
                    email: this.email,
                    password: SHA256(this.password)
                }

                try {
                    const res = await this.$store.dispatch('register', formData)
                    console.log(res)
                } catch (e) {
                    console.log()
                }

                this.username = ''
                this.email = ''
                this.password = ''
                this.$v.$reset()
            }
        }
    }
</script>

<style scoped>

</style>