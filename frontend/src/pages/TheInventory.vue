<template>
  <v-container>
    <v-row align-content="center">
      <v-col><h1>Inventory</h1></v-col>
    </v-row>
    <v-tabs
      v-model="tab"
      fixed-tabs
      color="primary"
      class="full-width mt-3"
    >
      <v-tab value="tab-ingredient">
        Zutaten
      </v-tab>
      <v-tab value="tab-beer">
        Biersorten
      </v-tab>
    </v-tabs>
    <v-window
      v-model="tab"
      class="full-width mt-4"
    >
      <v-window-item value="tab-ingredient">
        <IngredientTable />
      </v-window-item>
      <v-window-item value="tab-beer">
        <BeerTypeTable />
      </v-window-item>
    </v-window>
  </v-container>
</template>

<script>
import IngredientTable from "@/components/IngredientTable.vue";
import BeerTypeTable from "@/components/BeerTypeTable.vue";

export default {
  // Definieren der zu anezeigenden Komponentn auf der Seite
  components: { IngredientTable, BeerTypeTable },
  data: () => ({
    tab: "tab-ingredient",
  }),
};
</script>
