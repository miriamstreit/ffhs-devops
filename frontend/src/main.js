import App from "./App.vue";

import { createApp } from "vue";
import { createPinia } from "pinia";

// Plugins
import { registerPlugins } from "@/plugins";

import { router } from "./router";

const pinia = createPinia();

const app = createApp(App).use(pinia).use(router);

registerPlugins(app);

app.mount("#app");
