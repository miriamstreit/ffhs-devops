/**
 * Dieses Modul exportiert einen Pinia-Store, der den Zustand im Zusammenhang mit Brauprozessen verwaltet.
 * Der Store hat folgende Funktionen:
 * - Ein state field namens `brewingProcesses`, das ein Array von Brauprozessen enthält
 * - Ein Getter namens `getBrewingProcess`, der das Array von Brauprozessen zurückgibt
 * - Vier Aktionen:
 *   - `fetchBrewingProcess`: Holt eine Liste von Brauprozessen von der API und speichert sie im state field `brewingProcesses`
 *   - `saveBrewingProcess`: Speichert einen neuen Brauprozess in der API und fügt ihn dem state field `brewingProcesses` hinzu
 *   - `updateBrewingProcess`: Aktualisiert einen vorhandenen Brauprozess in der API und aktualisiert den entsprechenden Eintrag im state field `brewingProcesses`
 *   - `deleteBrewingProcess`: Löscht einen Brauprozess aus der API und entfernt ihn aus dem state field `brewingProcesses`
 */

import { defineStore } from "pinia";
import { showSnackbar } from "@/helpers/snackbarHelper";

import apiClient from "@/api/ApiClient";

export const useBrewingProcessStore = defineStore("brewingProcessStore", {
  state: () => ({
    brewingProcesses: [],
  }),
  getters: {
    getBrewingProcess: (state) => state.brewingProcesses,
  },
  actions: {
    async fetchBrewingProcess() {
      try {
        const response = await apiClient.get("/brewingprocess");

        this.brewingProcesses = response.data;
      } catch (error) {
        showSnackbar({ message: "Etwas ist schief gelaufen", color: "error" });
      }
    },

    async saveBrewingProcess(brewingProcess) {
      try {
        const response = await apiClient.post(
          "/brewingprocess",
          brewingProcess
        );

        this.brewingProcesses.push(response.data);

        return response;
      } catch (error) {
        console.log(error);
        showSnackbar({ message: error.response?.data?.message ?? error.message, color: "error" });
      }
    },

    async updateBrewingProcess(brewingProcess) {
      try {
        await apiClient.put(
          `/brewingprocess/${brewingProcess.brewingId}`,
          brewingProcess
        );
      } catch (error) {
        showSnackbar({ message: "Etwas ist schief gelaufen", color: "error" });
      }
    },

    async deleteBrewingProcess(brewingProcessId) {
      console.log(this.brewingProcesses);
      try {
        await apiClient.delete(`/brewingprocess/${brewingProcessId}`);

        this.brewingProcesses = this.brewingProcesses.filter(
          (item) => item.brewingId !== brewingProcessId
        );
      } catch (error) {
        showSnackbar({ message: "Etwas ist schief gelaufen", color: "error" });
      }
    },
  },
});
