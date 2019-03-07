import $ from 'jquery';
import OrderSearch from './OrderSearch';
import { render } from '../../../scripts/utils/render';

describe('OrderSearch', function () {
    before(function () {
        this.orderSearch = new OrderSearch({ el: document.body });
        this.initSpy = sinon.spy(this.orderSearch, 'init');
        this.renderSpy = sinon.spy(render, 'fn');
        this.orderSearch.init();
    });
    after(function () {
        this.initSpy.restore();
        this.renderSpy.restore();
    });
    it('should initialize', function () {
        expect(this.orderSearch.init.called).to.be.true;
    });
    it('should render component on page load', function () {
        expect(render.fn.called).to.be.true;
    });
});