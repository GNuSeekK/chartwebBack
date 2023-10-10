import { createApp } from 'vue'
import App from './App.vue'
import VueCookies from "vue-cookies";

import 'bootstrap/dist/css/bootstrap.min.css'
import 'bootstrap'



const app = createApp(App)
app.use(VueCookies)

app.mount('#app')
