import moment from "moment";

/**
  Formatiert ein Datum aus beliebiger Frorm in das Format "YYYY-MM-DD"
  @param {string} date - Datum in beliebiger Form
  @return {string} date - Formatiertes Datum im Format "YYYY-MM-DD"
*/
export const convertToCalendarDate = (date) => {
  return moment(date).format("YYYY-MM-DD");
};

/**
  Formatiert ein Datum aus beliebiger Frorm in das Format "DD.MM.YYYY"
  @param {string} date - Datum in beliebiger Form
  @return {string} date - Formatiertes Datum im Format "DD.MM.YYYY"
*/
export const convertToDisplayDate = (date) => {
  return moment(date).format("DD.MM.YYYY");
};
