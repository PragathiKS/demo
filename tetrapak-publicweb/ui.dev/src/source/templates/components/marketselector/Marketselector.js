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
      .on('showlanuagepreferencepopup-pw', this.showPopup);
    $marketSelectorList.on('click', this.trackMarketSelectorAnalytics);
  }

    trackMarketSelectorAnalytics = (e) => {
      e.preventDefault();
      const $target = $(e.currentTarget);
      const $this = $(e.currentTarget).text().trim();
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
        if (e.metaKey || e.ctrlKey || e.keyCode === 91 || e.keyCode === 224){
          window.open(url,'_blank');
        }
        else {
          window.open(url,'_self');
        }
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
