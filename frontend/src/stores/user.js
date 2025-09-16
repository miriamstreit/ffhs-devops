/**
 * Dieses Modul exportiert einen Pinia-Store, der den Zustand im Zusammenhang mit Benutzerauthentifizierung verwaltet.
 * Der Store hat folgende Funktionen:
 * - Ein status field namens `user`, das die Benutzerdaten enthält, die zurzeit im Local Storage gespeichert sind
 * - Zwei Aktionen:
 *   - `login`: Führt einen Login-Versuch mit den übergebenen Benutzername und Passwort aus und speichert die Benutzerdaten im status field `user` und im Local Storage, wenn der Login erfolgreich ist
 *   - `logout`: Entfernt die Benutzerdaten aus dem status field `user` und dem Local Storage
 * - Ein Getter namens `getUser`, der die Benutzerdaten im status field `user` zurückgibt
 */

import { defineStore } from "pinia";
import { showSnackbar } from "@/helpers/snackbarHelper";

import apiClient from "@/api/ApiClient";

export const useUserStore = defineStore("userStore", {
  state: () => ({
    user: JSON.parse(localStorage.getItem("user")),
  }),

  actions: {
    login: async function (username, password) {
      try {
        const user = await apiClient.post("/auth/login/", {
          email: username,
          password: password,
        });

        this.user = user.data;
        localStorage.setItem("user", JSON.stringify(user.data));

        return true;
      } catch (error) {
        showSnackbar({ message: "Etwas ist schief gelaufen", color: "error" });

        return false;
      }
    },
    logout() {
      this.user = null;
      localStorage.removeItem("user");
    },
  },

  getters: {
    getUser: (state) => state.user,
  },
});
