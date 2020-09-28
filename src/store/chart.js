import Vue from 'vue'
import router from "../router";

const ipEndPort = process.env.VUE_APP_SERVERIPENDPORT
export default {
    state: {
    },
    mutations: {
    },
    actions: {
        async getDataForChart({commit, getters}, ticker) {
            try {
                let res = await Vue.http.get(`${ipEndPort}api/chart`,
                    {
                        params: {ticker},
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
        }
    },
    getters: {
    }
}