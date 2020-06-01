const emailRegex = /^[a-zA-Z0-9.!#$%&'*+\/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$/;
const upperCaseRegex = /[A-Z]+/;
const lowerCaseRegex = /[a-z]+/;
const numberRegex = /[0-9]+/;
const phoneRegex = /^[0-9]{10}$/
const specialCharRegex = /[!@#$%*&^-_=+]+/;
const disableButtonCss = { "opacity": "0.3", "cursor": "not-allowed" };
const enableButtonCss = { "opacity": "1.0", "cursor": "pointer" };
const disableButtonAttr = { "disabled": true };
const enableButtonAttr = { "disabled": false }
const css = { "opacity": "0.30", "filter": "Alpha(Opacity=30)", "background": "#AAA 50% 50% repeat-x" };
const rcss = { "opacity": "1.0", "filter": "Alpha(Opacity=100)", "background": "#FFF" };
const defaultTip = "All fields are required.";
const loginUrl = "/login";
const logoutUrl = "/logout";
const regUrl = "/register";
const verifyTokenUrl = "/verifytoken";
const otpValidateUrl = "/2fa";
const loginDiv = '<div style="display:none" id="dialog-form" title="User Login">\
<p class="validateTips">' + defaultTip + '</p>\
<form>\
<fieldset>\
<label for="email">Email</label>\
<input type="text" name="email" id="loginEmail" value="" class="text ui-widget-content ui-corner-all">\
<label for="password">Password</label>\
<input type="password" name="password" id="loginPassword" value="" class="text ui-widget-content ui-corner-all">\
<!-- Allow form submission with keyboard without duplicating the dialog button -->\
<input type="submit" tabindex="-1" style="position:absolute; top:-1000px">\
</fieldset>\
</form>\
<div style="display:none; margin:auto; height:30px" id="loginLoader" class="loader"></div>\
</div>';
const registerDiv = '<div style="display:none" id="reg-dialog-form" title="User Registration">\
<p class="validateTips">' + defaultTip + '</p>\
<form>\
<fieldset>\
<label for="email">First Name</label>\
<input type="text" name="fname" id="regFname" value="" class="text ui-widget-content ui-corner-all">\
<label for="email">Last Name</label>\
<input type="text" name="lname" id="regLname" value="" class="text ui-widget-content ui-corner-all">\
<label for="email">Mobile</label>\
<input type="tel" name="mobile" id="regMobile" value="" class="text ui-widget-content ui-corner-all">\
<label for="email">Email</label>\
<input type="text" name="email" id="regEmail" value="" class="text ui-widget-content ui-corner-all">\
<label for="password">Password</label>\
<input type="password" name="password" id="regPassword" value="" class="text ui-widget-content ui-corner-all">\
<label for="password">Confirm Password</label>\
<input type="password" name="cpassword" id="regcPassword" value="" class="text ui-widget-content ui-corner-all">\
<!-- Allow form submission with keyboard without duplicating the dialog button -->\
<input type="submit" tabindex="-1" style="position:absolute; top:-1000px">\
</fieldset>\
</form>\
<div style="display:none; margin:auto; height:30px" id="loginLoader" class="loader"></div>\
</div>';
const otpDiv = '<div style="display:none" id="otp-dialog-form" title="Two-Factor Authentication">\
<p class="validateTips">' + defaultTip + '</p>\
<span id="otpEmail" style="font-size: 12px;margin-left: 5px;color: gray;"></span>\
<form>\
<fieldset>\
<label for="email">OTP</label>\
<input type="text" name="otp" id="otp" value="" class="text ui-widget-content ui-corner-all">\
<!-- Allow form submission with keyboard without duplicating the dialog button -->\
<input type="submit" tabindex="-1" style="position:absolute; top:-1000px">\
</fieldset>\
</form>\
<div style="display:none; margin:auto; height:30px" id="loginLoader" class="loader"></div>\
</div>';
var messageDiv = '<div id="dialog-message" title="MSG_TITLE">\
<p style="margin-top:MARGIN_TOP">\
  <span class="ui-icon ui-icon-circle-check" style="float:left; margin:6px 7px 50px 0;"></span>\
  <span id="msgMsg"></span>\
</p>\
</div>';
var loadingDiv = '<div id="loading-dialog" title="Loading">\
    <div style="margin:auto; height:90px; width:90px" id="loginLoader" class="loader"></div>\
</div>';

var loginCallback = null;
var registerCallback = null;
var dialog = null;
var overlayTimeout = null;

function updateTips(tips, t) {
    tips
        .html(t)
        .addClass("ui-state-highlight");
    setTimeout(function() {
        tips.removeClass("ui-state-highlight", 1500);
    }, 500);
}

function showLoading() {
    addOverlayElement();
    if ($("#loading-dialog").length == 0) {
        $("body").append(loadingDiv);
    }
    dialog = $("#loading-dialog").dialog({
        modal: true,
        height: 200,
        width: 200,
        show: {
            effect: "clip",
            duration: 300
        },
        hide: {
            effect: "clip",
            duration: 300
        },
        closeOnEscape: false,
        open: function(event, ui) {
            $(".ui-dialog-titlebar").css({ "visibility": "hidden" });
        },
        close: function() {
            hideOverlayElement();
        }
    });
}

function hideLoading() {
    if ($("#loading-dialog").length) {
        $("#loading-dialog").dialog("close");
    }
}

function dialogMsg(title, message, callback, marginTop) {
    addOverlayElement();
    if (!marginTop) {
        marginTop = "0 px";
    }
    messageDiv = messageDiv.split("MSG_TITLE").join(title).split("MARGIN_TOP").join(marginTop);
    if ($("#dialog-message").length == 0) {
        $("body").append(messageDiv);
    }
    $("#dialog-message #msgMsg").html(message);
    dialog = $("#dialog-message").dialog({
        modal: true,
        height: 200,
        show: {
            effect: "clip",
            duration: 300
        },
        hide: {
            effect: "clip",
            duration: 300
        },
        buttons: {
            Ok: function() {
                dialog.dialog("close");
                hideOverlayElement();
                if (callback) {
                    callback();
                }
            }
        }
    });
}

function openLoginDiv(callback) {
    addOverlayElement();
    loginCallback = callback;
    if ($("#dialog-form").length == 0) {
        $("body").append(loginDiv);
    }
    dialog = $("#dialog-form").dialog({
        autoOpen: false,
        height: 400,
        width: 350,
        modal: true,
        show: {
            effect: "clip",
            duration: 300
        },
        hide: {
            effect: "clip",
            duration: 300
        },
        buttons: {
            "Login": function() {
                userLogin();
            },
            Cancel: function() {
                clearFields();
                updateTips($("#dialog-form .validateTips"), defaultTip);
                dialog.dialog("close");
            }
        },
        close: function() {
            form[0].reset();
            $("#dialog-form").remove();
            hideOverlayElement();
        }
    });

    form = dialog.find("form").on("submit", function(event) {
        event.preventDefault();
    });

    dialog.dialog("open");
}

function openOtpDiv(email) {
    addOverlayElement();
    if ($("#otp-dialog-form").length == 0) {
        $("body").append(otpDiv);
    }
    dialog = $("#otp-dialog-form").dialog({
        autoOpen: false,
        height: 350,
        width: 350,
        modal: true,
        show: {
            effect: "clip",
            duration: 300
        },
        hide: {
            effect: "clip",
            duration: 300
        },
        buttons: {
            "Validate": function() {
                user2fa();
            },
            Cancel: function() {
                clearFields();
                updateTips($("#otp-dialog-form .validateTips"), defaultTip);
                dialog.dialog("close");
            }
        },
        close: function() {
            form[0].reset();
            $("#dialog-form").remove();
            hideOverlayElement();
        }
    });

    form = dialog.find("form").on("submit", function(event) {
        event.preventDefault();
    });

    dialog.find("#otpEmail").html("OTP sent to your email : " + email);

    dialog.dialog("open");
}

function addOverlayElement() {
    if (overlayTimeout) {
        clearTimeout(overlayTimeout);
    }
    var div = '<div id="overlay" style="\
            opacity: 0.0;\
            top:0px;\
            left:0px;\
            position: absolute;\
            height: 100%;\
            width: 100%;\
            z-index: 10;\
            background: black;\
        "></div>';
    if (!$("#overlay").length) {
        $("body").append(div);
    }
    $("#overlay").stop().animate({ "opacity": "0.5" }, 300);
    $("#overlay").css({ "z-index": 10 });
}

function hideOverlayElement() {
    $("#overlay").stop().animate({ "opacity": "0.0" }, 300);
    if (overlayTimeout) {
        clearTimeout(overlayTimeout);
    }
    overlayTimeout = setTimeout(function() {
        $("#overlay").css({ "z-index": -10 });
    }, 500);

}

function openRegisterDiv(callback) {
    addOverlayElement();
    registerCallback = callback;
    if ($("#reg-dialog-form").length == 0) {
        $("body").append(registerDiv);
    }
    dialog = $("#reg-dialog-form").dialog({
        autoOpen: false,
        height: 650,
        width: 350,
        modal: true,
        show: {
            effect: "clip",
            duration: 300
        },
        hide: {
            effect: "clip",
            duration: 300
        },
        buttons: {
            "Register": function() {
                userRegister();
            },
            Cancel: function() {
                clearFields();
                updateTips($("#dialog-form .validateTips"), defaultTip);
                dialog.dialog("close");
            }
        },
        close: function() {
            form[0].reset();
            $("#reg-dialog-form").remove();
            hideOverlayElement();
        }
    });

    form = dialog.find("form").on("submit", function(event) {
        event.preventDefault();
    });

    dialog.dialog("open");

}

function userRegister() {
    const fname = $("#regFname");
    const lname = $("#regLname");
    const mobile = $("#regMobile");
    const email = $("#regEmail");
    const password = $("#regPassword");
    const cpassword = $("#regcPassword");
    const tips = $("#reg-dialog-form .validateTips");
    const errorClass = "ui-state-error";

    if (!fname.val() || fname.val().trim().length == 0) {
        fname.addClass(errorClass);
        updateTips(tips, "Please enter full name!");
        return;
    } else {
        fname.removeClass(errorClass);
    }
    if (!lname.val() || lname.val().trim().length == 0) {
        lname.addClass(errorClass);
        updateTips(tips, "Please enter last name!");
        return;
    } else {
        lname.removeClass(errorClass);
    }
    if (!mobile.val() || !phoneRegex.test(mobile.val())) {
        mobile.addClass(errorClass);
        updateTips(tips, "Please enter valid 10 digit mobile number!");
        return;
    } else {
        mobile.removeClass(errorClass);
    }
    if (!email.val() || !(emailRegex.test(email.val()))) {
        email.addClass(errorClass);
        updateTips(tips, "Please enter valid email!");
        return;
    } else {
        var domain = email.val().split("@")[1];
        if (domain != "dal.ca") {
            email.addClass(errorClass);
            updateTips(tips, "Please use dal.ca email!");
            return;
        } else {
            email.removeClass(errorClass);
        }
    }
    if (!validatePassword(password, tips, errorClass)) {
        return;
    }
    if (password.val() != cpassword.val()) {
        cpassword.addClass(errorClass);
        password.addClass(errorClass);
        updateTips(tips, "Passwords don't match!");
        return;
    } else {
        cpassword.removeClass(errorClass);
        password.removeClass(errorClass);
    }

    var button = $($(".ui-dialog-buttonset button")[0]);
    disableButton(button);
    updateTips(tips, "Registering...");
    $("#loginLoader").show();
    register(fname.val(), lname.val(), mobile.val(), email.val(), password.val(), button);
}

function validatePassword(password, tips, errorClass) {
    var error = "Password must contain: ";
    var valid = true;
    if (!upperCaseRegex.test(password.val())) {
        error += "<br/>- 1 Uppercase letter";
        valid = false;
    }
    if (!lowerCaseRegex.test(password.val())) {
        error += "<br/>- 1 Lowercase letter";
        valid = false;
    }
    if (!numberRegex.test(password.val())) {
        error += "<br/>- 1 Number";
        valid = false;
    }
    if (!specialCharRegex.test(password.val())) {
        error += "<br/>- 1 Special character";
        valid = false;
    }
    if (password.val().length < 8) {
        error += "<br/>- 8 characters";
        valid = false;
    }
    if (!valid) {
        password.addClass(errorClass);
        updateTips(tips, error);
        return false;
    } else {
        password.removeClass(errorClass);
    }
    return true;
}

function user2fa() {
    const otp = $("#otp");
    const tips = $("#otp-dialog-form .validateTips");

    if (!otp.val() || otp.val().length == 0) {
        otp.addClass("ui-state-error");
        updateTips(tips, "Please enter the OTP sent to your email");
        return;
    } else {
        otp.removeClass("ui-state-error");
    }

    const button = $($(".ui-dialog-buttonset button")[0]);

    disableButton(button);
    updateTips(tips, "Validating...");
    $("#loginLoader").show();
    otpValidate(otp.val(), button);
}

function userLogin() {
    var email = $("#loginEmail");
    var password = $("#loginPassword");

    if (!email.val() || !(emailRegex.test(email.val()))) {
        email.addClass("ui-state-error");
        updateTips($("#dialog-form .validateTips"), "Please enter valid email !");
        return;
    } else {
        email.removeClass("ui-state-error");
    }

    if (!password.val() || password.val().length < 8) {
        password.addClass("ui-state-error");
        updateTips($("#dialog-form .validateTips"), "Please enter a password with atleast 8 characters !");
        return;
    } else {
        password.removeClass("ui-state-error");
    }

    var button = $($(".ui-dialog-buttonset button")[0]);
    disableButton(button);
    updateTips($("#dialog-form .validateTips"), "Logging in...");
    $("#loginLoader").show();
    login(email.val(), password.val(), button);
}

function otpValidate(otp, button) {
    var param = "token=" + encodeURIComponent(otp);

    $.ajax({
        url: otpValidateUrl,
        data: param,
        method: "POST",
        success: function(result) {
            if (result.data.verified) {
                if (loginCallback) {
                    loginCallback(result.data);
                }
                enableButton(button);
                clearFields();
                dialog.dialog("close");
            } else {
                updateTips($("#otp-dialog-form .validateTips"), result.data.message);
                enableButton(button);
                clearFields();
            }
        },
        error: function(result) {
            updateTips($("#otp-dialog-form .validateTips"), result.responseJSON.data.message);
            enableButton(button);
            clearFields();
        }
    });
}

function register(fname, lname, mobile, email, password, button) {
    var param = "fname=" + encodeURIComponent(fname);
    param += "&lname=" + encodeURIComponent(lname);
    param += "&mobile=" + encodeURIComponent(mobile);
    param += "&email=" + encodeURIComponent(email);
    param += "&password=" + encodeURIComponent(password);

    $.ajax({
        url: regUrl,
        data: param,
        method: "POST",
        success: function(result) {
            if (registerCallback) {
                registerCallback(result.data);
            }
            enableButton(button);
            clearFields();
            dialog.dialog("close");
            setTimeout(function() {
                dialogMsg("Success", "Registration successful. Please proceed to Login.");
            }, 500);
        },
        error: function(result) {
            updateTips($("#dialog-form .validateTips"), result.responseJSON.data.message);
            enableButton(button);
            clearFields();
        }
    });
}

function login(email, password, button) {
    var param = "email=" + encodeURIComponent(email) + "&password=" + encodeURIComponent(password);

    $.ajax({
        url: loginUrl,
        data: param,
        method: "POST",
        success: function(result) {
            enableButton(button);
            clearFields();
            dialog.dialog("close");
            setTimeout(function() {
                openOtpDiv(email);
            }, 500);
        },
        error: function(result) {
            updateTips($("#dialog-form .validateTips"), result.responseJSON.data.message);
            enableButton(button);
            clearFields();
        }
    });
}

function clearFields() {
    $("#loginEmail").val("");
    $("#loginPassword").val("");
    $("#loginLoader").hide();

    $("#regFname").val("");
    $("#regLname").val("");
    $("#regMobile").val("");
    $("#regEmail").val("");
    $("#regPassword").val("");
    $("#regcPassword").val("");
    $("#loginLoader").hide();

    $("#ccCvv").val("");
    $("#ccCard").val("");
    $("#ccMmyy").val("");
}

function enableButton(button) {
    button.css(enableButtonCss);
    button.attr(enableButtonAttr);
}

function disableButton(button) {
    button.css(disableButtonCss);
    button.attr(disableButtonAttr);
}

function userLogout(callback) {
    $.ajax({
        url: logoutUrl,
        method: "POST",
        success: function(result) {
            if (callback) {
                callback();
            }
        },
        error: function(result) {
            alert("Failed to logout. Try again later");
            console.log(result);
        }
    });
}

function isUserLoggedIn(positiveCallback, negativeCallback) {
    const cookies = document.cookie.split(";");
    var jwtCookieValue = null;
    for (var index in cookies) {
        var cookie = cookies[index].trim();
        var name = cookie.split("=")[0];
        var value = cookie.split("=")[1];
        if (name == "jwtToken") {
            jwtCookieValue = value;
            break;
        }
    }

    if (jwtCookieValue && jwtCookieValue.trim().length > 0) {
        $.ajax({
            url: verifyTokenUrl,
            method: "POST",
            success: function(result) {
                if (positiveCallback) {
                    positiveCallback(result.data);
                }
            },
            error: function(result) {
                if (negativeCallback) {
                    negativeCallback();
                }
            }
        });
    } else {
        if (negativeCallback) {
            negativeCallback();
        }
    }
}
