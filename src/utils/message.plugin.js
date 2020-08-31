export default {
    install(Vue) {
        Vue.prototype.$message = function (html) {
            // eslint-disable-next-line no-undef
            M.toast({html, classes: 'green darken-3', displayLength: 6000})
        }

        Vue.prototype.$error = function (html) {
            // eslint-disable-next-line no-undef
            const tos = M.toast({html, classes: 'red darken-3', displayLength: 6000})
            console.log(tos.options)
        }
    }
}