import $ from 'jquery';

export const bindDropdown = (listItemClass) => {
  $(document).on('click', `.${listItemClass}`, function (e) {
    $(this).parents('.dropdown').find('.dropdown-toggle').text(e.currentTarget.innerText);
  });
};
