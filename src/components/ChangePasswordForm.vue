<template>
  <div class="container" style="margin-top: 40px;">
    <div class="row">
      <form class="col s12" @submit.prevent="changePassword">
        <div class="row valign-wrapper">
          <div class="input-field col s6">
            <label for="oldPassword">enter old password</label>
            <input
                    v-model.trim="oldPassword"
                    placeholder="old password"
                    id="oldPassword"
                    type="password"
                    class="validate"
            >
            <!--            <small-->
            <!--                    class="helper-text invalid"-->
            <!--                    v-if="$v.newUsername.$dirty && !$v.newUsername.required"-->
            <!--            >-->
            <!--              введите свое имя-->
            <!--            </small>-->
            <!--            <small-->
            <!--                    class="helper-text invalid"-->
            <!--                    v-if="$v.newUsername.$dirty && !$v.newUsername.minLength"-->
            <!--            >-->
            <!--              минимум {{ $v.newUsername.$params.minLength.min }} символов, сечас {{ newUsername.length }}-->
            <!--            </small>-->
          </div>
          <h5 style="color: green" class="col s6">{{isAcceptedOldPassword?'OK':''}}</h5>
        </div>
        <div class="row valign-wrapper">
          <div class="input-field col s6">
            <label for="newPassword">enter new password</label>
            <input
                    v-model.trim="newPassword"
                    placeholder="new password"
                    id="newPassword"
                    type="password"
                    :disabled="!isAcceptedOldPassword"
                    class="validate"
                    :class="{invalid: ($v.newPassword.$dirty && !$v.newPassword.required) || ($v.newPassword.$dirty && !$v.newPassword.minLength) || (newPassword === oldPassword && oldPassword)}"
            >
            <small
                    class="helper-text invalid"
                    v-if="$v.newPassword.$dirty && !$v.newPassword.required"
            >
              введите пароль
            </small>
            <small
                    class="helper-text invalid"
                    v-if="$v.newPassword.$dirty && !$v.newPassword.minLength"
            >
              минимум {{ $v.newPassword.$params.minLength.min }} символов, сечас {{ newPassword.length }}
            </small>
            <small
                    class="helper-text invalid"
                    v-if="newPassword === oldPassword && oldPassword"
            >
              этот пароль уже установлен
            </small>
          </div>
          <div class="col s6">
            <button :disabled="!isAcceptedOldPassword" class="btn waves-effect waves-light" type="submit" name="action">изменить
              <i class="material-icons right">autorenew</i>
            </button>
          </div>
        </div>
      </form>
    </div>
  </div>
</template>

<script>
    import {SHA256} from '../utils/sha256'
    import {required, minLength} from 'vuelidate/lib/validators'
    export default {
        name: "ChangePasswordForm",
        data: () => ({
            oldPassword: '',
            isAcceptedOldPassword: false,
            newPassword: ''
        }),
        validations: {
            newPassword: {required, minLength: minLength(6)}
        },
        watch: {
            async oldPassword(psw) {
                if (psw) {
                    psw = SHA256(psw)
                    this.isAcceptedOldPassword = (await this.$store.dispatch('checkOldPassword', psw)) === 'true'
                }
            }
        },
        mounted() {
            // eslint-disable-next-line no-undef
            M.updateTextFields()
        },
        methods: {
            async changePassword() {
                if (this.oldPassword === this.newPassword) return
                if (this.$v.$invalid) {
                    this.$v.$touch()
                    return
                }

                await this.$store.dispatch('changePassword', SHA256(this.newPassword))

                this.oldPassword = ''
                this.newPassword = ''
                this.isAcceptedOldPassword = false
            }
        }
    }
</script>

<style scoped>

</style>
