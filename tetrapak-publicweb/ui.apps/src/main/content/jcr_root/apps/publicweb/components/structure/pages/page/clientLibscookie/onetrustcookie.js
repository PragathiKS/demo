(function () {
  var cookieValue, isCookieExist, oneTrustValue, uidValue, jwtToken;
  function getCookie(name) {
    var value = "; " + document.cookie;
    var parts = value.split("; " + name + "=");
    if (parts.length == 2) return parts.pop().split(";").shift();
    else return null;
  }
  function getCookieValue() {
    var myCookie = getCookie("TPCOOKIEUSER");
    cookieValue = myCookie;
    if (myCookie == null) {
      return false;
    } else {
      return true;
    }
  }

  isCookieExist = getCookieValue();

  function setCookie(cname, cvalue, exdays) {
    var d = new Date();
    var domain = ".tetrapak.com";
    d.setTime(d.getTime() + exdays * 24 * 60 * 60 * 1000);
    let expires = "expires=" + d.toUTCString();
    document.cookie = document.cookie =
      cname +
      "=" +
      cvalue +
      ";expires=" +
      expires +
      ";domain=" +
      domain +
      ";path=/";
  }

  // Url for the request

  oneTrustValue = window.oneTrustValue + "?UserID=" + cookieValue;
  if (isCookieExist === false || localStorage.getItem("jwttokenset") === null) {
    fetch(oneTrustValue)
      .then((response) => {
        return response.json();
      })
      .then((data) => {
        uidValue = data.uid;
        jwtToken = data.jwt;
        localStorage.setItem("uidValueset", uidValue);
        localStorage.setItem("jwttokenset", jwtToken);
        setCookie("TPCOOKIEUSER", uidValue, 365);
        var head = document.getElementsByTagName("head")[0];
        var sc = document.createElement("script");
        sc.setAttribute("type", "text/javascript");
        var string =
          "var OneTrust ={dataSubjectParams:{id :'" +
          uidValue +
          "',isAnonymous: false,token : '" +
          jwtToken +
          "'}}";
        sc.appendChild(document.createTextNode(string));
        head.appendChild(sc);
      })
      .catch(function (error) {
        console.log(error);
      });
  } else {
    var userid = localStorage.getItem("uidValueset");
    var jwt = localStorage.getItem("jwttokenset");
    var head = document.getElementsByTagName("head")[0];
    var sc = document.createElement("script");
    sc.setAttribute("type", "text/javascript");
    var string =
      "var OneTrust = {dataSubjectParams: {id :'" +
      userid +
      "',isAnonymous: false,token : '" +
      jwt +
      "'}}";
    sc.appendChild(document.createTextNode(string));
    head.appendChild(sc);
  }
})();
