import Vue from 'vue'
import router from "../router";

const ipEndPort = process.env.VUE_APP_SERVERIPENDPORT
export default {
    state: {
        transactions: []
    },
    mutations: {
        setTransactions(state, transactions) {
            state.transactions = transactions
        },
        clearTransactions(state) {
            state.transactions = []
        }
    },
    actions: {
        async getTransactions({commit, getters}) {
            try {
                const res = await Vue.http.get(`${ipEndPort}api/transaction`, {
                    headers: {
                        'Content-Type': 'application/json',
                        'Token': getters.getToken
                    }
                })
                const data = await res.json()
                commit('setTransactions', data)
            } catch (e) {
                if (e.bodyText === 'invalid token') {
                    router.push("/login?message=invalid token");
                } else {
                    console.log(e.bodyText || e.body);
                    commit('setError', e.bodyText || e.body)
                }
            }
        },
        async addTransaction({commit, getters}, formData) {
            try {
                await Vue.http.post(`${ipEndPort}api/transaction`,
                    JSON.stringify(formData),
                    {
                        headers: {
                            'Content-Type': 'application/json',
                            'Token': getters.getToken
                        }
                    })
            } catch (e) {
                if (e.bodyText === 'invalid token') {
                    router.push("/login?message=invalid token");
                } else {
                    console.log(e.bodyText || e.body);
                    commit('setError', e.bodyText || e.body)
                }
            }
        },
        async editTransaction({commit, getters}, formData) {
            try {
                await Vue.http.put(`${ipEndPort}api/transaction`,
                    JSON.stringify(formData),
                    {
                        headers: {
                            'Content-Type': 'application/json',
                            'Token': getters.getToken
                        }
                    })
            } catch (e) {
                if (e.bodyText === 'invalid token') {
                    router.push("/login?message=invalid token");
                } else {
                    console.log(e.bodyText || e.body);
                    commit('setError', e.bodyText || e.body)
                }
            }
        },
        async deleteTransaction({commit, getters}, idTransaction) {
            try {
                await Vue.http.delete(`${ipEndPort}api/transaction`,
                    {
                        params: {id: idTransaction},
                        headers: {
                            'Content-Type': 'application/json',
                            'Token': getters.getToken
                        }
                    })
            } catch (e) {
                if (e.bodyText === 'invalid token') {
                    router.push("/login?message=invalid token");
                } else {
                    console.log(e.bodyText || e.body);
                    commit('setError', e.bodyText || e.body)
                }
            }
        }
    },
    getters: {
        transactions: state => state.transactions
    }
}