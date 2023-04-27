export default async function (context, commands) {
    const env = context.options.test.env;
    const root_url = `https://mypages-${env}.tetrapak.com`; 
    try {
      await commands.navigate(root_url);
      await commands.addText.byId(context.options.uname, "username");
      await commands.addText.byId(context.options.upass, "password");
      await commands.click.byIdAndWait('signInButton', 2000);
      if(env == 'dev' || env == 'qa') {
        await commands.click.byIdAndWait('onetrust-accept-btn-handler', 1000);
      } else  {
        await commands.wait.bySelector('.tp-cookie-consent__btn', 2000);
        await commands.click.bySelectorAndWait('.tp-cookie-consent__btn', 1000);
      }
    } catch (e) {
      throw e
    }
  };