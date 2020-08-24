<template>
  <div class="row">
    <form class="col s6" @submit.prevent="submitHandler">
      <div class="row">
        <div class="input-field col s6">
          <input v-model="email" placeholder="введите email" id="first_name" type="email" class="validate">
          <label for="first_name">email</label>
        </div>
        <div class="input-field col s6">
          <input v-model="password" placeholder="введите пароль" id="last_name" type="password" class="validate">
          <label for="last_name">password</label>
        </div>
      </div>
      <button class="btn waves-effect waves-light" type="submit" name="action">Войти
        <i class="material-icons right">send</i>
      </button>
    </form>
  </div>
</template>

<script>
    export default {
        name: "Login",
        data: () => ({
            email: '',
            password: ''
        }),
        mounted() {
            // eslint-disable-next-line no-undef
            M.updateTextFields()
            if (this.$route.query.message) {
                if (this.$route.query.message === 'you are out') {
                    this.$store.commit('logout')
                }
            }
        },
        methods: {
            async submitHandler () {
                const formData = {
                    email: this.email,
                    password: this.password
                }

                try {
                    await this.$store.dispatch('login', formData)
                } catch (e) {
                    console.log()
                }

                this.email = ''
                this.password = ''
            }
        }
    }
</script>

<style scoped>

</style>