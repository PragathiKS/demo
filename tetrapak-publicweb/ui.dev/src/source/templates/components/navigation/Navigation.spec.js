import $ from 'jquery';
import Navigation from './Navigation';
import navigationTemplate from '../../../test-templates-hbs/navigation.hbs';

describe('Navigation', function() {
  before(function() {
    this.enableTimeouts(false);
    $(document.body)
      .empty()
      .html(navigationTemplate());
    this.navigation = new Navigation({
      el: document.body
    });
    this.initSpy = sinon.spy(this.navigation, 'init');
    this.openMobileSectionMenuSpy = sinon.spy(
      this.navigation,
      'openMobileSectionMenu'
    );
    this.navigation.init();
  });
  after(function() {
    $(document.body).empty();
    this.initSpy.restore();
    this.openMobileSectionMenuSpy.restore();
  });
  it('should initialize', function() {
    expect(this.navigation.init.called).to.be.true;
  });
  it('should call openMobileSectionMenu on click', function() {
    $('.collapse-button').trigger('click');
    expect(this.navigation.openMobileSectionMenu.called).to.be.true;
  });
});
