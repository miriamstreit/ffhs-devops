/**
  Zeigt eine Snackbar mit den angegebenen Eigenschaften an.
  @param {Object} props - Die Eigenschaften, die für die Snackbar verwendet werden sollen:
    message {string} - Die Nachricht, die in der Snackbar angezeigt werden soll.
    timer {number} - Die Anzahl der Millisekunden, die die Snackbar angezeigt wird, bevor sie verschwindet.
    color {string} - Material Colors zB "error", "warning"
    icon {string} - Material Icon Code für das Icon zB "mdi-check"
*/
export const showSnackbar = (props) => {
  window.app.snackbar.show(props);
};
