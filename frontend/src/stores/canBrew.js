/**
 * Dieses Modul exportiert einen Pinia-Store, der den Zustand im Zusammenhang mit Biertypen, die gebraut werden können, verwaltet.
 * Der Store hat folgende Funktionen:
 * - Ein state field namens `canBrewBeerTypes`, das ein Array von Biertypen enthält, die gebraut werden können
 * - Ein Getter namens `getCanBrewBeerTypes`, der das Array von Biertypen, die gebraut werden können, zurückgibt
 * - Eine Aktion namens `fetchCanBrewBeerTypes`, die eine Liste von Biertypen von der API holt, die gebraut werden können, und sie im state field `canBrewBeerTypes` speichert
 */

import { defineStore } from "pinia";
import { showSnackbar } from "@/helpers/snackbarHelper";

import apiClient from "@/api/ApiClient";

export const useCanBrewStore = defineStore("canBrewStore", {
  state: () => ({
    canBrewBeerTypes: [],
  }),
  getters: {
    getCanBrewBeerTypes: (state) => state.canBrewBeerTypes,
  },
  actions: {
    async fetchCanBrewBeerTypes() {
      try {
        const response = await apiClient.get("/canbrew");

        this.canBrewBeerTypes = response.data;
      } catch (error) {
        showSnackbar({ message: "Etwas ist schief gelaufen", color: "error" });
      }
    },
  },
});
