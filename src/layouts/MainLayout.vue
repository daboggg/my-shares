<template>
  <div>
    <nav class="teal" ref="nv">
      <a href="#" data-target="mobile-demo" class="sidenav-trigger show-on-small"><i class="material-icons">menu</i></a>
      <ul class="right">
        <li><a
                class="dropdown-trigger"
                href="#"
                data-target="dropdown"
                ref="dropdown"
        >{{username}}<i class="material-icons right">arrow_drop_down</i></a>
        </li>
        <li>
          <ul id="dropdown" class="dropdown-content">
            <li><router-link to="/profile">профиль</router-link></li>
            <li class="divider"></li>
            <li><router-link to="/login?message=you are out">выйти</router-link></li>
          </ul>
        </li>
      </ul>
      <ul class="right hide-on-small-only">
        <li v-for="item in menuItems" :key="item.name"><router-link :to="item.path">{{item.name}}</router-link></li>
      </ul>
      <p class="center">- {{view}} -</p>
    </nav>

    <ul ref="sidenav" class="sidenav" id="mobile-demo">
      <li @click="sidebar.close()" v-for="item in menuItems" :key="item.name"><router-link :to="item.path">{{item.name}}</router-link></li>
    </ul>

    <router-view/>

  </div>

</template>

<script>
  import messageMixin from '../mixins/message.mixin'
    export default {
        name: "MainLayout",
        data: () => ({
            menuItems: [
                {name: 'Home', path: '/'},
                {name: 'Add', path: '/add'},
                {name: 'Notebook', path: '/notebook'},
                {name: 'Alert', path: '/alert'}
            ],
            dropdown: null,
            sidebar: null
        }),
        mixins: [messageMixin],
        mounted() {
            const elems = this.$refs.sidenav
            // eslint-disable-next-line no-undef
            this.sidebar = M.Sidenav.init(elems)
            // eslint-disable-next-line no-undef
            this.dropdown = M.Dropdown.init(this.$refs.dropdown)
        },
        computed: {
            username() {
                return this.$store.getters.getUsername
            },
            view() {
                return this.$route.name.toUpperCase()
            }
        },
        beforeDestroy() {
            if (this.sidebar && this.sidebar.destroy) {
                this.sidebar.destroy()
            }
            if (this.dropdown && this.dropdown.destroy) {
                this.dropdown.destroy()
            }
        }
    }
</script>

<style scoped>

</style>
