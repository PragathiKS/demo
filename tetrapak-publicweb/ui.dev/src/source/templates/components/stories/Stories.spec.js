import Stories from "./Stories";
import $ from "jquery";
import { ajaxWrapper } from "../../../scripts/utils/ajax";
import storiesTemplate from "../../../test-templates-hbs/stories.hbs";
import storiesData from "./data/stories.json";

describe("Stories", function () {
  const jqRef = {
    setRequestHeader() {
      // Dummy Method
    },
  };
  function ajaxResponse(response) {
    const pr = $.Deferred();
    pr.resolve(response, "success", jqRef);
    return pr.promise();
  }

  before(function () {
    this.stories = new Stories({
      el: document.body,
    });
    $(document.body).empty().html(storiesTemplate());
    this.stories = new Stories({
      el: document.body,
    });
    this.initSpy = sinon.spy(this.stories, "init");
    this.handleShowMoreSpy = sinon.spy(this.stories, "handleShowMore");
    this.ajaxStub = sinon.stub(ajaxWrapper, 'getXhrObj');
    this.ajaxStub.returns(ajaxResponse(storiesData));
    this.stories.init();
  });
  after(function () {
    $(document.body).empty();
    this.initSpy.restore();
    this.ajaxStub.restore();
    this.handleShowMoreSpy.restore();
  });
  it('should initialize', function(done) {
    expect(this.stories.init.called).to.be.true;
    done();
  });
  it('should show more stories click of show more button', function (done) {
    $('.pw-stories__load-more').trigger('click');
    expect(this.stories.handleShowMore.called).to.be.true;
    done();
  });
});
