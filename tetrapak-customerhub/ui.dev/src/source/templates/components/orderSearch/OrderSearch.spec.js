import OrderSearch from './OrderSearch';
import $ from 'jquery';

describe('OrderSearch', function () {
    before(function () {
        this.orderSearch = new OrderSearch({ el: document.body });
        this.initSpy = sinon.spy(this.orderSearch, 'init');
        this.orderSearch.init();
    });
    it('should initialize', function () {
        expect(this.orderSearch.init.called).to.be.true;
    });
});