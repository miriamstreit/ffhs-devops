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
            <th class="text-left" />
            <th class="text-left" />
          </tr>
        </thead>
        <tbody>
          <tr
            v-for="item in getBeerTypes"
            :key="item.name"
          >
            <td>{{ item.name }}</td>
            <td />
            <td>
              <v-btn
                class="ma-2"
                variant="text"
                icon="mdi-pencil-outline"
                @click="
                  beerTypeDialog = true;
                  selectedBeerType = item;
                "
              />
            </td>
          </tr>
        </tbody>
      </v-table>
      <v-btn
        color="primary"
        text
        class="float-right"
        @click="openCreateOrEditBeerTypetDialog()"
      >
        Neue Biersorte
      </v-btn>
    </v-card>
    <v-dialog v-model="beerTypeDialog">
      <v-card>
        <v-card-title>
          {{
            selectedBeerType.beerTypeId > 0
              ? "Bearbeite " + selectedBeerType.name
              : "Neue Biersorte " +
                (selectedBeerType.name == undefined
                  ? "..."
                  : selectedBeerType.name)
          }}
        </v-card-title>
        <v-card-text>
          <v-form
            ref="beerTypeForm"
            v-model="validBeerTypeForm"
          >
            <v-row>
              <v-col
                cols="12"
                md="4"
              >
                <v-text-field
                  v-model="selectedBeerType.name"
                  variant="underlined"
                  label="Name"
                  :rules="[(v) => !!v || 'Biersorte eingeben!']"
                  required
                />
              </v-col>
            </v-row>
            <v-row>
              <v-col
                cols="12"
                md="4"
              >
                <v-text-field
                  v-model="selectedBeerType.duration"
                  variant="underlined"
                  :rules="[(v) => !!v || 'Dauer angeben!']"
                  label="Brauzeit"
                  required
                />
              </v-col>
            </v-row>
            <v-row>
              <v-col
                cols="12"
                md="4"
              >
                <v-autocomplete
                  v-model="selectedIngredient"
                  label="Neue Zutat hinzufügen"
                  :items="getIngredients"
                  variant="underlined"
                  item-title="name"
                  return-object
                />
              </v-col>
            </v-row>
            <v-row>
              <v-col
                vcols="12"
                md="4"
              >
                <v-chip
                  v-for="(ingredient, i) in selectedBeerType.ingredients"
                  :key="ingredient.ingredientId"
                  class="mr-5 mt-5"
                  label
                >
                  {{
                    `${ingredient.ingredient.name} (${ingredient.amount} kg.)`
                  }}
                  <v-btn
                    icon="mdi-close"
                    size="x-small"
                    class="close-button"
                    color="black"
                    @click="selectedBeerType.ingredients.splice(i, 1)"
                  />
                </v-chip>
              </v-col>
            </v-row>
          </v-form>
        </v-card-text>
        <v-card-actions>
          <v-btn
            v-if="selectedBeerType.beerTypeId > 0"
            color="error"
            icon="mdi-delete-forever"
            @click="
              deleteBeerType(selectedBeerType.beerTypeId);
              beerTypeDialog = false;
            "
          />
          <v-spacer />
          <v-btn
            color="primary"
            text
            @click="beerTypeDialog = false"
          >
            Abbrechen
          </v-btn>
          <v-btn
            color="primary"
            text
            @click="saveOrEditBeerType()"
          >
            Speichern
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
    <v-dialog v-model="beerTypeIngredientDialog">
      <v-card>
        <v-card-title>{{ selectedIngredient?.name }}</v-card-title>
        <v-card-text>
          <v-form v-model="validBeerTypeIngredientForm">
            <v-row>
              <v-col
                cols="12"
                md="4"
              >
                <div v-if="selectedIngredient !== null">
                  <v-text-field
                    v-model="selectedIngredient.amount"
                    variant="underlined"
                    label="Anzahl (kg.)"
                    hide-no-data="true"
                    required
                  />
                </div>
              </v-col>
            </v-row>
          </v-form>
        </v-card-text>
        <v-card-actions>
          <v-spacer />
          <v-btn
            color="primary"
            text
            @click="beerTypeIngredientDialog = false"
          >
            Abbrechen
          </v-btn>
          <v-btn
            color="primary"
            text
            @click="addIngredientToBeerType()"
          >
            Speichern
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-container>
</template>

<script>
import { useBeerTypeStore } from "@/stores/beerType";
import { useIngredientStore } from "@/stores/ingredient";
import { mapState, mapActions } from "pinia";

export default {
  data: () => ({
    beerTypeDialog: false,
    beerTypeIngredientDialog: false,
    validBeerTypeForm: false,
    validBeerTypeIngredientForm: false,
    selectedBeerType: null,
    selectedIngredient: null,
  }),

  computed: {
    /**
     * Wird verwendet, damit die funktionen aus dem Store in diesem Komponenten verwendet werden können.
     */
    ...mapState(useBeerTypeStore, ["getBeerTypes"]),
    ...mapState(useIngredientStore, ["getIngredients"]),
  },

  /**
   * Ein watcher auf selectedIngredient. Wenn sich der Wert ändert, wird die Funktion onIngredientClick
   * aufgerufen mit dem geänderten Wert.
   * Bei der Initialisierung ist der Wert undefiend oder null, weshalb dies hier explizit ausgeschlossen wird
   */
  watch: {
    selectedIngredient: function (selectedIngredient) {
      if (selectedIngredient !== undefined || selectedIngredient !== null)
        this.onIngredientClick(selectedIngredient);
    },
  },

  /**
   * Mounted wird am Anfang des vue-Lifecycle aufgerufen. Somit kann vor dem rendern der Page die Funktion
   * fetchIngredient aufgerufen werden.
   */
  async mounted() {
    await this.fetchBeerTypes();
  },
  
  methods: {
    /**
     * Validiert und speichert oder aktualisiert eine Biersorte, je nachdem, ob es bereits in der Datenbank vorhanden ist oder nicht.
     */
    saveOrEditBeerType: async function () {
      const { valid } = await this.$refs.beerTypeForm.validate();
      if (!valid) return;

      await (this.selectedBeerType.beerTypeId > 0
        ? this.updateBeerType(this.selectedBeerType)
        : this.saveBeertype(this.selectedBeerType));

      this.beerTypeDialog = false;
    },

    openCreateOrEditBeerTypetDialog: function () {
      this.selectedBeerType = { ingredients: [] };
      this.beerTypeDialog = true;
    },

    /**
     * Fügt die selektierte Zutat einem Brauprozess hinzu.
     * Damit das Objekt mit dem Datenmodell übereinstimmt, muss amount aus ingredient
     * gelöscht werden
     */
    addIngredientToBeerType: function () {
      let ingredientAmount = {
        amount: this.selectedIngredient.amount,
        ingredient: this.selectedIngredient,
      };

      delete ingredientAmount.ingredient.amount;

      this.selectedBeerType.ingredients.push(ingredientAmount);

      this.beerTypeIngredientDialog = false;
      this.selectedIngredient = null;
    },

    /**
     * Wird aufgerufen bei einem Klick auf eine Zutat aus dem Dropdown.
     * Öffnet den IngredientDialog
     */
    onIngredientClick: function (ingredient) {
      if (ingredient?.ingredientId > 0) {
        this.beerTypeIngredientDialog = true;
      }
    },

    /**
     * Wird verwendet, damit die funktionen aus dem Store in diesem Komponenten verwendet werden können.
     */
    ...mapActions(useBeerTypeStore, [
      "fetchBeerTypes",
      "saveBeertype",
      "updateBeerType",
      "deleteBeerType",
    ]),
  },
};
</script>

<style>
.close-button {
  height: 18px !important;
  width: 18px !important;
  margin-left: 8px;
}
</style>
