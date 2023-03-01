import { logger } from './logger';

export default {
  init() {
    // Include site improve script
    const siteImproveScript = $.trim($('#siteImproveScript').val());
    if (siteImproveScript) {
      const allScripts = $('script');
      allScripts.eq(allScripts.length - 1).after(`<script type="text/plain" src="${siteImproveScript}" class="optanon-category-2" async="true"></script>`);
      logger.log('[Webpack]: Siteimprove script added');
    }
  }
};
