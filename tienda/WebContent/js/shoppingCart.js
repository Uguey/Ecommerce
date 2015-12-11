/**
 * Requires: jquery.form.min.js
 */
$(document).ready(function() {
  /**
   * @param $row
   *          jQuery wrapped .prod-row element
   * @return Quantity of items of a given .prod-row.
   */
  var getQuantity = function($row) {
    return parseInt($row.find('.quantity').html());
  }

  /**
   * @param $row
   *          jQuery wrapped .prod-row element
   * @param q
   *          Quantity of items to set.
   */
  var setQuantity = function($row, q) {
    $row.find('.quantity').html(q);
  }

  /**
   * Re calculate all row subtotal prices and the grand total one. Also if some
   * row has a quantity of 0, delete it.
   */
  var updateRows = function() {
    var total = 0;

    $('.prod-row').each(function() {
      var $row = $(this);
      var quantity = getQuantity($row);
      var price = parseFloat($row.find('.price').html());
      var subtotal = quantity * price;
      total += subtotal;

      // Update this row's subtotal with 2 dec precision.
      $row.find('.subtotal').html(subtotal.toFixed(2));

      // If the row has no quantity left, fade it out.
      if (quantity <= 0) {
        $row.fadeOut();
      }
    });

    // Update grand total with 2 dec precision.
    $('#total').html(total.toFixed(2));
  };

  // Register delete product action.
  $('.remove-from-cart').ajaxForm(function(response, statusText, xhr, $form) {
    // Update cart badge.
    $(document).trigger({
      type: 'cartupdate',
      numItems: response
    });

    var $row = $form.closest('.prod-row');
    setQuantity($row, 0);

    // Update rows.
    updateRows();
  });

  // Register decrement product action.
  $('.decrement-unit').ajaxForm(function(response, statusText, xhr, $form) {
    // Update cart badge.
    $(document).trigger({
      type: 'cartupdate',
      numItems: response
    });

    var $row = $form.closest('.prod-row');
    setQuantity($row, getQuantity($row) - 1);

    // Update rows.
    updateRows();
  });

  // Register increment product action.
  $('.increment-unit').ajaxForm(function(response, statusText, xhr, $form) {
    // Update cart badge.
    $(document).trigger({
      type: 'cartupdate',
      numItems: response
    });

    var $row = $form.closest('.prod-row');
    setQuantity($row, getQuantity($row) + 1);

    // Update rows.
    updateRows();
  });

  // Calculate initial price values
  updateRows();
});