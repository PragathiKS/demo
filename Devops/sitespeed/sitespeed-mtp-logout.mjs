export default async function (context, commands) {
    await commands.navigate(
      context.options.test.url
    );
  
    try {
      await commands.click.click.byLinkTextAndWait('Logout', 3000); 
      await commands.cache.clear();
    } catch (e) {
    }
  };