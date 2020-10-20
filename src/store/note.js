import Vue from 'vue'
import router from "../router";

const ipEndPort = process.env.VUE_APP_SERVERIPENDPORT
export default {
    state: {
    },
    mutations: {
    },
    actions: {
        async getNotes({commit, getters}) {
            try {
                const res = await Vue.http.get(`${ipEndPort}api/note`,
                    {
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
        async getNote({commit, getters}, id) {
            try {
                const res = await Vue.http.get(`${ipEndPort}api/note/${id}`,
                    {
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
        async createNote({commit, getters}, note) {
            try {
                await Vue.http.post(`${ipEndPort}api/note`,
                    JSON.stringify(note),
                    {
                        headers: {
                            'Content-Type': 'application/json',
                            'Token': getters.getToken
                        }
                    })
                commit('setMessage', 'запись добавлена')
            } catch (e) {
                if (e.bodyText === 'invalid token') {
                    router.push("/login?message=invalid token");
                } else {
                    console.log(e.bodyText || e.body);
                    commit('setError', e.bodyText || e.body)
                }
            }
        },
        async deleteNote({commit, getters}, id) {
            try {
                await Vue.http.delete(`${ipEndPort}api/note`,
                    {
                        params: {id: id},
                        headers: {
                            'Content-Type': 'application/json',
                            'Token': getters.getToken
                        }
                    })
                commit('setMessage', 'запись удалена')
                await router.push('/notebook')
            } catch (e) {
                if (e.bodyText === 'invalid token') {
                    router.push("/login?message=invalid token");
                } else {
                    console.log(e.bodyText || e.body);
                    commit('setError', e.bodyText || e.body)
                }
            }
        },
        async editNote({commit, getters}, note) {
            try {
                await Vue.http.put(`${ipEndPort}api/note`,
                    JSON.stringify(note),
                    {
                        headers: {
                            'Content-Type': 'application/json',
                            'Token': getters.getToken
                        }
                    })
                commit('setMessage', 'запись изменена')
                await router.push('/notebook')
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
