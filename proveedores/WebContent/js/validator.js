function emailError(textbox) {
  if (textbox.value == '') {
    textbox.setCustomValidity('Please fill in this field');
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
    textbox.setCustomValidity('The passwords do not match');
  } else {
    textbox.setCustomValidity('')
  }
  return true;
};

function standardError(textbox) {
  if (textbox.value == '') {
    textbox.setCustomValidity('Please fill in this field');
  } else {
    textbox.setCustomValidity('');
  }
  return true;
};

