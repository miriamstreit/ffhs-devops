/**
 * Dieses Modul exportiert einen Pinia-Store, der den Zustand im Zusammenhang mit Tanks verwaltet.
 * Der Store hat folgende Funktionen:
 * - Ein status field namens `tanks`, das ein Array von Tanks enthält
 * - Ein Getter namens `getTanks`, der das Array von Tanks zurückgibt
 * - Eine Aktion namens `fetchTanks`, die eine Liste von Tanks von der API holt und sie im status field `tanks` speichert
 */

import { defineStore } from "pinia";
import { showSnackbar } from "@/helpers/snackbarHelper";

import apiClient from "@/api/ApiClient";

export const useTankStore = defineStore("tankStore", {
  state: () => ({
    tanks: [],
  }),
  getters: {
    getTanks: (state) => state.tanks,
  },
  actions: {
    async fetchTanks() {
      try {
        const response = await apiClient.get("/tanks");

        this.tanks = response.data.sort();
      } catch (error) {
        showSnackbar({ message: "Etwas ist schief gelaufen", color: "error" });
      }
    },
  },
});
