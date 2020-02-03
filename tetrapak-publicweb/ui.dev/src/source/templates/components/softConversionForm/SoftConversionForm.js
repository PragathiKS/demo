import $ from 'jquery';
import 'bootstrap';
import { storageUtil, loc } from '../../../scripts/common/common';
import { ajaxWrapper } from '../../../scripts/utils/ajax';
import { ajaxMethods, API_SOFT_CONVERSION, REG_EMAIL } from '../../../scripts/utils/constants';
import { $body } from '../../../scripts/utils/commonSelectors';

class SoftConversionForm {
  constructor({ el }) {
    this.root = $(el);
  }
  cache = {};
  initCache() {
    this.cache.$modal = this.root.find('#softConversionModal');
    this.cache.$form = this.root.find('#softConversionModal form');
    this.cache.$field = this.root.find('#softConversionModal input[type="text"]');
    this.cache.$submitBtn = this.root.find('#softConversionModal .form-submit');
    this.cache.$tabtoggle = this.root.find('#softConversionModal .pw-form__nextbtn[data-toggle="tab"]');
    this.cache.$prevScfToggle = this.root.find('#softConversionModal .pw-form__prevbtn__scf[data-toggle="tab"]');
    this.cache.$radiobtns = this.root.find('#softConversionModal input:radio');
  }
  storageFormData() {
    const formData = this.cache.$form.serializeArray();
    storageUtil.set('softConversionData', formData);
  }
  validEmail(email) {
    return REG_EMAIL.test(email);
  }
  setFormData(data) {
    $.each(data, (...args) => {
      const [, obj] = args;
      if (obj.name === 'group') {
        this.root.find('#softConversionModal input[name="group"]:checked').prop('checked', false);
        this.root.find(`#softConversionModal input[name="group"][value="${obj.value}"]`).prop('checked', true);
      } else {
        this.root.find(`#softConversionModal input[name="${obj.name}"]`).val(obj.value);
      }
    });
  }
  checkAndSubmit(e, $this) {
    const self = this;
    let isValid = true;
    const $target = $(e.target);
    const $parentTab = $target.closest('.tab-pane');
    $parentTab.find('input').each(function () {
      const fieldName = $this.attr('name');
      const currentValue = $this.val();
      if ($this.prop('required') && (currentValue === '') || (fieldName === 'email-address') && !self.validEmail(currentValue)) {
        isValid = false;
        e.preventDefault();
        e.stopPropagation();
        $this.closest('.form-group').addClass('hasError');
      }
    });
    if (isValid) {
      if (window.digitalData && window.digitalData.formInfo) {
        $.extend(window.digitalData.formInfo, {
          stepName: 'thank you'
        });
        delete window.digitalData.formInfo.stepNo;
        if (window._satellite) {
          window._satellite.track('form_tracking');
        }
      }
      e.preventDefault();
      self.storageFormData();
      ajaxWrapper.getXhrObj({
        url: API_SOFT_CONVERSION,
        method: ajaxMethods.GET,
        data: this.root.find('#softConversionModal form').serialize()
      }).done(
        () => {
          const docpath = this.root.find('#softConversionModal input[name="docpath"]').val();
          loc.open(docpath, '_blank');
          this.root.find('#softConversionModal').data('form-filled', true);
          this.root.find('#softConversionModal .softc-title-js').addClass('d-none');
          this.root.find('#softConversionModal .softc-thankyou-js').removeClass('d-none');
        }
      );
    }
  }
  checkStepAndContinue(e, $this) {
    const self = this;
    const $target = $(e.target);
    const $parentTab = $target.closest('.tab-pane');
    let isValidStep = true;
    $parentTab.find('input').each(function () {
      const fieldName = $this.attr('name');
      const currentValue = $this.val();
      if ($this.prop('required') && (currentValue === '') || (fieldName === 'email-address') && !self.validEmail(currentValue)) {
        e.preventDefault();
        e.stopPropagation();
        $this.closest('.form-group').addClass('hasError');
        self.root.find(`.info-group.${fieldName}`).removeClass('show');
        isValidStep = false;
      } else {
        self.root.find(`p.${fieldName}`).text(currentValue);
        self.root.find(`.info-group.${fieldName}`).addClass('show');
        $this.closest('.form-group').removeClass('hasError');
      }
    });
    if (
      isValidStep
      && window.digitalData
      && window.digitalData.formInfo
    ) {
      const stepNumber = $parentTab.attr('data-stepNumber');
      const stepName = $parentTab.attr('data-stepName');
      $.extend(window.digitalData.formInfo, {
        stepName,
        stepNo: stepNumber
      });
      if (stepNumber === '0') {
        const userRole = $('input[name="group"]:checked').val();
        window.digitalData.formInfo.userRoleSelected = userRole;
      } else if (stepNumber === '1') {
        delete window.digitalData.formInfo.userRoleSelected;
      }
      if (window._satellite) {
        window._satellite.track('form_tracking');
      }
    }
  }
  setFields($this) {
    if ($this.val() !== 'Professional') {
      this.root.find('#softConversionModal .isPro').addClass('d-none');
      this.root.find('#softConversionModal .isNotPro').removeClass('d-none');
    } else {
      this.root.find('#softConversionModal .isPro').removeClass('d-none');
      this.root.find('#softConversionModal .isNotPro').addClass('d-none');
    }
  }
  bindEvents() {
    /* Bind jQuery events here */
    const self = this;
    const { $modal, $field, $submitBtn, $tabtoggle, $prevScfToggle, $radiobtns } = this.cache;
    $body.on('show.bs.tab', function (e) {
      const $target = $(e.target);
      const parentModal = $target.closest('.modal');
      if (!parentModal.length) {
        return;
      }
      const grandParentId = parentModal.attr('id');
      const $toggleBtns = $(`#${grandParentId}`).find('[data-toggle="tab"]');
      $toggleBtns.removeClass('active show');
      const selectedTarget = $(e.target).data('target');
      $('[data-target="' + selectedTarget + '"]').addClass('active show');
    });
    $modal
      .on('show.bs.modal', () => {
        const $softConversionModal = this.root.find('#softConversionModal');
        if ($softConversionModal.data('form-filled')) {
          $softConversionModal.find('[data-toggle="tab"]').removeClass('active show');
          $softConversionModal.find('.tab-pane').removeClass('active');
          $softConversionModal.find('.tab-content #step-final').addClass('active');
          $softConversionModal.find('softc-title-js').addClass('d-none');
          $softConversionModal.find('.softc-thankyou-js').removeClass('d-none');
          const docpath = $softConversionModal.find('input[name="docpath"]').val();
          loc.open(docpath, '_blank');
        }
      })
      .on('hidden.bs.modal', () => {
        const $softConversionModal = this.root.find('#softConversionModal');
        $softConversionModal.find('[data-toggle="tab"]').removeClass('active show');
        $softConversionModal.find('.tab-pane').removeClass('active');
        $softConversionModal.find('.tab-content .tab-pane:first').addClass('active');
      });
    $field.on('change', function () {
      const $this = $(this);
      if ($this.val().length) {
        $this.closest('.form-group').removeClass('hasError');
      }
    });
    $submitBtn.on('click', function (e) {
      self.checkAndSubmit(e, $(this));
    });
    $tabtoggle.on('click', function (e) {
      self.checkStepAndContinue(e, $(this));
    });
    $prevScfToggle.on('click', function (e) {
      const $parentTab = $(e.target).closest('.tab-pane');
      const stepNumber = $parentTab.attr('data-stepNumber');
      const stepName = $parentTab.attr('data-stepName');
      if (window.digitalData) {
        $.extend(window.digitalData.formInfo, {
          stepName: `previous:${stepName}`,
          stepNo: stepNumber
        });
        if (window._satellite) {
          window._satellite.track('form_tracking');
        }
      }
    });
    $radiobtns.on('click', function () {
      self.setFields.apply(self, [$(this)]);
    });
  }
  init() {
    const softConversionData = storageUtil.get('softConversionData');
    this.initCache();
    if (softConversionData) {
      this.setFormData(softConversionData);
      this.root.find('#softConversionModal').data('form-filled', true);
    }
    this.bindEvents();
  }
}

export default SoftConversionForm;
