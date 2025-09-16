<template>
  <v-container>
    <v-row align-content="center">
      <v-col><h1>Calendar</h1></v-col>
    </v-row>
    <br>
    <v-row>
      <FullCalendar
        ref="fullCalendar"
        class="calendar__full-calendar"
        :options="calendarOptions"
      />
    </v-row>
    <v-dialog v-model="calendarEntryDialog">
      <v-card>
        <v-card-title>
          {{
            selectedEntry.biersorte == undefined
              ? "Neuer Eintrag ab " + selectedEntry.displayDate
              : "Bearbeite " + selectedEntry.biersorte
          }}
        </v-card-title>
        <v-card-text>
          <v-form
            ref="form"
            v-model="valid"
            lazy-validation
          >
            <v-row>
              <v-col
                cols="12"
                md="4"
              >
                <v-select
                  v-model="selectedEntry.beerType"
                  label="Biersorte"
                  item-title="name"
                  variant="underlined"
                  return-object
                  :items="getBeerTypes"
                  :rules="[(v) => !!v || 'Biersorte auswählen!']"
                />
              </v-col>
              <v-col
                cols="12"
                md="4"
              >
                <v-select
                  v-model="selectedEntry.tank"
                  label="Tank"
                  item-title="tankId"
                  variant="underlined"
                  item-value="tankId"
                  return-object
                  :rules="[(v) => !!v || 'Tank auswählen!']"
                  :items="getTanks"
                />
              </v-col>
              <v-col
                cols="12"
                md="4"
              >
                <v-text-field
                  v-model="selectedEntry.sudNumber"
                  variant="underlined"
                  label="Sud Nummer"
                  :rules="[(v) => !!v || 'Sudnummer eingeben!']"
                />
              </v-col>
              <v-col
                cols="12"
                md="4"
              />
              <v-col
                cols="12"
                md="4"
              >
                <v-select
                  v-model="selectedEntry.color"
                  label="Farbe"
                  item-title="label"
                  variant="underlined"
                  item-value="hex"
                  :rules="[(v) => !!v || 'Farbe auswählen!']"
                  :items="colors"
                />
              </v-col>
              <v-col
                cols="12"
                md="4"
              >
                <v-textarea
                  v-model="selectedEntry.comment"
                  variant="underlined"
                  label="Anmerkung"
                  append-inner-icon="mdi-comment"
                  rows="2"
                />
              </v-col>
            </v-row>
          </v-form>
        </v-card-text>
        <v-card-actions>
          <v-btn
            v-if="selectedEntry.brewingId > 0"
            color="error"
            icon="mdi-delete-forever"
            @click="handleDeleteBrewingProcess()"
          />
          <v-spacer />
          <v-btn
            color="primary"
            text
            @click="
              calendarEntryDialog = false;
              selectedEntry = {};
            "
          >
            Abbrechen
          </v-btn>
          <v-btn
            color="primary"
            text
            @click="createOrUpdateCalenadrEntry()"
          >
            Speichern
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-container>
</template>

<script>
import "@fullcalendar/core/vdom"; // solves problem with Vite
import FullCalendar from "@fullcalendar/vue3";
import dayGridPlugin from "@fullcalendar/daygrid";
import interactionPlugin from "@fullcalendar/interaction";

import { mapState, mapActions } from "pinia";

import { useBrewingProcessStore } from "@/stores/brewingProcess";
import { useTankStore } from "@/stores/tank";
import { useBeerTypeStore } from "@/stores/beerType";

import {
  convertBrewingProcessesToCalendarEntry,
  convertCalendarEntryToBrewingProcess,
} from "@/helpers/brewingProcessHelpers";

import {
  convertToCalendarDate,
  convertToDisplayDate,
} from "@/helpers/dateHelper";

export default {
  components: {
    FullCalendar,
  },
  data() {
    return {
      calendarOptions: {
        plugins: [dayGridPlugin, interactionPlugin],
        initialView: "dayGridMonth",
        dateClick: this.handleDateClick,
        eventClick: this.handleEntryClick,
        events: this.getCalendarEntries(),
        headerToolbar: {
          left: "prev",
          center: "title",
          right: "next",
        },
      },
      colors: [
        { label: "Rot", hex: "#e53935" },
        { label: "Blau", hex: "#3949ab" },
        { label: "Orange", hex: "#fb8c00" },
        { label: "Pink", hex: "#d81b60" },
        { label: "Grün", hex: "#43a047" },
      ],
      calendarEntryDialog: false,
      valid: true,
      selectedEntry: {},
    };
  },

  /**
   * Wird verwendet, damit die funktion aus dem Store in diesem Komponenten verwendet werden kann.
   */
  computed: {
    ...mapState(useBrewingProcessStore, ["getBrewingProcess"]),
    ...mapState(useTankStore, ["getTanks"]),
    ...mapState(useBeerTypeStore, ["getBeerTypes"]),
  },

  /**
   * Mounted wird am Anfang des vue-Lifecycle aufgerufen. Somit kann vor dem rendern der Page alle unten
   * stehende Funktionen aufgerufen werden.
   */
  async mounted() {
    await this.fetchBrewingProcess();
    await this.fetchBeerTypes();
    await this.fetchTanks();

    this.refetchCalendar();
  },

  methods: {
    /**
      Validiert und erstellt oder editiert einen Kalendereintrag.
      Schliesst anschliessend den Dialog und aktualisiert die Kalendereinträge 
    */
    createOrUpdateCalenadrEntry: async function () {
      const { valid } = await this.$refs.form.validate();
      if (!valid) return;

      let payload = convertCalendarEntryToBrewingProcess(this.selectedEntry);

      await (this.selectedEntry.brewingId > 0
        ? this.updateBrewingProcess(payload)
        : this.saveBrewingProcess(payload));

      this.calendarEntryDialog = false;
      this.selectedEntry = {};

      this.refetchCalendar();
    },

    /**
      Wird aufgerufen beim Click aufg ein leeres Datum. Setzt Startdatum und Display Datum und
      öffnet den Calendar Entry Dialog
    */
    handleDateClick: function (DateSelectArg) {
      this.selectedEntry = {
        startDate: convertToCalendarDate(DateSelectArg.date),
        displayDate: convertToDisplayDate(DateSelectArg.date),
      };

      this.calendarEntryDialog = true;
    },

    /**
      Wird aufgerufen beim Click auf einen Kalendereintrag. Als selectedEntry wird dann der Brauprozess
      gesucht und in selectedEntry abgespeichert.
      Anschliessend wir dder CalendarEntry Dialog geöffnet
    */
    handleEntryClick: function (EntrySelectedArg) {
      let selectedBrewingId = EntrySelectedArg.event.extendedProps.brewingId;

      this.selectedEntry = this.getBrewingProcess.find(
        (x) => x.brewingId == selectedBrewingId
      );

      this.calendarEntryDialog = true;
    },

    handleDeleteBrewingProcess: async function () {
      await this.deleteBrewingProcess(this.selectedEntry.brewingId);
      this.calendarEntryDialog = false;

      this.refetchCalendar();
    },

    /**
      In einem successCallback werden die konvertierten Kalendereinträge zurückgeben.
      Grund für den successCallback ist, dass FullCalendar dies explizit so wünscht.
    */
    getCalendarEntries: function () {
      return (calendarInfo, successCallback) => {
        if (this.getBrewingProcess != null) {
          successCallback(
            convertBrewingProcessesToCalendarEntry(this.getBrewingProcess)
          );
        }
      };
    },

    refetchCalendar: function () {
      let calendarApi = this.$refs.fullCalendar.getApi();
      calendarApi.refetchEvents();
    },

    /**
     * Wird verwendet, damit die funktionen aus dem Store in diesem Komponenten verwendet werden können.
     */
    ...mapActions(useBrewingProcessStore, [
      "fetchBrewingProcess",
      "saveBrewingProcess",
      "deleteBrewingProcess",
      "updateBrewingProcess",
    ]),
    ...mapActions(useTankStore, ["fetchTanks"]),
    ...mapActions(useBeerTypeStore, ["fetchBeerTypes"]),
  },
};
</script>

<style scoped>
.calendar__full-calendar {
  height: 70vh;
  width: 100%;
}
</style>
