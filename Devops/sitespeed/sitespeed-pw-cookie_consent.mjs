export default async function (context, commands) {
    const env = context.options.env;
    const root_url = `https://www-${env}.tetrapak.com`; 
    try {
      await commands.navigate(root_url);
      await commands.click.byIdAndWait('onetrust-accept-btn-handler');
    } catch (e) {
    }
  };