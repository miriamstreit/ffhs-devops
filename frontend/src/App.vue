<template>
  <v-app>
    <v-main class="py-6 px-4">
      <router-view :key="$route.fullPath" />
      <TheNavigation v-if="getUser != null" />
    </v-main>
    <Snackbar ref="snackbar" />
  </v-app>
</template>

<script>
import TheNavigation from "@/components/TheNavigation.vue";
import Snackbar from "./components/TheSnackbar.vue";

import { useUserStore } from "@/stores/user";
import { mapState } from "pinia";

export default {
  components: { TheNavigation, Snackbar },
  computed: {
    ...mapState(useUserStore, ["getUser"]),
  },
  mounted() {
    window.app.snackbar = this.$refs.snackbar;
  },
};
</script>
