<template>
  <v-container>
    <v-row class="mb-10">
      <v-col><h1>Login</h1></v-col>
    </v-row>
    <v-form
      ref="form"
      v-model="valid"
      lazy-validation
    >
      <v-row>
        <v-text-field
          v-model="email"
          :rules="[(v) => !!v || 'E-mail eingeben!']"
          label="E-mail"
          required
        />
      </v-row>
      <v-row>
        <v-text-field
          v-model="password"
          :rules="[(v) => !!v || 'Passwort eingeben!']"
          type="password"
          label="Passwort"
          required
        />
      </v-row>
      <v-row v-if="errorMessage">
        <p class="ma-3 red-text">
          {{ errorMessage }}
        </p>
      </v-row>
      <v-row>
        <v-spacer />
        <v-btn
          color="primary"
          class="mt-4"
          @click="submit"
        >
          Login
        </v-btn>
      </v-row>
    </v-form>
  </v-container>
</template>

<script>
import { useUserStore } from "@/stores/user";
import { mapActions, mapState } from "pinia";
import { router } from "@/router";

export default {
  data: () => ({
    email: "",
    password: "",
    valid: true,
    errorMessage: "",
  }),

  // Wird verwendet, damit die funktionen aus dem Store in diesem Komponenten verwendet werden können.
  computed: {
    ...mapState(useUserStore, ["getUser"]),
  },

  methods: {
    /**
     * checkt, ob das Form valide ist und sendet anschliessend den Login-Request.
     * Falls das Login erfolgreichwar, wird der User auf die Startseite weitergeleitet.
     */
    submit: async function () {
      const { valid } = await this.$refs.form.validate();

      if (valid) {
        let response = await this.login(this.email, this.password);

        if (response) router.push("/");
      }
    },

    // Wird verwendet, damit die funktionen aus dem Store in diesem Komponenten verwendet werden können.
    ...mapActions(useUserStore, ["login"]),
  },
};
</script>

<style>
.red-text {
  color: #b20020;
}
</style>
