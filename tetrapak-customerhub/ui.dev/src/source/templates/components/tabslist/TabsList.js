import $ from 'jquery';
import 'bootstrap';
import { logger } from '../../../scripts/utils/logger';

function _renderFirstTab() {
  this.root.find('.js-tablist__events-sidesection').html($('#Event0').html());
}
class TabsList {
  constructor({ el }) {
    this.root = $(el);
  }
  bindEvents() {
    const $this = this;
    this.root
      .on('click', '.js-tablist__event', function () {
        $this.showTabDetail($(this).data('target'));
      });
  }
  showTabDetail = (detailTargetEle) => {
    this.root.find('.js-tablist__events-sidesection').html($(detailTargetEle).html());
  }

  renderFirstTab = () => _renderFirstTab.call(this);

  init() {
    this.bindEvents();
    this.renderFirstTab();
  }
}

export default TabsList;
