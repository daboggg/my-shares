<template>
  <div class="container">
    <ul class="collapsible" ref="collapsible">
      <li>
        <div class="collapsible-header row valign-wrapper">
          <div class="col s2 center teal-text"><b>Ticker</b></div>
          <div class="col s2 center teal-text"><b>Number Of Shares</b></div>
          <div class="col s2 center teal-text"><b>Price</b></div>
          <div class="col s2 center teal-text"><b>Last Price</b></div>
          <div class="col s2 center teal-text"><b>All Time</b></div>
          <div class="col s2 center teal-text"><b>All Time %</b></div>
        </div>
      </li>
      <li v-for="(pos, i) in collect" :key="i">
        <div class="collapsible-header row grey lighten-1">
          <div class="col s2 center"><b>{{pos.ticker}}</b></div>
          <div class="col s2 center"><b>{{pos.numberOfShares}}</b></div>
          <div class="col s2 center"><b>{{pos.price}}</b></div>
          <div class="col s2 center"><b>{{pos.lastPrice}}</b></div>
          <div class="col s2 center"><b>{{pos.allTime}}</b></div>
          <div class="col s2 center"><b>{{pos.allTimePercent}}</b></div>
        </div>
        <div class="collapsible-body"><span>Lorem ipsum dolor sit amet.</span></div>
      </li>
    </ul>
  </div>


</template>

<script>
    export default {
        name: "Home",
        computed: {
            transactions() {
                return this.$store.getters.transactions
            },
            collect() {
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
                        allTimePercent: 0
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
                        position.allTime = (position.lastPrice - position.price).toFixed(2)
                        // изменение в процентах за все время
                        position.allTimePercent = ((position.lastPrice - position.price) / position.price * 100).toFixed(2)
                    }

                    positions.push(position)

                })
                return positions
            }
        },
        async mounted() {
            await this.$store.dispatch('getTransactions');
            // eslint-disable-next-line no-undef
            M.Collapsible.init(this.$refs.collapsible)
        }
    }
</script>

<style scoped>
.brdr {
  border-left: black 1px solid;
  border-right: black 1px solid;
  margin: 0;
  padding: 0;
  height: 100%;
  width: 100%;
}
</style>