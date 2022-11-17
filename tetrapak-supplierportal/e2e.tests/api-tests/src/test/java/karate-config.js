function fn() {
  var env = karate.env; // get system property 'karate.env'
  karate.log('karate.env system property was:', env);
  if (!env) {
    env = 'dev';
  }

  var appConfig = read("classpath:config/" + env + "-config.json")

  var config = {
    url: appConfig.url,
    env: env,
    appConfig: appConfig
  }

  karate.configure('connectTimeout', 60000);
  karate.configure('readTimeout', 60000);
  karate.configure('ssl', true);
  karate.configure('charset', null);

  return config;
}