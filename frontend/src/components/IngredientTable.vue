<template>
  <v-container>
    <v-card
      color="basil"
      flat
    >
      <v-table
        fixed-header
        height="600px"
      >
        <thead>
          <tr>
            <th class="text-left">
              Name
            </th>
            <th class="text-left">
              Anzahl (kg.)
            </th>
            <th class="text-left" />
          </tr>
        </thead>
        <tbody>
          <tr
            v-for="ingredient in getIngredients"
            :key="ingredient.name"
          >
            <td>{{ ingredient.name }}</td>
            <td>{{ ingredient.stock }}</td>
            <td>
              <v-btn
                class="ma-2"
                variant="text"
                icon="mdi-pencil-outline"
                @click="
                  ingredientDialog = true;
                  selectedIngredient = ingredient;
                "
              />
            </td>
          </tr>
        </tbody>
      </v-table>
    </v-card>
    <v-btn
      color="primary"
      text
      class="float-right"
      @click="
        selectedIngredient = {};
        ingredientDialog = true;
        OpenCreateNewIngredientDialog();
      "
    >
      Neue Zutat
    </v-btn>
    <v-dialog v-model="ingredientDialog">
      <v-card>
        <v-card-title>
          {{
            selectedIngredient.ingredientId > 0
              ? "Bearbeite " + selectedIngredient.name
              : "Erstelle " +
                (selectedIngredient.name == undefined
                  ? "..."
                  : selectedIngredient.name)
          }}
        </v-card-title>
        <v-card-text>
          <v-form
            ref="ingredientForm"
            v-model="validIngredientForm"
          >
            <v-row>
              <v-col
                cols="12"
                md="4"
              >
                <v-text-field
                  v-model="selectedIngredient.name"
                  variant="underlined"
                  :rules="[(v) => !!v || 'Name eingeben!']"
                  label="Name"
                  required
                />
              </v-col>

              <v-col
                cols="12"
                md="4"
              >
                <v-text-field
                  v-model="selectedIngredient.stock"
                  variant="underlined"
                  label="Anzahl"
                  :rules="[(v) => !!v || 'Anzahl eingeben!']"
                  type="number"
                  suffix="kg"
                  required
                />
              </v-col>

              <v-col
                cols="12"
                md="4"
              >
                <v-text-field
                  v-model="selectedIngredient.threshold"
                  variant="underlined"
                  label="Schwellwert"
                  :rules="[(v) => !!v || 'Schwellwert eingeben!']"
                  suffix="kg"
                  type="number"
                  required
                />
              </v-col>
            </v-row>
          </v-form>
        </v-card-text>
        <v-card-actions>
          <v-btn
            v-if="selectedIngredient.ingredientId > 0"
            color="error"
            icon="mdi-delete-forever"
            @click="
              deleteIngredient(selectedIngredient.ingredientId);
              ingredientDialog = false;
            "
          />
          <v-spacer />
          <v-btn
            color="primary"
            text
            @click="ingredientDialog = false"
          >
            Abbrechen
          </v-btn>
          <v-btn
            color="primary"
            text
            @click="SaveOrUpdateIngredient()"
          >
            Speichern
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-container>
</template>

<script>
import { useIngredientStore } from "@/stores/ingredient";
import { mapState, mapActions } from "pinia";

export default {
  data: () => ({
    ingredientDialog: false,
    validIngredientForm: false,
    selectedIngredient: null,
    ingredients: [],
  }),

  computed: {
    /**
     * Wird verwendet, damit die funktion aus dem Store in diesem Komponenten verwendet werden kann.
     */
    ...mapState(useIngredientStore, ["getIngredients"]),
  },
  /**
   * Mounted wird am Anfang des vue-Lifecycle aufgerufen. Somit kann vor dem rendern der Page die Funktion
   * fetchIngredient aufgerufen werden.
   */
  async mounted() {
    await this.fetchIngredient();
  },

  methods: {
    OpenCreateNewIngredientDialog: function () {
      this.selectedIngredient = {};
      this.ingredientDialog = true;
    },

    /**
     * Validiert und speichert oder aktualisiert ein Zutatenobjekt, je nachdem, ob es bereits in der Datenbank vorhanden ist oder nicht.
     */
    SaveOrUpdateIngredient: async function () {
      const { valid } = await this.$refs.ingredientForm.validate();
      if (!valid) return;

      await (this.selectedIngredient.ingredientId > 0
        ? this.updateIngredient(this.selectedIngredient)
        : this.saveIngredient(this.selectedIngredient));

      this.ingredientDialog = false;
    },

    /**
     * Wird verwendet, damit die funktionen aus dem Store in diesem Komponenten verwendet werden können.
     */
    ...mapActions(useIngredientStore, [
      "fetchIngredient",
      "saveIngredient",
      "deleteIngredient",
      "updateIngredient",
    ]),
  },
};
</script>

<style></style>
