import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import './quasar'
import VueCodemirror from 'vue-codemirror'
import 'codemirror/lib/codemirror.css'

Vue.config.productionTip = false
Vue.use(VueCodemirror);

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
