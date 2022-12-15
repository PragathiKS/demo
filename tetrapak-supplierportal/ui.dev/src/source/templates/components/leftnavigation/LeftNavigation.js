import $ from 'jquery';
import LeftNavigationParent from '../../../../../../../tetrapak-customerhub/ui.dev/src/source/templates/components/leftnavigation/LeftNavigation';

let parent;
class Leftnavigation extends LeftNavigationParent {
  constructor({ el }) {
    parent = super({ el });
    this.root = $(el);
  }
  bindEvents() {
    super.bindEvents();
    $(document.body).on('hideleftnav',parent.closeSideNav);
  }

  init() {
    super.init();
    this.bindEvents();
  }
}

export default Leftnavigation;