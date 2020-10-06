import Vue from 'vue'
import router from "../router";

const ipEndPort = process.env.VUE_APP_SERVERIPENDPORT
export default {
    state: {
    },
    mutations: {
    },
    actions: {
        async addAlert({commit, getters}, formData) {
            try {
                await Vue.http.post(`${ipEndPort}api/alert`,
                    JSON.stringify(formData),
                    {
                        headers: {
                            'Content-Type': 'application/json',
                            'Token': getters.getToken
                        }
                    })
                // const data = await res.json()
            } catch (e) {
                if (e.bodyText === 'invalid token') {
                    router.push("/login?message=invalid token");
                } else {
                    console.log(e.bodyText || e.body);
                    commit('setError', e.bodyText || e.body)
                }
            }
        },
        async getAlerts({commit, getters}) {
            try {
                const res = await Vue.http.get(`${ipEndPort}api/alert`, {
                    headers: {
                        'Content-Type': 'application/json',
                        'Token': getters.getToken
                    }
                })
                const data = await res.json()
                return data
            } catch (e) {
                if (e.bodyText === 'invalid token') {
                    router.push("/login?message=invalid token");
                } else {
                    console.log(e.bodyText || e.body);
                    commit('setError', e.bodyText || e.body)
                }
            }
        },
        async deleteAlert({commit, getters}, idAlert) {
            try {
                await Vue.http.delete(`${ipEndPort}api/alert`,
                    {
                        params: {id: idAlert},
                        headers: {
                            'Content-Type': 'application/json',
                            'Token': getters.getToken
                        }
                    })
                commit('setMessage', 'alert удален')
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
    }
}
