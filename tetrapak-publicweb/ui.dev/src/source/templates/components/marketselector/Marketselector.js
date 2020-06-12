import $ from 'jquery';
import 'bootstrap';
import { trackAnalytics } from '../../../scripts/utils/analytics';

class Marketselector {
  constructor({ el }) {
    this.root = $(el);
  }
  cache = {};
  initCache() {
    this.cache.$modal = this.root.parent().find('.js-lang-modal');
    this.cache.$marketSelectorList= this.root.find('.js-lang-selector__btn > a');
  }
  bindEvents() {
    const { $marketSelectorList} = this.cache;
    this.root.on('click', '.js-close-btn', this.hidePopUp)
      .on('click', function () {
        if ($(this).hasClass('js-lang-modal')) {
          this.hidePopUp;
        }
      })
      .on('showlanuagepreferencepopup-pw', this.showPopup);
    $marketSelectorList.on('click', this.trackMarketSelectorAnalytics);
  }

    trackMarketSelectorAnalytics = (e) => {
      e.preventDefault();
      const $target = $(e.target);
      const $this = $(e.target).text().trim();
      const marketlink = $this;
      const url = $target.attr('href');
      const linkType = $target.attr('target') === '_blank'? 'external' :'internal';


      const trackingObj = {
        linkType,
        linkSection: 'Hyperlink click',
        linkParentTitle:'',
        linkName: marketlink
      };

      const eventObj = {
        eventType: 'linkClick',
        event: 'Market Selector'
      };

      trackAnalytics(trackingObj, 'linkClick', 'linkClick', undefined, false, eventObj);
      if(url){
        window.open(url);
      }
    }

  showPopup = () => {
    const $this = this;
    const { $modal } = $this.cache;
    $modal.modal();
  }

  hidePopUp = () => {
    const $this = this;
    $this.root.modal('hide');
  }

  init() {
    /* Mandatory method */
    this.initCache();
    this.bindEvents();
    // this.showPopup(true);
  }
}

export default Marketselector;
