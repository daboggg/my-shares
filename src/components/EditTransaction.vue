<template>
  <div>
    <div ref="modal" id="modal1" class="modal">
      <div class="modal-content">
        <h5 class="center-align">Edit transaction</h5>
        <div class="row">
          <form class="col s12" @submit.prevent="editTransaction">
            <div>{{ticker}}</div>
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
                <label for="price">цена</label>
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
                <label for="numberOfShares">количество</label>
                <small
                        class="helper-text invalid"
                        v-if="isNaN(numberOfShares) || numberOfShares.includes('.')"
                >
                  только целые числа
                </small>
              </div>
            </div>
            <div class="row">
              <div class="input-field col s6">
                <input v-model="transactionDate" type="text" ref="dtp" id="dtp">
                <label for="dtp">дата</label>
              </div>
              <div class="input-field col s6">
                <textarea v-model="note" id="textarea1" class="materialize-textarea"></textarea>
                <label for="textarea1">комментарий</label>
              </div>
            </div>
            <div class="row">
              <div class="container">
                <button class="btn-small waves-effect waves-light" @click="modalEditTransaction.close()" type="button">
                  отменить
                </button>
                <button class="right btn-small waves-effect waves-light" type="submit" name="action">
                  изменить
                </button>
              </div>
            </div>
            <div class="row">
              <div class="container center">
                <button class="btn-small waves-effect waves-light" @click="modalDeleteTransaction.open()" type="button">
                  удалить сделку
                </button>
              </div>
            </div>
          </form>
        </div>
      </div>
      <div ref="modalDeleteTransaction" id="modalDelete" class="modal">
        <div class="modal-content">
          <p>Точно хотите удалить сделку?</p>
        </div>
        <div class="modal-footer">
          <a href="#!" @click="deleteTransaction()" class="modal-close waves-effect waves-green btn-flat">Подтвердить</a>
          <a href="#!" class="modal-close waves-effect waves-green btn-flat">Отменить</a>
        </div>
      </div>
    </div>
  </div>

</template>

<script>
    import {required} from 'vuelidate/lib/validators'
    export default {
        name: "EditTransaction",
        props: ['editableTransaction', 'refresh'],
        data: () => ({
            modalEditTransaction: null,
            modalDeleteTransaction: null,
            ticker:'',
            directionOfTransaction: true,
            price: '',
            numberOfShares: '',
            note: '',
            transactionDate: ''
        }),
        validations: {
            searchString: {required}
        },
        methods: {
            async editTransaction() {
                if (!this.price || isNaN(this.price) || !this.numberOfShares || isNaN(this.numberOfShares) || this.numberOfShares.includes('.')) {
                    return
                }
                const formData = {
                    id: this.editableTransaction.id,
                    ticker: this.ticker,
                    directionOfTransaction: this.directionOfTransaction,
                    price: this.price,
                    numberOfShares: this.numberOfShares,
                    note: this.note,
                    transactionDate: new Date(this.transactionDate).getTime()
                }
                await this.$store.dispatch('editTransaction', formData)
                this.modalEditTransaction.close()
                this.$store.commit('setMessage', 'запись изменена')
                this.refresh()
            },
            async deleteTransaction() {
                await this.$store.dispatch('deleteTransaction', this.editableTransaction.id)
                this.$store.commit('setMessage', 'запись удалена')
                this.refresh()
            }
        },
        mounted() {

            // eslint-disable-next-line no-undef
            this.modalEditTransaction = M.Modal.init(this.$refs.modal)
            this.$emit('modalEdit', this.modalEditTransaction)
            // eslint-disable-next-line no-undef
            this.datepicker = M.Datepicker.init(this.$refs.dtp, {
                format: 'mmm dd, yyyy',
                onSelect: date => this.transactionDate = new Date(date).toDateString(),
                autoClose: true
            })
            // eslint-disable-next-line no-undef
            this.modalDeleteTransaction = M.Modal.init(this.$refs.modalDeleteTransaction)
        },
        watch:{
            editableTransaction(transaction) {
                this.directionOfTransaction = transaction.directionOfTransaction
                this.ticker = transaction.ticker
                this.price = transaction.price
                this.numberOfShares = transaction.numberOfShares+''
                this.transactionDate = new Date(transaction.transactionDate).toDateString()
                this.note = transaction.note

            }
        },
        destroyed() {
            if (this.modalEditTransaction && this.modalEditTransaction.destroy) {
                this.modalEditTransaction.destroy()
            }
        }
    }
</script>

<style scoped>

</style>