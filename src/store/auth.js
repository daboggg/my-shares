import Vue from 'vue'
import router from '../router'
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
                const res = await Vue.http.post('http://localhost:9006/user/register',
                    JSON.stringify(formData),
                    {'Content-Type': 'application/json'})
                const data = await res.json()
                commit('login', data)
                await router.push('/')
            }catch (e) {
                console.log(e.bodyText)
            }
        },
        async login({commit}, formData) {
            try {
                const res = await Vue.http.post('http://localhost:9006/user/login',
                    JSON.stringify(formData),
                    {'Content-Type': 'application/json'})
                const data = await res.json()
                commit('login', data)
                await router.push('/')
            }catch (e) {
                console.log(e.bodyText)
            }
        }
    },
    getters: {
        getUsername: state => state.username,
        getToken: state => state.token
    }
}