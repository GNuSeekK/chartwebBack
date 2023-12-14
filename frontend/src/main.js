import Antd from 'ant-design-vue'
import 'ant-design-vue/dist/reset.css'
import {createPinia} from 'pinia'
import {createApp} from 'vue'
import App from './App.vue'
import router from '@/router'
import VueCookies from 'vue3-cookies'
import './assets/fonts.css'

const KAKAO_APP_KEY = import.meta.env.VITE_KAKAO_APP_KEY
const app = createApp(App)
app.use(Antd)
app.use(createPinia())
app.use(router)
app.use(VueCookies)
window.Kakao.init(KAKAO_APP_KEY)
app.mount('#app')