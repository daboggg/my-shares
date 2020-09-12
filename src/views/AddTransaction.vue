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
      <div class="col s12">
        <loader v-if="loading"/>
        <h6 v-else-if="instruments&&Object.keys(instruments).length === 0" class="center-align">Ничего не найдено</h6>
        <div v-else class="card horizontal" v-for="(v ,k, i) in instruments" :key="i">
          <div class="card-stacked">
            <div class="card-content">
              {{v.description}}
              <br>
              {{v.assetType}}
            </div>
            <div class="card-action">
              <a href="#" @click="ticker = k">Ticker: {{k}}</a>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="row" v-show="ticker">
      <form class="col s12" @submit.prevent="addTransaction">
        <div class="row valign-wrapper">
          <div class="switch col s5 m4 l3">
            <label>
              Sell
              <input type="checkbox" v-model="directionOfTransaction">
              <span class="lever"></span>
              Buy
            </label>
          </div>
          <div class="input-field col s5 m6 l7">
            <input
                    v-model.trim="price"
                    placeholder="price"
                    id="price"
                    type="text"
                    class="validate"
            >
            <small
                    class="helper-text invalid"
                    v-if="isNaN(price)"
            >
              только числа
            </small>
          </div>
          <div class="input-field col s5 m6 l7">
            <input
                    v-model.trim="numberOfShares"
                    placeholder="numberOfShares"
                    id="numberOfShares"
                    type="text"
                    class="validate"
            >
            <small
                    class="helper-text invalid"
                    v-if="isNaN(numberOfShares) || numberOfShares.includes('.')"
            >
              только целые числа
            </small>
          </div>
        </div>
        <div class="row">
          <div class="input-field col s8">
            <textarea v-model="note" id="textarea1" class="materialize-textarea"></textarea>
            <label for="textarea1">Коментарий</label>
          </div>
          <div class="input-field col s4">
            <input type="text" ref="dtp" id="dtp">
            <label for="dtp">Дата</label>
          </div>
        </div>
        <div class="row">
          <loader v-if="loading"/>
          <div v-else class="container">
            <button class="left btn-small waves-effect waves-light col" type="button" @click="resetData">отменить</button>
            <button class="right btn-small waves-effect waves-light col" type="submit" name="action">
              добавить
              <i class="material-icons right">send</i>
            </button>
          </div>
        </div>
      </form>
    </div>
  </div>
</template>

<script>
    import {required} from 'vuelidate/lib/validators'

    export default {
        name: "AddTransaction",
        data: () => ({
            loading: false,
            searchByName: false,
            searchString: '',
            instruments: null,
            ticker: '',

            directionOfTransaction: true,
            price: '',
            numberOfShares: '',
            note: '',
            transactionDate: '',

            datepicker: null
        }),
        mounted() {
            // eslint-disable-next-line no-undef
            this.datepicker = M.Datepicker.init(this.$refs.dtp, {
                format: 'mmm dd, yyyy',
                onSelect: date => this.transactionDate = new Date(date).getTime(),
                autoClose: true
            })
        },
        validations: {
            searchString: {required}
        },
        watch: {},
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
            async addTransaction() {
                if (!this.price || isNaN(this.price) || !this.numberOfShares || isNaN(this.numberOfShares) || this.numberOfShares.includes('.') || !this.transactionDate) {
                    return
                }
                this.loading = true
                const formData = {
                    ticker: this.ticker,
                    directionOfTransaction: this.directionOfTransaction,
                    price: this.price,
                    numberOfShares: this.numberOfShares,
                    note: this.note,
                    transactionDate: this.transactionDate
                }
                await this.$store.dispatch('addTransaction', formData)
                this.loading = false
                this.resetData()
                this.$store.commit('setMessage', 'запись добавлена')
                await this.$router.push('/')
            },
            resetData() {
                this.searchByName = false
                this.searchString = ''
                this.instruments = null
                this.ticker = ''
                this.directionOfTransaction = true
                this.price = ''
                this.numberOfShares = ''
                this.note = ''
                this.transactionDate = ''
            }
        },
        destroyed() {
            if (this.datepicker && this.datepicker.destroy) {
               this.datepicker.destroy()
            }
        },
    }
</script>

<style scoped>

</style>