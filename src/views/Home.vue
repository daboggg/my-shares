<template>
  <table class="striped">
    <thead>
    <tr>
      <th>Ticker</th>
      <th>Direction</th>
      <th>Price</th>
      <th>Number</th>
    </tr>
    </thead>

    <tbody>
    <tr v-for="t in transactions" :key="t.id">
      <td>{{t.ticker}}</td>
      <td>{{t.directionOfTransaction}}</td>
      <td>{{t.price}}</td>
      <td>{{t.numberOfShares}}</td>
    </tr>

    </tbody>
  </table>
</template>

<script>
    export default {
        name: "Home",
        data: () => ({
            transactions: []
        }),
        async mounted() {
            this.transactions = await this.$store.dispatch('getTransactions')
        }
    }
</script>

<style scoped>

</style>