<template>
  <v-container>
    <v-row>
      <v-col><h1>Tanks</h1></v-col>
    </v-row>
    <div class="home__custom-tank-wrapper mt-4">
      <div class="home__custom-tanks d-flex">
        <v-card
          v-for="tank in getTanks"
          :key="tank.tankId"
          text=""
          variant="tonal"
        >
          <v-card-title> Tank {{ tank.tankId }} </v-card-title>
          <v-card-subtitle>
            {{ tank.brewingProcess?.endDate }} <br>
            {{ tank.brewingProcess?.beerType.name }}
          </v-card-subtitle>
          <v-card-text />
        </v-card>
      </div>
    </div>
    <v-row>
      <v-col class="mt-6">
        <h1>Schwellwert</h1>
      </v-col>
    </v-row>
    <v-card
      variant="tonal"
      class="pa-5 mt-6"
    >
      <v-row
        v-for="ingredient in getIngredients"
        :key="ingredient.ingredientId"
        class="d-flex"
      >
        <v-col>{{ ingredient.name }}</v-col>
        <v-col
          class="text-right"
          style="color: #f44336"
        >
          {{ ingredient.stock }} kg
        </v-col>
      </v-row>
    </v-card>
  </v-container>
</template>

<script>
import { useTankStore } from "@/stores/tank";
import { useIngredientStore } from "@/stores/ingredient";
import { mapState, mapActions } from "pinia";

export default {
  data: () => ({
    tanks: [],
  }),

  computed: {
    ...mapState(useTankStore, ["getTanks"]),
    ...mapState(useIngredientStore, ["getIngredients"]),
  },

  // Mounted wird am Anfang des vue-Lifecycle aufgerufen. Somit kann vor dem rendern der Page die Funktion
  // aufgelisteten Funktionen aufgerufen werden.
  async mounted() {
    await this.fetchTanks();
    await this.fetchIngredientsBelowThreshold();
  },

  // Wird verwendet, damit die funktionen aus dem Store in diesem Komponenten verwendet werden können.
  methods: {
    ...mapActions(useTankStore, ["fetchTanks"]),
    ...mapActions(useIngredientStore, ["fetchIngredientsBelowThreshold"]),
  },
};
</script>

<style>
.home__custom-tank-wrapper {
  overflow-y: scroll;
  width: 100%;
  padding-bottom: 15px;
}

.home__custom-tanks {
  overflow-y: scroll;
  width: fit-content;
  gap: 15px;
}

.home__custom-tanks .v-card {
  width: 120px;
  height: 200px;
  display: inline-block;
}
</style>
