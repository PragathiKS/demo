export default async function (context, commands) {
    await commands.navigate(
      context.options.test.url
    );
  
    try {
      await commands.addText.byId("aemext099", "username");
      await commands.addText.byId("T3traP@kr0ck$", "password");
      await commands.click.byIdAndWait('signInButton', 2000); 
      await commands.wait.bySelector('.tp-cookie-consent__btn', 2000);
      await commands.click.bySelectorAndWait('.tp-cookie-consent__btn', 1000);
    } catch (e) {
    }
  };