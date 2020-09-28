<template>
  <div ref="dv">
    <loader v-if="loading"/>
    <trading-vue
            v-else
            :data="chart"
            :width="width"
            :height="height"
    ></trading-vue>
    <div class="fixed-action-btn" ref="fixed">
      <a class="btn-floating btn-large teal" @click="$router.push('/')">
        <i class="large material-icons">arrow_back</i>
      </a>
    </div>
  </div>
</template>

<script>
    import TradingVue from 'trading-vue-js'
    // import Data from '../utils/data'

    export default {
        name: "Charts",
        data: () => ({
            loading: true,
            chart: {},
            width: document.documentElement.clientWidth,
            height: document.documentElement.clientHeight*0.95,
            floatingButton: null
        }),
        methods: {
            onResize() {
                this.width = document.documentElement.clientWidth
                this.height = document.documentElement.clientHeight *0.95
                console.log(this.$refs.dv.scrollWidth)
            }
        },
        async created() {
            const res = await this.$store.dispatch('getDataForChart', this.$route.params.ticker)
            const candles = res.candles.map(item=>{
                const tmp =[]
                tmp.push(item.datetime)
                tmp.push(item.open)
                tmp.push(item.high)
                tmp.push(item.low)
                tmp.push(item.close)
                tmp.push(item.volume)
                return tmp
            })
            this.chart = {
                chart: {
                    name: this.$route.params.ticker,
                    type: 'Candles',
                    data: candles,
                    settings: {
                        showVolume: true
                    }
                }
            }
            this.loading = false
            // console.log(candles)
        },
        mounted()  {
            // eslint-disable-next-line no-undef
            this.floatingButton = M.FloatingActionButton.init(this.$refs.fixed, {
                direction: 'left'
            })
            window.addEventListener('resize', this.onResize)
            // this.width = this.$refs.dv.clientWidth
        },
        destroyed() {
            if (this.floatingButton && this.floatingButton.destroy) {
                this.floatingButton.destroy()
            }
        },
        beforeDestroy() {
            window.removeEventListener('resize', this.onResize)
        },
        components: {
            TradingVue
        }
    }
</script>

<style scoped>

</style>