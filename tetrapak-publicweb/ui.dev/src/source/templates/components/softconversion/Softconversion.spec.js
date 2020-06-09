// import $ from 'jquery';
// import Softconversion from './Softconversion';
// import softConversionTemplate from '../../../test-templates-hbs/softConversion.hbs';

// describe('Softconversion', function () {
//   before(function () {
//   	$(document.body).empty().html(softConversionTemplate());
//     this.softconversion = new Softconversion({el: document.body});
//     this.initSpy = sinon.spy(this.softconversion, 'init');
//     this.submitFormSpy = sinon.spy(this.softconversion, 'submitForm');
//     this.softconversion.init();
//   });

//   afterEach(function () {
//     $(document.body).empty();
//     this.initSpy.restore();
//     this.submitFormSpy.restore();
//   });

//   it('should initialize', function () {
//     expect(this.softconversion.init.called).to.be.true;
//   });

//   // it('should not submit Form when required fields are empty', function (done) {

//   // 	// console.log('document:::', document);
//   //   $('#firstName-textimage').value = 'mockname';
//   //   this.softconversion.cache.$submitBtn.click();
//   //   expect(this.softconversion.submitForm.called).to.be.false;
//   //   done();
//   // });

//    it('should not submit Form when required fields are empty', function (done) {

//   	// console.log('document:::', document);
//     $('#firstName-textimage').value = 'mockname';
//     this.softconversion.cache.$submitBtn.click();
//     expect(this.softconversion.submitForm.called).to.be.false;
//     done();
//   });

// });
