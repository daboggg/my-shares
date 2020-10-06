<template>
  <div class="container">
    <div class="row" v-show="!ticker">
      <form class="col s12" @submit.prevent="searchTicker">
        <div class="row valign-wrapper">
          <div class="switch col s5 m4 l3">
            <label>
              Ticker
              <input type="checkbox" v-model="searchByName">
              <span class="lever"></span>
              Name
            </label>
          </div>
          <div class="input-field col s5 m6 l7">
            <input
                    v-model.trim="searchString"
                    placeholder="enter ticker or name"
                    id="first_name"
                    type="text"
                    class="validate"
                    :class="{invalid: $v.searchString.$dirty && !$v.searchString.required}"
            >
            <small
                    class="helper-text invalid"
                    v-if="$v.searchString.$dirty && !$v.searchString.required"
            >
              введите тикер или название
            </small>
          </div>
          <button class="btn-small waves-effect waves-light col s2" type="submit" name="action">
            поиск
            <i class="material-icons right">send</i>
          </button>
        </div>
      </form>
    </div>
    <loader v-if="loading" v-show="!ticker"/>
    <div class="row" v-show="!ticker">
      <div class="col s12">
        <div class="card horizontal" v-for="(v ,k, i) in instruments" :key="i">
          <div class="card-stacked">
            <div class="card-content">
              <h5>{{v.description}}</h5>
              <p>{{v.assetType}}</p>
            </div>
            <div class="card-action">
              <a href="#" @click.prevent="ticker = k">Ticker: {{k}}</a>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="row" v-show="ticker">
      <div class="col s12">
        <h5 class="center-align">Ticker: {{ticker}}</h5>
      </div>
    </div>
    <div class="row" v-show="ticker">
      <div class="col s12">
        <form action="#">
          <p v-for="(type, i) in typeAlerts" :key="i">
            <label>
              <input name="group1" type="radio" :value="type" v-model="action"/>
              <span>{{type}}</span>
            </label>
          </p>
        </form>
      </div>
    </div>
    <div class="row valign-wrapper" v-show="ticker">
      <div class="col s4 offset-s1">
        <input type="text" v-model="value"/>
      </div>
      <div class="col s4 offset-s2">
        <button class="btn-small waves-effect waves-light" type="button" @click="addAlert">
          добавить
          <i class="material-icons right">send</i>
        </button>
      </div>
    </div>
    <loader v-if="loading" v-show="ticker"/>
    <div class="row" v-if="alerts.length">
      <div class="col s12">
        <table class="centered striped">
          <thead>
          <tr>
            <th>Ticker</th>
            <th>Action</th>
            <th>Value</th>
            <th>Delete</th>
          </tr>
          </thead>

          <tbody>
          <tr v-for="(alert, i) in alerts" :key="i">
            <td>{{alert.ticker}}</td>
            <td>{{alert.action}}</td>
            <td>{{alert.value}}</td>
            <td>
              <button @click="deleteAlert(alert.id)" type="button" class="btn-flat"><i class="material-icons">delete</i></button>
            </td>
          </tr>
          </tbody>
        </table>
      </div>
    </div>
    <h5 v-else class="center-align">No Alerts</h5>
  </div>
</template>

<script>
    import {required} from 'vuelidate/lib/validators'

    export default {
        name: "Alert",
        data: () => ({
            loading: false,
            searchByName: false,
            searchString: '',
            instruments: null,
            ticker: null,
            action: '',
            value: 0,
            alerts: [],
            typeAlerts: ['intersectsValueUp', 'intersectsValueDown', 'growth(percent)', 'drop(percent)']
        }),
        validations: {
            searchString: {required}
        },
        async mounted() {
            this.alerts = await this.$store.dispatch('getAlerts')
        },
        methods: {
            async searchTicker() {
                if (this.$v.$invalid) {
                    this.$v.$touch()
                    return
                }
                this.loading = true
                const formData = {
                    searchByName: this.searchByName,
                    searchString: this.searchString
                }
                this.instruments = await this.$store.dispatch('searchInstrument', formData)
                this.loading = false
            },
            async addAlert() {
                if (!this.action || this.value <= 0) {
                    return
                }
                this.loading = true
                const formData = {
                    action: this.action,
                    value: this.value,
                    ticker: this.ticker
                }
                await this.$store.dispatch("addAlert", formData)
                this.loading = false
                this.$store.commit('setMessage', 'alert добавлен')
                this.alerts = await this.$store.dispatch('getAlerts')

                this.searchByName = false
                this.searchString = ''
                this.instruments = null
                this.ticker = null
                this.action = ''
                this.value = 0
            },
            async deleteAlert(id) {
                await this.$store.dispatch('deleteAlert', id)
                this.alerts = await this.$store.dispatch('getAlerts')
            }
        }
    }
</script>

<style scoped>

</style>
