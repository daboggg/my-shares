<template>
  <div class="row">
    <form class="col s12" @submit.prevent="submitHandler">
      <div class="row">
        <div class="input-field col s4">
          <input v-model="username" placeholder="введите ваше имя" id="username" type="text" class="validate">
          <label for="username">username</label>
        </div>
        <div class="input-field col s4">
          <input v-model="email" placeholder="введите email" id="email" type="email" class="validate">
          <label for="email">email</label>
        </div>
        <div class="input-field col s4">
          <input v-model="password" placeholder="введите пароль" id="password" type="password" class="validate">
          <label for="password">password</label>
        </div>
      </div>
      <button class="btn waves-effect waves-light" type="submit" name="action">Зарегистрироваться
        <i class="material-icons right">send</i>
      </button>
    </form>
  </div>
</template>

<script>
    export default {
        name: "Register",
        data: () => ({
            username: '',
            email: '',
            password: ''
        }),
        mounted() {
            // eslint-disable-next-line no-undef
            M.updateTextFields()
        },
        methods: {
            async submitHandler () {
                const formData = {
                    username: this.username,
                    email: this.email,
                    password: this.password
                }

                try {
                    const res = await this.$store.dispatch('register', formData)
                    console.log(res)
                } catch (e){
                    console.log()
                }

                this.username = ''
                this.email = ''
                this.password = ''
            }
        }
    }
</script>

<style scoped>

</style>