<template>
  <div class="container">
    <div class="row center" style="margin-top: 40px;">
      <div class="col s12 m10 offset-m1 l8 offset-l2">
        <div class="card grey lighten-4">
          <div class="card-content">
            <span class="card-title center-align">Profile:</span>
            <h5>Username: <b>{{profile.username}}</b></h5>
            <h5>Email: <b>{{profile.email}}</b></h5>
            <button @click="modal.open()" style="margin-top: 20px;" class="btn">Удалить пользователя</button>
            <span class="card-title center-align" style="margin-top: 50px;">Change:</span>
          </div>
          <div class="row">
            <div class="col s12">
              <ul class="tabs" ref="tabs">
                <li class="tab col s4"><a href="#test1">Username</a></li>
                <li class="tab col s4"><a href="#test2">Email</a></li>
                <li class="tab col s4"><a href="#test3">Password</a></li>
              </ul>
            </div>
            <div id="test1" class="col s12">
              <h5 class="center-align">изменить имя пользователя</h5>
              <change-username-form :username="profile.username" @updateProfile="updateProfile"/>
            </div>
            <div id="test2" class="col s12">
              <h5 class="center-align">изменить email</h5>
              <change-email-fofm :email="profile.email" @updateProfile="updateProfile" />
            </div>
            <div id="test3" class="col s12">
              <h5 class="center-align">изменить пароль</h5>
              <change-password-form />
            </div>
          </div>
        </div>
      </div>
    </div>
    <!-- Modal Structure -->
    <div ref="modal" id="modal1" class="modal">
      <div class="modal-content">
        <h4  class="center-align red-text">Внимание!</h4>
        <h6 class="center-align red-text">Точно удалить пользователя?</h6>
      </div>
      <div class="modal-footer">
        <a href="#!" @click="deleteUser" class="left modal-close waves-effect waves-green btn-flat">Удалить</a>
        <a href="#!" class="modal-close waves-effect waves-green btn-flat">Отмена</a>
      </div>
    </div>
  </div>
</template>

<script>
  import ChangeUsernameForm from "../components/ChangeUsernameForm";
  import ChangeEmailFofm from "../components/ChangeEmailFofm";
  import ChangePasswordForm from "../components/ChangePasswordForm";
    export default {
        name: "Profile",
        data: () => ({
            profile: {},
            tabs: null,
            modal: null
        }),
        async mounted() {
            // eslint-disable-next-line no-undef
            this.modal = M.Modal.init(this.$refs.modal)
            // eslint-disable-next-line no-undef
            this.tabs = M.Tabs.init(this.$refs.tabs)
            this.profile = await this.$store.dispatch('getProfile')
        },
        methods: {
            async updateProfile() {
                this.profile = await this.$store.dispatch('getProfile')
            },
            async deleteUser() {
                await this.$store.dispatch('deleteUser')
            }
        },
        destroyed() {
            if (this.tabs && this.tabs.destroy) {
                this.tabs.destroy()
            }
            if (this.modal && this.modal.destroy) {
                this.modal.destroy()
            }
        },
        components: {
            ChangeUsernameForm, ChangeEmailFofm, ChangePasswordForm
        }
    }
</script>

<style scoped>

</style>
