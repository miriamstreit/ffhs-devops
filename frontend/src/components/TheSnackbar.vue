<!--
  Globaler Snackbar-Komponent, welcher mit der Methode show() eine Snackbar anzeigt.
-->
<template>
  <v-snackbar
    v-model="showSnackbar"
    :color="color"
    :timeout="timer"
    top
  >
    <v-icon left>
      {{ icon }}
    </v-icon>
    {{ message }}
  </v-snackbar>
</template>

<script>
export default {
  name: "TheSnackbar",
  data() {
    return {
      showSnackbar: false,
      message: "",
      color: "",
      icon: "",
      timer: 0,
    };
  },
  methods: {
    show(data) {
      this.message = data.message || 'missing "message".';
      this.color = data.color || "success";
      this.timer = data.timer || 3500;
      this.icon = data.icon || "mdi-alert-circle-outlined";
      this.showSnackbar = true;
    },
  },
};
</script>
