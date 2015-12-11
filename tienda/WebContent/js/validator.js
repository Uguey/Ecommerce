function emailError(textbox) {
  if (textbox.value == '') {
    textbox.setCustomValidity('Please fill out this field');
  } else if (textbox.validity.typeMismatch) {
    textbox.setCustomValidity('Please enter a valid email address');
  } else {
    textbox.setCustomValidity('');
  }
  return true;
};

function passwordError(textbox) {
  var passOne = document.getElementById("password_first").value;
  var comparison = textbox.value.localeCompare(passOne);
  if (comparison != 0) {
    textbox.setCustomValidity('The passwords do not macth');
  } else {
    textbox.setCustomValidity('');
  }
  return true;
};

function standardError(textbox) {
  if (textbox.value == '') {
    textbox.setCustomValidity('Please fill out this field');
  } else {
    textbox.setCustomValidity('');
  }
  return true;
};

function creditCardError(textbox) {
  if (textbox.value == '') {
    textbox.setCustomValidity('Please fill out this field');
  } else if (textbox.value.length != 20) {
    textbox
            .setCustomValidity('Please enter a valid credit card (20 alphanumeric characters)');
  } else {
    textbox.setCustomValidity('');
  }
  return true;
};

function postalCodeError(textbox) {
  if (textbox.value == '') {
    textbox.setCustomValidity('Please fill out this field');
  } else if (textbox.value.length != 5) {
    textbox.setCustomValidity('Please enter a valid postal code (5 numbers)');
  } else {
    textbox.setCustomValidity('');
  }
  return true;
};

function cvvError(textbox) {
  if (textbox.value == '') {
    textbox.setCustomValidity('Please fill out this field');
  } else if (textbox.value.length != 3) {
    textbox.setCustomValidity('Please enter a valid cvv (3 numbers)');
  } else {
    textbox.setCustomValidity('');
  }
  return true;
};

function isNumber(evt) {
  evt = (evt) ? evt : window.event;
  var charCode = (evt.which) ? evt.which : evt.keyCode;
  if (charCode > 31 && (charCode < 48 || charCode > 57)) { return false; }
  return true;
};