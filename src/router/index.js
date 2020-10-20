import Vue from 'vue'
import VueRouter from 'vue-router'
import store from '../store'

Vue.use(VueRouter)

const routes = [
    {
        path: '/',
        name: 'home',
        meta: {layout: 'main', auth: true},
        component: () => import('../views/Home.vue')
    },
    {
        path: '/add',
        name: 'add transaction',
        meta: {layout: 'main', auth: true},
        component: () => import('../views/AddTransaction.vue')
    },
    {
        path: '/alert',
        name: 'alert',
        meta: {layout: 'main', auth: true},
        component: () => import('../views/Alert.vue')
    },
    {
        path: '/addNote',
        name: 'add note',
        meta: {layout: 'main', auth: true},
        component: () => import('../views/AddNote.vue')
    },
    {
        path: '/noteEdit/:id',
        name: 'note edit',
        meta: {layout: 'main', auth: true},
        component: () => import('../views/NoteEdit.vue')
    },
    {
        path: '/note/:id',
        name: 'note',
        meta: {layout: 'main', auth: true},
        component: () => import('../views/Note.vue')
    },
    {
        path: '/notebook',
        name: 'notebook',
        meta: {layout: 'main', auth: true},
        component: () => import('../views/Notebook.vue')
    },
    {
        path: '/charts/:ticker',
        name: 'charts',
        meta: {layout: 'chart', auth: true},
        component: () => import('../components/Charts.vue')
    },
    {
        path: '/profile',
        name: 'profile',
        meta: {layout: 'main', auth: true},
        component: () => import('../views/Profile.vue')
    },
    {
        path: '/login',
        name: 'login',
        meta: {layout: 'empty'},
        component: () => import('../views/Login.vue')
    },
    {
        path: '/register',
        name: 'register',
        meta: {layout: 'empty'},
        component: () => import('../views/Register.vue')
    }
];

const router = new VueRouter({
    mode: 'history',
    base: process.env.BASE_URL,
    routes
})

router.beforeEach((to, from, next) => {

    const usernameFromSessionStorage = sessionStorage.getItem('username')
    const tokenFromSessionStorage = sessionStorage.getItem('token')

    const currentUser = store.getters.getToken

    const requireAuth = to.matched.some(record => record.meta.auth)
    if (requireAuth && !currentUser && !tokenFromSessionStorage) {
        next('/login');
    } else {
        if (tokenFromSessionStorage) {
            store.commit('login', {
                username: usernameFromSessionStorage,
                token: tokenFromSessionStorage
            })
        }
        next()
    }
})

export default router
