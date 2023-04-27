export default async function (context, commands) {
    // const env = context.options.test.env;
    // const root_url = `https://mypages-${env}.tetrapak.com`; 
    try {
      // await commands.navigate(root_url);
      // await commands.click.click.byLinkTextAndWait('Logout', 3000); 
      await commands.cache.clear();
    } catch (e) {
    }
  };