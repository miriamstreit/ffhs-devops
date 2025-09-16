/**
  Parst ein JSON Web Token (JWT) und gibt die Nutzdaten als Objekt zurück.
  @param {string} token - Das zu parsende JWT.
  @return {Object} Die Nutzdaten des JWT als Objekt.
*/
export const parseJwt = (token) => {
  var base64Url = token.split(".")[1];
  var base64 = base64Url.replace(/-/g, "+").replace(/_/g, "/");
  var jsonPayload = decodeURIComponent(
    window
      .atob(base64)
      .split("")
      .map(function (c) {
        return "%" + ("00" + c.charCodeAt(0).toString(16)).slice(-2);
      })
      .join("")
  );

  return JSON.parse(jsonPayload);
};
