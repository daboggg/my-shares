<template>
  <div class="container">
    <div class="row center" style="margin-top: 40px;">
      <div class="col s12 m10 offset-m1 l8 offset-l2">
        <div class="card grey lighten-4">
          <div class="card-content">
            <span class="card-title center-align">Profile:</span>
            <h5>Username: <b>{{profile.username}}</b></h5>
            <h5>Email: <b>{{profile.email}}</b></h5>
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
              <ChangeUsernameForm :username="profile.username" />
            </div>
            <div id="test2" class="col s12">
              <h5 class="center-align">изменить email</h5>
            </div>
            <div id="test3" class="col s12">
              <h5 class="center-align">изменить пароль</h5>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import ChangeUsernameForm from "../components/ChangeUsernameForm";
    export default {
        name: "Profile",
        data: () => ({
            profile: {},
            tabs: null
        }),
        async mounted() {
            // eslint-disable-next-line no-undef
            this.tabs = M.Tabs.init(this.$refs.tabs)
            this.profile = await this.$store.dispatch('getProfile')
        },
        destroyed() {
            if (this.tabs && this.tabs.destroy) {
                this.tabs.destroy()
            }
        },
        components: {
            ChangeUsernameForm
        }
    }
</script>

<style scoped>

</style>
