import moment from "moment";
import { convertToCalendarDate } from "@/helpers/dateHelper";

/**
  Konvertiert eine Liste von Brauprozessen in Kalendereinträge.
  @param {Array} brewingProcesses - Die Liste der zu konvertierenden Brauprozesse. Jedes Element sollte folgende Eigenschaften haben:
    brewingId {string} - Die ID des Brauprozesses.
    beerType {Object} - Ein Objekt, das die Art des gebrauten Biers repräsentiert. Es sollte eine Eigenschaft "name" haben.
    startDate {Date} - Das Startdatum des Brauprozesses.
    endDate {Date} - Das Enddatum des Brauprozesses.
    color {string} - Die für den Kalendereintrag zu verwendende Farbe.
  @return {Array} calendarEntries - Die Liste der aus den angegebenen Brauprozessen erstellten Kalendereinträge. Jedes Element wird folgende Eigenschaften haben:
    brewingId {string} - Die ID des Brauprozesses.
    title {string} - Der Name der Bierart, die gebraut wird. Wenn kein Biertyp angegeben wurde, lautet der Titel "Not linked" (Nicht verknüpft).
    start {Date} - Das Startdatum des Brauprozesses in einem für den Gebrauch in einem Kalender geeigneten Format.
    end {Date} - Das Enddatum des Brauprozesses in einem für den Gebrauch in einem Kalender geeigneten Format.
    backgroundColor {string} - Die für den Kalendereintrag zu verwendende Farbe.
    borderColor {string} - Die für den Rand des Kalendereintrags zu verwendende Farbe.
*/
export const convertBrewingProcessesToCalendarEntry = (brewingProcesses) => {
  let calendarEntries = [];

  brewingProcesses?.forEach((brewingProcess) => {
    let calendarEntry = {};

    calendarEntry.brewingId = brewingProcess.brewingId;
    calendarEntry.title = brewingProcess.beerType?.name ?? "Not linked";
    calendarEntry.start = convertToCalendarDate(brewingProcess.startDate);
    calendarEntry.end = convertToCalendarDate(brewingProcess.endDate);
    calendarEntry.backgroundColor = brewingProcess.color;
    calendarEntry.borderColor = brewingProcess.color;

    calendarEntries.push(calendarEntry);
  });

  return calendarEntries;
};

/**
  Konvertiert einen Kalendereintrag in einen Brauprozess.
  @param {Object} calendarEntry - Der zu konvertierende Kalendereintrag. Er sollte folgende Eigenschaften haben:
    brewingId {string} - Die ID des Brauprozesses.
    beerType {Object} - Ein Objekt, das die Art des gebrauten Biers repräsentiert. Es sollte folgende Eigenschaften haben:
    name {string} - Der Name der Bierart.
    duration {number} - Die Anzahl der Tage, die der Brauprozess voraussichtlich dauern wird.
    tank {Object} - Ein Objekt, das den für den Brauprozess verwendeten Tank repräsentiert. Es sollte eine Eigenschaft "name" haben.
    startDate {Date} - Das Startdatum des Brauprozesses.
    sudNumber {number} - Die Chargennummer des Brauprozesses.
    comment {string} - Ein Kommentar zum Brauprozess.
    color {string} - Die für den Brauprozess zu verwendende Farbe.
  @return {Object} brewingProcess - Der aus dem angegebenen Kalendereintrag erstellte Brauprozess. Er wird folgende Eigenschaften haben:
    brewingId {string} - Die ID des Brauprozesses.
    beerType {Object} - Ein Objekt, das die Art des gebrauten Biers repräsentiert. Es wird folgende Eigenschaften haben:
    name {string} - Der Name der Bierart.
    duration {number} - Die Anzahl der Tage, die der Brauprozess voraussichtlich dauern wird.
    tank {Object} - Ein Objekt, das den für den Brauprozess verwendeten Tank repräsentiert. Es wird eine Eigenschaft "name" haben.
    startDate {Date} - Das Startdatum des Brauprozesses.
    endDate {Date} - Das Enddatum des Brauprozesses, berechnet als das Startdatum plus die Dauer des Brauprozesses.
    sudNumber {number} - Die Chargennummer des Brauprozesses.
    comment {string} - Ein Kommentar zum Brauprozess.
    color {string} - Die für den Brauprozess zu verwendende Farbe.
*/
export const convertCalendarEntryToBrewingProcess = (calendarEntry) => {
  let brewingProcess = { beerType: {} };

  brewingProcess.brewingId = calendarEntry.brewingId;
  brewingProcess.beerType = calendarEntry.beerType;
  brewingProcess.tank = calendarEntry.tank;
  brewingProcess.startDate = calendarEntry.startDate;
  brewingProcess.endDate = moment(calendarEntry.startDate)
    .add(calendarEntry.beerType.duration, "days")
    .format("YYYY-MM-DD");
  brewingProcess.sudNumber = calendarEntry.sudNumber;
  brewingProcess.comment = calendarEntry.comment;
  brewingProcess.color = calendarEntry.color;

  return brewingProcess;
};
