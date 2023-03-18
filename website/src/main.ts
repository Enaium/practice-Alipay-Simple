import {createApp} from 'vue'
import App from './App.vue'
import naive from "@/util/naive";
import router from "@/router";

createApp(App).use(naive).use(router).mount('#app')
