import $ from 'jquery';
import Navigation from './Navigation';

describe('Navigation', function() {
  before(function() {
    this.enableTimeouts(false);
    $(document.body)
      .empty()
      .html(
        '<div class="body-content"><div class="sticky-section-menu"><div data-selected-header="Insights"></div></div></div>'
      );
    this.navigation = new Navigation({
      el: document.body
    });
    this.initSpy = sinon.spy(this.navigation, 'init');
    this.navigation.init();
  });
  after(function() {
    $(document.body).empty();
    this.initSpy.restore();
  });
  it('should initialize', function() {
    expect(this.initSpy.called).to.be.true;
  });
});
