<template>
  <v-container>
    <v-row align-content="center">
      <v-col>
        <h1>CanBrew</h1>
      </v-col>
    </v-row>
    <v-card
      variant="tonal"
      class="pa-5 mt-6"
    >
      <v-row>
        <v-col>
          <h3>Braubar</h3>
        </v-col>
      </v-row>
      <v-divider class="mb-4 mt-1" />
      <v-row
        v-for="beerType in getCanBrewBeerTypes.filter(
          (x) => x.brewable === true
        )"
        :key="beerType.name"
        class="d-flex"
      >
        <v-col>{{ beerType.name }}</v-col>
      </v-row>
    </v-card>
    <v-card
      variant="tonal"
      class="pa-5 mt-6"
    >
      <v-row>
        <v-col>
          <h3>Zutaten fehlen</h3>
        </v-col>
      </v-row>
      <v-divider class="mb-4 mt-1" />
      <v-row
        v-for="beerType in getCanBrewBeerTypes.filter(
          (x) => x.brewable !== true
        )"
        :key="beerType.name"
        class="d-flex align-center"
      >
        <v-col>{{ beerType.name }}</v-col>
        <v-col class="text-right">
          <v-btn
            color="#f0c40f"
            variant="text"
            icon="mdi-information"
            @click="
              missingIngredientDialog = true;
              selectedBeerType = beerType;
            "
          />
        </v-col>
      </v-row>
    </v-card>
    <v-dialog v-model="missingIngredientDialog">
      <v-card>
        <v-card-title>
          Fehlende Zutaten {{ selectedBeerType.name }}
        </v-card-title>
        <v-card-text>
          <v-row
            v-for="ingredient in selectedBeerType.missingIngredients"
            :key="ingredient.ingredient.ingredientId"
          >
            <v-col>{{ ingredient.ingredient.name }}</v-col>
            <v-col
              class="text-right"
              style="color: #f44336"
            >
              {{ ingredient.amount }} kg.
            </v-col>
          </v-row>
        </v-card-text>
        <v-card-actions>
          <v-spacer />
          <v-btn
            color="primary"
            text
            @click="missingIngredientDialog = false"
          >
            Schliessen
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-container>
</template>

<script>
import { useCanBrewStore } from "@/stores/canBrew";
import { mapState, mapActions } from "pinia";

export default {
  data: () => ({
    missingIngredientDialog: false,
    selectedBeerType: {},
  }),

  computed: {
    ...mapState(useCanBrewStore, ["getCanBrewBeerTypes"]),
  },

  // Mounted wird am Anfang des vue-Lifecycle aufgerufen. Somit kann vor dem rendern der Page die Funktion
  // fetchCanBrewBeerTypes aufgerufen werden.
  async mounted() {
    await this.fetchCanBrewBeerTypes();
  },

  // Wird verwendet, damit die funktionen aus dem Store in diesem Komponenten verwendet werden können.
  methods: {
    ...mapActions(useCanBrewStore, ["fetchCanBrewBeerTypes"]),
  },
};
</script>

<style></style>
