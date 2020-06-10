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
    $('body').append( '<a class=\'js-tp-pw-header-logo-digital-data js-tp-pw-header-item js-tp-pw-header-item-desktop\' data-link-section=\'Header Menu Navigation\' data-link-name=\'Insights\'>Insights</a>' );
    $('body').attr('style','overflow : auto');
    this.initSpy = sinon.spy(this.navigation, 'init');
    this.sectionMenuToggleClickSpy = sinon.spy(
      this.navigation,
      'sectionMenuToggleClick'
    );
    this.navigation.init();
  });
  after(function() {
    $(document.body).empty();
    this.initSpy.restore();
    this.sectionMenuToggleClickSpy.restore();
  });
  it('should initialize', function() {
    expect(this.navigation.init.called).to.be.true;
  });
  it('should call sectionMenuToggleClick on click', function() {
    $('.collapse-button').trigger('click');
    expect(this.navigation.sectionMenuToggleClick.called).to.be.true;
  });
  it('add class overflow auto in body when section menu closed ', function() {
    $('.collapse-button').removeClass('collapsed');
    $('.collapse-button').trigger('click');
    expect(document.body.style.overflow).to.equal('auto');
  });
});
