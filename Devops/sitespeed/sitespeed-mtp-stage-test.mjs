export default async function (context, commands) {
    try {
      await commands.measure.start('https://mypages-stage.tetrapak.com/dashboard.html')
      await commands.measure.start('https://mypages-stage.tetrapak.com/package-design/introduction.html');

      await commands.measure.start('MTP_Equipments');
      await commands.navigate('https://mypages-stage.tetrapak.com/installed-equipments/equipments.html')
      await commands.wait.byTime(5000)
      await commands.measure.stop()

      await commands.measure.start('MTP_Rebuilding_Kit');
      await commands.navigate('https://mypages-stage.tetrapak.com/installed-equipments/rebuilding-kits.html')
      await commands.wait.byTime(5000)
      await commands.measure.stop()

      
      await commands.measure.start('MTP_Financial');
      await commands.navigate('https://mypages-stage.tetrapak.com/financials.html')
      await commands.wait.byTime(5000)
      return commands.measure.stop()

    } catch (e) {
      throw e;
    }
  };