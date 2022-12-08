@ignore
Feature: This feature file contains Custom Karate Methods that can be called and used from other Feature Files

  Scenario:
    * def sleep =
         """
            function(seconds){
              karate.log('Wait started......');
              java.lang.Thread.sleep(seconds*1000);
              karate.log('Wait End.....');
            }
         """

    * def randomNumber =
         """
            function (min, max) {
                 return Math.floor(Math.random() * (max - min + 1) + min)
            }
         """

    * def randomAlphanumericString =
          """
          function(s) {
              var text = "";
              var possible = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
              for (var i = 0; i < s; i++)
              text += possible.charAt(Math.floor(Math.random() * possible.length));
              return text;
          }
          """

    * def randomString =
          """
          function(s) {
              var text = "";
              var possible = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
              for (var i = 0; i < s; i++)
              text += possible.charAt(Math.floor(Math.random() * possible.length));
              return text;
          }
          """

    * def randomDigitString =
          """
          function(s) {
              var text = "";
              var possible = "0123456789";
              for (var i = 0; i < s; i++)
              text += possible.charAt(Math.floor(Math.random() * possible.length));
              return text;
          }
          """

    * def getDate =
         """
            function(pattern) {
              var SimpleDateFormat = Java.type('java.text.SimpleDateFormat');
              var sdf = new SimpleDateFormat(pattern);
              var date = new java.util.Date();
              return sdf.format(date);
            }
         """