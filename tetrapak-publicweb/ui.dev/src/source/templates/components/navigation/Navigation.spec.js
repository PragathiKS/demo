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
});
