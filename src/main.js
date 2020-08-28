import Vue from 'vue'
import App from './App.vue'
import VueResource from 'vue-resource'
import Vuelidate from "vuelidate";
import router from './router'
import store from './store'
import 'materialize-css/dist/js/materialize.min'

Vue.use(VueResource)
Vue.use(Vuelidate)

Vue.config.productionTip = false

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
