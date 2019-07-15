import $ from 'jquery';
import 'bootstrap';

function _renderFirstTab() {
  this.root.find('.js-tablist__events-sidesection').html($('#Event0').html());
}
class TabsList {
  constructor({ el }) {
    this.root = $(el);
  }
  cache = {};
  initCache() {
    /* Initialize selector cache here */
    /**
     * If component is added more than once please use "this.root" hook to prevent overlap issues.
     * Example:
     * this.cache.$submitBtn = this.root.find('.js-submit-btn');
     */
  }
  bindEvents() {
    const $this = this;
    this.root
      .on('click', '.js-tablist__event', function () {
        let detailTargetEle = $(this).data('target');
        $this.root.find('.js-tablist__events-sidesection').html($(detailTargetEle).html());
      });
  }
  renderFirstTab = () => _renderFirstTab.call(this);

  init() {
    this.initCache();
    this.bindEvents();
    this.renderFirstTab();
  }
}

export default TabsList;
