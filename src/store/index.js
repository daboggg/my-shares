import Vue from 'vue'
import Vuex from 'vuex'
import auth from "./auth";
import transaction from "./transaction";
import router from "../router";
const  ipEndPort = process.env.VUE_APP_SERVERIPENDPORT

Vue.use(Vuex)

export default new Vuex.Store({
    state: {
        error: null,
        message: null
    },
    mutations: {
        setError(state, error) {
            state.error = error
        },
        clearError(state) {
            state.error = null
        },
        setMessage(state, message) {
            state.message = message
        },
        clearMessage(state) {
            state.message = null
        }
    },
    actions: {
        async searchInstrument({commit, getters}, formData) {
            try {
                const res = await Vue.http.get(`${ipEndPort}search/instrument`, {
                    params: formData,
                    headers: {
                        'Content-Type': 'application/json',
                        'Token': getters.getToken
                    }
                })
                const data = await res.json()
                return data
            } catch (e) {
                if (e.bodyText === 'invalid token') {
                    await router.push("/login?message=invalid token");
                } else {
                    console.log(e.bodyText || e.body);
                    commit('setError', e.bodyText || e.body)
                }
            }
        }
    },
    getters: {
        error: s => s.error,
        message: s => s.message
    },
    modules: {
        auth, transaction
    }
})
