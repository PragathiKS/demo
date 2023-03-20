import $ from "jquery";
import DatePicker from './DatePicker';
import datePickerTmpl from "../../../test-templates-hbs/datePicker.hbs";
import { WrongInitDateError } from "./constants";



describe('DatePicker', function () {
  before(function() {
    this.init = (args={}, useDefaultArgs=true) => {
      const MOCK_MIN_DATE = new Date('1900-01-01');
      const MOCK_MAX_DATE = new Date('2100-01-01');

      $(document.body).empty().html(datePickerTmpl());

      const defaultArgs = {
        el: $('.js-dp'),
        minDate: MOCK_MIN_DATE, 
        maxDate: MOCK_MAX_DATE,
      }
      
      this.dp = new DatePicker({
        ...(useDefaultArgs ? defaultArgs : {}),
        ...args
      });
      
      this.initSpy = sinon.spy(this.dp, "init");
      this.bindEventsSpy = sinon.spy(this.dp, "bindEvents");
      this.initValueSpy = sinon.spy(this.dp, "initValue");
      this.callDatesSpy = sinon.spy(this.dp, "callDates");

      this.dp.init();
    }
  });
  after(function () {
    $(document.body).empty();
    this.initSpy.restore();
    this.bindEventsSpy.restore();
  });
  it("should initialize", function (done) {
    this.init({}, false);

    expect(this.dp.init.called).to.be.true;
    expect(this.dp.bindEvents.called).to.be.true;
    expect(this.dp.cache).to.be.an('object');
    expect(this.dp.cache).to.have.property('widgetSelectorId');
    expect(this.dp.cache).to.have.property('inputsWrapperSelectorId');
    expect(this.dp.cache).to.have.property('inputFromWrapperSelectorId');
    expect(this.dp.cache).to.have.property('inputFromLabelSelectorId');
    expect(this.dp.cache).to.have.property('inputToWrapperSelectorId');
    expect(this.dp.cache).to.have.property('inputToLabelSelectorId');
    expect(this.dp.cache).to.have.property('inputFromSelectorId');
    expect(this.dp.cache).to.have.property('inputToSelectorId');
    expect(this.dp.cache).to.have.property('fromDateClearIconSelectorId');
    expect(this.dp.cache).to.have.property('toDateClearIconSelectorId');
    expect(this.dp.cache).to.have.property('inputFromErrorSelectorId');
    expect(this.dp.cache).to.have.property('inputToErrorSelectorId');
    expect(this.dp.cache).to.have.property('monthIconSelectorId');
    expect(this.dp.cache).to.have.property('yearIconSelectorId');

    expect($(`.${this.dp.cache['widgetSelectorId']}`)).to.have.lengthOf(1);
    expect($(`.${this.dp.cache['inputsWrapperSelectorId']}`)).to.have.lengthOf(1);
    expect($(`.${this.dp.cache['inputFromWrapperSelectorId']}`)).to.have.lengthOf(1);
    expect($(`.${this.dp.cache['inputFromLabelSelectorId']}`)).to.have.lengthOf(1);
    expect($(`.${this.dp.cache['inputToWrapperSelectorId']}`)).to.have.lengthOf(0);
    expect($(`.${this.dp.cache['inputToLabelSelectorId']}`)).to.have.lengthOf(0);
    expect($(`.${this.dp.cache['inputFromSelectorId']}`)).to.have.lengthOf(1);
    expect($(`.${this.dp.cache['inputToSelectorId']}`)).to.have.lengthOf(0);
    expect($(`.${this.dp.cache['fromDateClearIconSelectorId']}`)).to.have.lengthOf(1);
    expect($(`.${this.dp.cache['toDateClearIconSelectorId']}`)).to.have.lengthOf(0);
    expect($(`.${this.dp.cache['inputFromErrorSelectorId']}`)).to.have.lengthOf(0);
    expect($(`.${this.dp.cache['inputToErrorSelectorId']}`)).to.have.lengthOf(0);
    expect($(`.${this.dp.cache['monthIconSelectorId']}`)).to.have.lengthOf(1);
    expect($(`.${this.dp.cache['yearIconSelectorId']}`)).to.have.lengthOf(1);
    done();
  });
  it("should initialize with 'range' type", function (done) {
    this.init({ type: 'range' });

    expect($(`.${this.dp.cache['inputToWrapperSelectorId']}`)).to.have.lengthOf(1);
    expect($(`.${this.dp.cache['inputToLabelSelectorId']}`)).to.have.lengthOf(1);
    expect($(`.${this.dp.cache['inputToSelectorId']}`)).to.have.lengthOf(1);

    done();
  });
  it("should initialize with values", function (done) {
    const MOCK_START_DATE = '2000-01-01';
    const MOCK_END_DATE = '2000-02-01';

    this.init({ 
      type: 'range', 
      startDate: MOCK_START_DATE, 
      endDate: MOCK_END_DATE
    });

    expect($(`.${this.dp.cache['inputFromSelectorId']}`).val()).to.equal(MOCK_START_DATE);
    expect($(`.${this.dp.cache['inputToSelectorId']}`).val()).to.equal(MOCK_END_DATE);

    done();
  });
  it("should initialize with 'date' type values", function (done) {
    const EXPECTED_START_DATE = '2000-01-01';
    const EXPECTED_END_DATE = '2000-02-01';
    const MOCK_START_DATE = new Date(EXPECTED_START_DATE);
    const MOCK_END_DATE = new Date(EXPECTED_END_DATE);

    this.init({ 
      type: 'range', 
      startDate: MOCK_START_DATE, 
      endDate: MOCK_END_DATE
    });

    expect($(`.${this.dp.cache['inputFromSelectorId']}`).val()).to.equal(EXPECTED_START_DATE);
    expect($(`.${this.dp.cache['inputToSelectorId']}`).val()).to.equal(EXPECTED_END_DATE);

    done();
  });

  it("should throw an error on invalid startDate", function (done) {
    const MOCK_ERROR_START_DATE = 'XXXX-XX-XX';

    this.init({ startDate: MOCK_ERROR_START_DATE });

    expect(this.initValueSpy.exceptions[0]).to.equal(WrongInitDateError);

    done();
  });

  it("should throw an error on invalid endDate", function (done) {
    const MOCK_START_DATE = '2000-01-01';
    const MOCK_ERROR_END_DATE = 'XXXX-XX-XX';

    this.init({ startDate: MOCK_START_DATE, endDate: MOCK_ERROR_END_DATE });

    expect(this.initValueSpy.exceptions[1]).to.equal(WrongInitDateError);

    done();
  });

  it("should update input field on day select", function (done) {
    this.init();

    expect($(`.${this.dp.cache['inputFromSelectorId']}`).val()).to.be.empty;

    $('.ui-state-default').trigger('click');

    expect($(`.${this.dp.cache['inputFromSelectorId']}`).val()).to.not.be.empty;

    done();
  });

  it("should update calendar on input change", function (done) {
    const MOCK_SELECTED_DATE = '2000-01-02';
    const EXPECTED_SELECTED_DAY = '2';

    this.init();

    $(`.${this.dp.cache['inputFromSelectorId']}`).val(MOCK_SELECTED_DATE).trigger('change');
    
    expect($(`.ui-state-active`).text()).to.equal(EXPECTED_SELECTED_DAY);

    done();
  });

  it("should focus input field on render", function (done) {
    this.init();
 
    expect($(`.${this.dp.cache['inputFromSelectorId']}`).is(':focus')).to.be.true;

    done();
  });

  it("should focus 'to' date after 'from' date is filled", function (done) {
    const MOCK_CORRECT_DATE = '2000-02-28';

    this.init({ type: 'range' });

    $(`.${this.dp.cache['inputFromSelectorId']}`).val(MOCK_CORRECT_DATE).trigger('change');

    expect($(`.${this.dp.cache['inputToSelectorId']}`).is(':focus')).to.be.true;

    done();
  });

  it("shouldn't accept values with incorrect format", function (done) {
    const MOCK_WRONG_VALUE = 'XXXX-XX-XX';

    this.init({ type: 'range' });

    $(`.${this.dp.cache['inputFromSelectorId']}`).val(MOCK_WRONG_VALUE).trigger('change');

    expect($(`.${this.dp.cache['inputFromSelectorId']}`).val()).to.equal('');

    done();
  });

  it("should handle date errors", function (done) {
    const MOCK_ERROR_DATE = '2000-02-30';
    const MOCK_CORRECT_DATE = '2000-02-28';

    this.init({ type: 'range' });

    $(`.${this.dp.cache['inputFromSelectorId']}`).val(MOCK_ERROR_DATE).trigger('change');

    expect($(`.${this.dp.cache['inputFromErrorSelectorId']}`)).to.have.lengthOf(1);

    $(`.${this.dp.cache['inputFromSelectorId']}`).val(MOCK_CORRECT_DATE).trigger('change');

    expect($(`.${this.dp.cache['inputFromErrorSelectorId']}`)).to.have.lengthOf(0);

    $(`.${this.dp.cache['inputToSelectorId']}`).val(MOCK_ERROR_DATE).trigger('change');

    expect($(`.${this.dp.cache['inputToErrorSelectorId']}`)).to.have.lengthOf(1);

    $(`.${this.dp.cache['inputToSelectorId']}`).val(MOCK_CORRECT_DATE).trigger('change');

    expect($(`.${this.dp.cache['inputToErrorSelectorId']}`)).to.have.lengthOf(0);

    done();
  });

  it("should fire handlers on value change", function (done) {
    const MOCK_ON_CORRECT_VALUE_HANDLER = () => {};
    const MOCK_ON_INCORRECT_VALUE_HANDLER = () => {};
    const MOCK_ON_EMPTY_VALUE_HANDLER = () => {};
    const MOCK_CORRECT_DATE = '2000-02-28';
    const MOCK_ERROR_DATE = '2000-02-30';
    const MOCK_EMPTY_DATE = '';

    this.init({
      onCorrectValue: MOCK_ON_CORRECT_VALUE_HANDLER,
      onIncorrectValue:  MOCK_ON_INCORRECT_VALUE_HANDLER,
      onEmptyValue: MOCK_ON_EMPTY_VALUE_HANDLER
    });

    this.callDatesSpy.resetHistory();
    expect(this.callDatesSpy.calledWith(MOCK_ON_CORRECT_VALUE_HANDLER)).to.be.false;
    $(`.${this.dp.cache['inputFromSelectorId']}`).val(MOCK_CORRECT_DATE).trigger('change');
    expect(this.callDatesSpy.calledWith(MOCK_ON_CORRECT_VALUE_HANDLER)).to.be.true;
    expect(this.callDatesSpy.calledWith(MOCK_ON_INCORRECT_VALUE_HANDLER)).to.be.false;
    expect(this.callDatesSpy.calledWith(MOCK_ON_EMPTY_VALUE_HANDLER)).to.be.false;

    this.callDatesSpy.resetHistory();
    expect(this.callDatesSpy.calledWith(MOCK_ON_INCORRECT_VALUE_HANDLER)).to.be.false;
    $(`.${this.dp.cache['inputFromSelectorId']}`).val(MOCK_ERROR_DATE).trigger('change');
    expect(this.callDatesSpy.calledWith(MOCK_ON_CORRECT_VALUE_HANDLER)).to.be.false;
    expect(this.callDatesSpy.calledWith(MOCK_ON_INCORRECT_VALUE_HANDLER)).to.be.true;
    expect(this.callDatesSpy.calledWith(MOCK_ON_EMPTY_VALUE_HANDLER)).to.be.false;

    this.callDatesSpy.resetHistory();
    expect(this.callDatesSpy.calledWith(MOCK_ON_EMPTY_VALUE_HANDLER)).to.be.false;
    $(`.${this.dp.cache['inputFromSelectorId']}`).val(MOCK_EMPTY_DATE).trigger('change');
    expect(this.callDatesSpy.calledWith(MOCK_ON_CORRECT_VALUE_HANDLER)).to.be.false;
    expect(this.callDatesSpy.calledWith(MOCK_ON_INCORRECT_VALUE_HANDLER)).to.be.false;
    expect(this.callDatesSpy.calledWith(MOCK_ON_EMPTY_VALUE_HANDLER)).to.be.true;

    done();
  });

  it("should clear values on 'clear icon' click", function (done) {
    const MOCK_FROM_DATE = '2000-01-01';
    const MOCK_TO_DATE = '2000-02-01';

    this.init({ type: 'range' });

    $(`.${this.dp.cache['inputFromSelectorId']}`).val(MOCK_FROM_DATE).trigger('change');
    $(`.${this.dp.cache['inputToSelectorId']}`).val(MOCK_TO_DATE).trigger('change');

    expect($(`.${this.dp.cache['inputFromSelectorId']}`).val()).to.equal(MOCK_FROM_DATE);
    expect($(`.${this.dp.cache['inputToSelectorId']}`).val()).to.equal(MOCK_TO_DATE);

    $(`.${this.dp.cache['fromDateClearIconSelectorId']}`).trigger('click');
    
    expect($(`.${this.dp.cache['inputFromSelectorId']}`).val()).to.equal('');
    expect($(`.${this.dp.cache['inputToSelectorId']}`).val()).to.equal(MOCK_TO_DATE);

    $(`.${this.dp.cache['inputFromSelectorId']}`).val(MOCK_FROM_DATE).trigger('change');
    $(`.${this.dp.cache['toDateClearIconSelectorId']}`).trigger('click');

    expect($(`.${this.dp.cache['inputFromSelectorId']}`).val()).to.equal(MOCK_FROM_DATE);
    expect($(`.${this.dp.cache['inputToSelectorId']}`).val()).to.equal('');

    done();
  });

  it("should switch start date and end date if earlier end date is selected", function (done) {
    const MOCK_FROM_DATE = '2000-02-01';
    const MOCK_TO_DATE = '2000-01-01';

    this.init({ type: 'range' });

    $(`.${this.dp.cache['inputFromSelectorId']}`).val(MOCK_FROM_DATE).trigger('change');
    $(`.${this.dp.cache['inputToSelectorId']}`).val(MOCK_TO_DATE).trigger('change');

    expect($(`.${this.dp.cache['inputFromSelectorId']}`).val()).to.equal(MOCK_TO_DATE);
    expect($(`.${this.dp.cache['inputToSelectorId']}`).val()).to.equal(MOCK_FROM_DATE);

    done();
  });

  it("should highlight selected range on day hover", function (done) {
    const MOCK_FROM_DATE = '2000-01-15';
    const MOCK_HOVERED_RIGHT_DAY = '17';
    const MOCK_HOVERED_BEFORE_RIGHT_DAY = '16';
    const MOCK_HOVERED_AFTER_RIGHT_DAY = '18';
    const MOCK_HOVERED_LEFT_DAY = '13';
    const MOCK_HOVERED_BEFORE_LEFT_DAY = '14';
    const MOCK_HOVERED_AFTER_LEFT_DAY = '12';

    this.init({ type: 'range' });

    $(`.${this.dp.cache['inputFromSelectorId']}`).val(MOCK_FROM_DATE).trigger('change');

    $(`a:contains("${MOCK_HOVERED_RIGHT_DAY}")`).trigger('mouseenter');
    
    expect(
      $(`td:contains("${MOCK_HOVERED_BEFORE_RIGHT_DAY}")`).attr('class')
    ).to.equal(
      this.dp.cache['hoverClass']
    );

    expect(
      $(`td:contains("${MOCK_HOVERED_AFTER_RIGHT_DAY}")`).attr('class')
    ).to.not.equal(
      this.dp.cache['hoverClass']
    );

    expect(
      $(`td:contains("${MOCK_HOVERED_RIGHT_DAY}")`).attr('class')
    ).to.equal(
      this.dp.cache['hoverLastClass']
    );

    $(`.ui-state-disabled`).trigger('mouseenter');

    expect(
      $(`td:contains("${MOCK_HOVERED_AFTER_RIGHT_DAY}")`).attr('class')
    ).to.not.equal(
      this.dp.cache['hoverClass']
    );


    $(`a:contains("${MOCK_HOVERED_LEFT_DAY}")`).trigger('mouseenter');

    expect(
      $(`td:contains("${MOCK_HOVERED_BEFORE_LEFT_DAY}")`).attr('class')
    ).to.equal(
      this.dp.cache['hoverClass']
    );

    expect(
      $(`td:contains("${MOCK_HOVERED_AFTER_LEFT_DAY}")`).attr('class')
    ).to.not.equal(
      this.dp.cache['hoverClass']
    );

    expect(
      $(`td:contains("${MOCK_HOVERED_LEFT_DAY}")`).attr('class')
    ).to.equal(
      this.dp.cache['hoverFirstClass']
    );

    $(`a:contains("${MOCK_HOVERED_RIGHT_DAY}")`).trigger('mouseleave');

    expect(
      $(`td:contains("${MOCK_HOVERED_AFTER_LEFT_DAY}")`).attr('class')
    ).to.not.equal(
      this.dp.cache['hoverClass']
    );

    done();
  });
});
