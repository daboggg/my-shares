import Vue from 'vue'
import router from '../router'
const  ipEndPort = process.env.VUE_APP_SERVERIPENDPORT
export default {
    state: {
        username: null,
        token: null
    },
    mutations: {
        login(state, login) {
            state.username = login.username
            state.token = login.token
            sessionStorage.setItem('username', login.username)
            sessionStorage.setItem('token', login.token)
        },
        logout(state) {
            state.username = null
            state.token = null
            sessionStorage.removeItem('username')
            sessionStorage.removeItem('token')
        }
    },
    actions: {
        async register({commit}, formData) {
            try {
                const res = await Vue.http.post(`${ipEndPort}api/user/register`,
                    JSON.stringify(formData),
                    {'Content-Type': 'application/json'})
                const data = await res.json()
                commit('login', data)
                await router.push('/')
            }catch (e) {
                console.log(e.bodyText || e.body)
                commit('setError', e.bodyText || e.body)
            }
        },
        async login({commit}, formData) {
            try {
                const res = await Vue.http.post(`${ipEndPort}api/user/login`,
                    JSON.stringify(formData),
                    {'Content-Type': 'application/json'})
                const data = await res.json()
                commit('login', data)
                await router.push('/')
            }catch (e) {
                console.log(e.bodyText || e.body)
                commit('setError', e.bodyText || e.body)
            }
        }
    },
    getters: {
        getUsername: state => state.username,
        getToken: state => state.token
    }
}