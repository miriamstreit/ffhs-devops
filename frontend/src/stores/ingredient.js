/**
 * Dieses Modul exportiert einen Pinia-Store, der den Zustand im Zusammenhang mit Zutaten verwaltet.
 * Der Store hat folgende Funktionen:
 * - Ein Statusfeld namens `ingredients`, das ein Array von Zutaten enthält
 * - Ein Getter namens `getIngredients`, der das Array von Zutaten zurückgibt
 * - Fünf Aktionen:
 *   - `fetchIngredient`: Holt eine Liste von Zutaten von der API und speichert sie im Statusfeld `ingredients`
 *   - `fetchIngredientsBelowThreshold`: Holt eine Liste von Zutaten von der API, die unter dem Schwellenwert liegen, und speichert sie im Statusfeld `ingredients`
 *   - `saveIngredient`: Speichert eine neue Zutat in der API und fügt sie dem Statusfeld `ingredients` hinzu
 *   - `updateIngredient`: Aktualisiert eine vorhandene Zutat in der API und aktualisiert den entsprechenden Eintrag im Statusfeld `ingredients`
 *   - `deleteIngredient`: Löscht eine Zutat aus der API und entfernt sie aus dem Statusfeld `ingredients`
 */

import { defineStore } from "pinia";
import { showSnackbar } from "@/helpers/snackbarHelper";

import apiClient from "@/api/ApiClient";

export const useIngredientStore = defineStore("ingredientStore", {
  state: () => ({
    ingredients: [],
  }),
  getters: {
    getIngredients: (state) => state.ingredients,
  },
  actions: {
    async fetchIngredient() {
      try {
        const response = await apiClient.get("/ingredients");

        this.ingredients = response.data;
      } catch (error) {
        showSnackbar({ message: "Etwas ist schief gelaufen", color: "error" });
      }
    },

    async fetchIngredientsBelowThreshold() {
      try {
        const response = await apiClient.get("/ingredients/belowthreshold");

        this.ingredients = response.data;
      } catch (error) {
        showSnackbar({ message: "Etwas ist schief gelaufen", color: "error" });
      }
    },

    async saveIngredient(ingredient) {
      try {
        const response = await apiClient.post("/ingredients", ingredient);

        this.ingredients.push(response.data);
      } catch (error) {
        showSnackbar({ message: "Etwas ist schief gelaufen", color: "error" });
      }
    },

    async updateIngredient(ingredient) {
      try {
        await apiClient.put(
          `/ingredients/${ingredient.ingredientId}`,
          ingredient
        );

        //this.ingredients.find(x => x.ingredientId == ingredient.ingredientId) = response.data;
      } catch (error) {
        showSnackbar({ message: "Etwas ist schief gelaufen", color: "error" });
      }
    },

    async deleteIngredient(ingredientId) {
      try {
        await apiClient.delete(`/ingredients/${ingredientId}`);

        this.ingredients = this.ingredients.filter(
          (item) => item.ingredientId !== ingredientId
        );
      } catch (error) {
        showSnackbar({ message: "Etwas ist schief gelaufen", color: "error" });
      }
    },
  },
});
