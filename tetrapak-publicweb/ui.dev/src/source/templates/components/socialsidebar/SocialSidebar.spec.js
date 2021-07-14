import $ from 'jquery';
import SocialSidebar from './SocialSidebar';
import socialsidebarTemplate from '../../../test-templates-hbs/socialsidebar.hbs';

describe('SocialSidebar', function () {
  beforeEach(function () {
    $(document.body).empty().html(socialsidebarTemplate());
    this.sidebar = new SocialSidebar({
      el: document.body
    });
    this.initSpy = sinon.spy(this.sidebar, 'init');
    this.showPopupSpy = sinon.spy(this.sidebar, 'showPopup');
    this.sidebar.root.modal = () => {};
    this.sidebar.init();
  });
  afterEach(function () {
    $(document.body).empty();
    this.initSpy.restore();
    this.showPopupSpy.restore();
  });
  it('should initialize', function (done) {
    expect(this.sidebar.init.called).to.be.true;
    done();
  });
  it('should click of tp-pw-sidebar__link', function (done) {
    $('.tp-pw-sidebar__link').trigger('click');
    done();
  });
  it('should open modal', function (done) {
    $('.tp-pw-sidebar__link').click();
    expect(this.sidebar.showPopup.called).to.be.true;
    done();
  });
  it('should click of js-close-btn', function (done) {
    $('.js-close-btn').trigger('click');
    done();
  });
});
