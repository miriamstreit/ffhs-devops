/**
 * Dieses Modul exportiert einen Pinia-Store, der den Zustand im Zusammenhang mit Biertypen verwaltet.
 * Der Store hat folgende Funktionen:
 * - Ein state Feld namens `beerTypes`, das ein Array von Biertypen enthält
 * - Ein Getter namens `getBeerTypes`, der das Array von Biertypen zurückgibt
 * - Drei Aktionen:
 *   - `fetchBeerTypes`: Holt eine Liste von Biertypen von der API und speichert sie im state Feld `beerTypes`
 *   - `saveBeerType`: Speichert einen neuen Biertyp in der API und fügt ihn dem state Feld `beerTypes` hinzu
 *   - `updateBeerType`: Aktualisiert einen vorhandenen Biertyp in der API und aktualisiert den entsprechenden Eintrag im state Feld `beerTypes`
 */

import { defineStore } from "pinia";
import { showSnackbar } from "@/helpers/snackbarHelper";

import apiClient from "@/api/ApiClient";

export const useBeerTypeStore = defineStore("beerTypeStore", {
  state: () => ({
    beerTypes: [],
  }),
  getters: {
    getBeerTypes: (state) => state.beerTypes,
  },
  actions: {
    async fetchBeerTypes() {
      try {
        const response = await apiClient.get("/beertypes");

        this.beerTypes = response.data;
      } catch (error) {
        showSnackbar({ message: "Etwas ist schief gelaufen", color: "error" });
      }
    },

    async saveBeertype(beerType) {
      try {
        const response = await apiClient.post("/beertypes", beerType);

        this.beerTypes.push(response.data);
      } catch (error) {
        showSnackbar({ message: "Etwas ist schief gelaufen", color: "error" });
      }
    },

    async updateBeerType(beerType) {
      try {
        await apiClient.put(`/beertypes/${beerType.beerTypeId}`, beerType);
      } catch (error) {
        showSnackbar({ message: "Etwas ist schief gelaufen", color: "error" });
      }
    },

    async deleteBeerType(beerTypeId) {
      try {
        await apiClient.delete(`/beertypes/${beerTypeId}`);

        this.beerTypes = this.beerTypes.filter(
          (item) => item.beerTypeId !== beerTypeId
        );
      } catch (error) {
        showSnackbar({ message: "Etwas ist schief gelaufen", color: "error" });
      }
    },
  },
});
