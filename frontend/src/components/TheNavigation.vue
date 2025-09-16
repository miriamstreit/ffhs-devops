<!--
  Globla Navigation, welche immer am unteren Rand des Bildschirms sichbar ist.
-->
<template>
  <v-bottom-navigation>
    <v-btn
      value="home"
      to="/"
    >
      <v-icon>mdi-home</v-icon>
      Home
    </v-btn>

    <v-btn
      value="calendar"
      to="/calendar"
    >
      <v-icon>mdi-calendar</v-icon>
      Kalender
    </v-btn>

    <v-btn
      value="inventory"
      to="/inventory"
    >
      <v-icon>mdi-database</v-icon>
      Inventar
    </v-btn>

    <v-btn
      value="canBrew"
      to="/canBrew"
    >
      <v-icon>mdi-beer</v-icon>
      CanBrew
    </v-btn>
  </v-bottom-navigation>
</template>

<style scoped>
.v-btn--active i {
  color: rgb(240, 196, 15);
}
</style>
