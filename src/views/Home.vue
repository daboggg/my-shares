<template>
  <div class="container">
    <div class="row">
      <div class="col s12 right-align" style="margin-top: 1rem;">
        <button @click="refresh" class="btn-small waves-effect waves-light" type="button">
          <i class="material-icons">refresh</i>
        </button>
      </div>
    </div>
    <loader v-if="loading"/>
    <h5 v-else-if="!collection.length" class="center-align">Empty list</h5>
    <ul class="collapsible" ref="collapsible" v-else>
      <li>
        <div class="row valign-wrapper">
          <div class="col s2 center teal-text"><b>Ticker</b></div>
          <div class="col s2 center teal-text"><b>Number Of Shares</b></div>
          <div class="col s2 center teal-text"><b>Price</b></div>
          <div class="col s2 center teal-text"><b>Last Price</b></div>
          <div class="col s2 center teal-text"><b>All Time</b></div>
          <div class="col s2 center teal-text"><b>All Time %</b></div>
        </div>
      </li>
      <li v-for="(pos, i) in collection" :key="i">
        <div class="collapsible-header row grey lighten-1">
          <div class="col s2 center"><b>{{pos.ticker}}</b></div>
          <div class="col s2 center"><b>{{pos.numberOfShares}}</b></div>
          <div class="col s2 center"><b>{{pos.price}}</b></div>
          <div class="col s2 center"><b>{{pos.lastPrice}}</b></div>
          <div class="col s2 center"><b :class="pos.allTime > 0 ? 'green-text text-darken-2':'red-text'">{{pos.allTime}}</b></div>
          <div class="col s2 center"><b :class="pos.allTime > 0 ? 'green-text text-darken-2':'red-text'">{{pos.allTimePercent}}</b></div>
        </div>
        <div class="collapsible-body">
          <ul class="collection">
            <button @click="$router.push(`/charts/${pos.ticker}`)" class="center btn-small waves-effect waves-light" type="button">
              chart
            </button>
            <h5 class="center-align">{{pos.note}}</h5>
            <h6><b>Note: </b>{{pos.mainNote}}</h6>
            <h6><b>Dividend Yield %</b>: {{pos.divYield}}</h6>
            <h5 class="center-align">Transactions:</h5>
            <li v-for="(tr, i) in transactionsByTicker(pos.ticker)" :key="i" class="collection-item">
              <div style="cursor: pointer"
                   @click="note(tr.note)"
                   class="row valign-wrapper">
                <div class="col s2">
                  <button @click.stop="editTransaction(tr)" class="btn-small waves-effect waves-light" type="button">
                    e
                  </button>
                </div>
                <div class="col s4 center-align"><b>{{new Date(tr.transactionDate).toLocaleDateString()}}</b></div>
                <div class="col s2 center-align"><b :class="tr.directionOfTransaction ? 'green-text':'red-text'">{{tr.directionOfTransaction ? 'buy' : 'sale'}}</b></div>
                <div class="col s2 center-align"><b>{{tr.numberOfShares}}</b></div>
                <div class="col s2 center-align"><b>${{tr.price}}</b></div>
              </div>
            </li>
          </ul>
        </div>
      </li>
    </ul>
    <div ref="modal" id="modal1" class="modal">
      <div class="modal-content">
        <h5>Note: </h5>
        <p>{{textNote ? textNote: 'нет записки'}}</p>
      </div>
      <div class="modal-footer">
        <button class="btn-flat modal-close waves-effect waves-green">close</button>
      </div>
    </div>
    <EditTransaction :editableTransaction="editableTransaction" :refresh="refresh" @modalEdit="val => modalEditTransaction = val"/>
  </div>


</template>

<script>
  import EditTransaction from "../components/EditTransaction";
    export default {
        name: "Home",
        data: () => ({
            loading: true,
            modal: null,
            collapsible: null,
            textNote: '',
            isHovering: false,
            modalEditTransaction: null,
            editableTransaction: null
        }),
        methods: {
            transactionsByTicker(ticker) {
                return this.$store.getters.transactions[0].filter(f => f.ticker === ticker)
            },
            note(note) {
                this.textNote = note
                this.modal.open()
            },
            editTransaction(transaction) {
                this.editableTransaction = transaction
                this.modalEditTransaction.open()
                // eslint-disable-next-line no-undef
                this.$nextTick(() => M.updateTextFields())
            },
            async refresh() {
                this.loading = true
                await this.$store.dispatch('getTransactions')
                this.loading = false
                // eslint-disable-next-line no-undef
                this.$nextTick(() => this.collapsible = M.Collapsible.init(this.$refs.collapsible))
            }
        },
        computed: {
            collection() {
                if (!this.$store.getters.transactions.length) return []
                // группировка по тикеру
                const groupsByTicker = []
                Object.keys(this.$store.getters.transactions[1]).forEach(i => {
                    groupsByTicker.push(this.$store.getters.transactions[0].filter(f => f.ticker === i))
                })

                const positions = []
                // фор по группам
                groupsByTicker.forEach(gr => {

                    const position = {
                        ticker: gr[0].ticker,
                        numberOfShares: 0,
                        price: 0,
                        lastPrice: this.$store.getters.transactions[1][gr[0].ticker].lastPrice,
                        allTime: 0,
                        allTimePercent: 0,
                        mainNote: gr[0].note,
                        note: this.$store.getters.transactions[1][gr[0].ticker].description,
                        divYield: this.$store.getters.transactions[1][gr[0].ticker].divYield
                    }
                    // фор по транзакциям
                    for (let grKey in gr) {

                        // временные переменные
                        let tmpNumberOfShares = position.numberOfShares
                        let tmpPrice = position.price

                        // количесво акций
                        gr[grKey].directionOfTransaction ? position.numberOfShares = tmpNumberOfShares + gr[grKey].numberOfShares : position.numberOfShares = tmpNumberOfShares - gr[grKey].numberOfShares

                        // средняя цена акции
                        if (gr[grKey].directionOfTransaction) {
                            position.price = ((tmpPrice * tmpNumberOfShares + gr[grKey].price * gr[grKey].numberOfShares) / (tmpNumberOfShares + gr[grKey].numberOfShares)).toFixed(2)
                        }
                        // изменение за все время
                        position.allTime = ((position.lastPrice - position.price)*position.numberOfShares).toFixed(2)
                        // изменение в процентах за все время
                        position.allTimePercent = ((position.lastPrice - position.price) / position.price * 100).toFixed(2)

                        if (position.numberOfShares <= 0) {
                            position.allTime = 0
                            position.allTimePercent = 0
                            position.price =0
                            position.numberOfShares = 0
                        }
                    }

                    positions.push(position);

                })
                return positions
            }
        },
        async mounted() {
            await this.$store.dispatch('getTransactions')
            this.loading = false

            // eslint-disable-next-line no-undef
            this.$nextTick(() => this.collapsible = M.Collapsible.init(this.$refs.collapsible))
            // eslint-disable-next-line no-undef
            this.modal = M.Modal.init(this.$refs.modal)
        },
        destroyed() {
            if (this.collapsible && this.collapsible.destroy) {
                this.collapsible.destroy()
            }
            if (this.modal && this.modal.destroy) {
                this.modal.destroy()
            }
        },
        components: {
            EditTransaction
        }
    }
</script>

<style scoped>
</style>