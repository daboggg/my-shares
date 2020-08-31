import Vue from 'vue'
import Vuex from 'vuex'
import auth from "./auth";

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
    actions: {},
    getters: {
        error: s => s.error,
        message: s => s.message
    },
    modules: {
        auth
    }
})
