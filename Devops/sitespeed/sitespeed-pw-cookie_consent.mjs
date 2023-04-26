export default async function (context, commands) {
    await commands.navigate(
      context.options.test.url
    );
  
    try {
      await commands.click.byIdAndWait('onetrust-accept-btn-handler');
    } catch (e) {
    }
  };