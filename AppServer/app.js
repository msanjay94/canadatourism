const express = require('express');
const bodyParser = require('body-parser');
const http = require('http');
const urlEncodedParser = bodyParser.urlencoded({ extended: false });
const app = express();
const https = require("https"),
    fs = require("fs");

const options = {
    key: fs.readFileSync("./key.key"),
    cert: fs.readFileSync("./cert.pem"),
    passphrase: "bmserv"
};
const appPort = 8443;

const rootFolder = { "root": "." };
//const userServiceServer = { "host": "iam.travelsca.com", "port": 443 };
//const touristPlaceServer = { "host": "data.travelsca.com", "port": 443 };
//const bookingServer = { "host": "booking.travelsca.com", "port": 443 };
const userServiceServer = { "host": "ec2-18-233-13-69.compute-1.amazonaws.com", "port": 8081};
const touristPlaceServer = { "host": "ec2-18-233-13-69.compute-1.amazonaws.com", "port": 8082 };
const bookingServer = { "host": "ec2-18-233-13-69.compute-1.amazonaws.com", "port": 8083 };


const loginUrl = "/canadatourism/api/customer/authenticate";
const regUrl = "/canadatourism/api/customer/user/register";
const cities = "/canadatourism/api/cities/getcities";
const provinces = "/canadatourism/api/provinces/province/allProvinces"
const verifyTokenUrl = "/canadatourism/api/customer/verifytoken";
const bookingUrl = "/canadatourism/api/bus/booking/makebooking";
const busAvailabiltyUrl = "/canadatourism/api/bus/getavailibility"
const getBookingUrl = "/canadatourism/api/bus/booking/getbooking/all";
const paymentUrl = "/canadatourism/api/transaction/booking/makePayment";

const emailRegex = /^[a-zA-Z0-9.!#$%&'*+\/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$/;
const upperCaseRegex = /[A-Z]+/;
const lowerCaseRegex = /[a-z]+/;
const numberRegex = /[0-9]+/;
const phoneRegex = /^[0-9]{10}$/
const specialCharRegex = /[!@#$%*&^-_=+]+/;

const urls = {
    "home": "/home",
    "travel": "/travel",
    "booking": "/booking",
    "logout": "/logout",
    "verifytoken": "/verifytoken",
    "tfa": "/2fa",
    "login": "/login",
    "register": "/register",
    "getbusavailability": "/getbusavailability",
    "getbooking": "/getbooking",
    "makebooking": "/makebooking",
    "makepayment": "/makepayment",
    "getcities": "/getcities",
    "getprovinces": "/getprovinces",
    "loadbeaches": "/loadbeaches",
    "loadnationalpark": "/loadnationalpark",
    "getcitiesbyprovince": "/getcitiesbyprovince",
    "deletebooking": "/deletebooking",
    "sendticket": "/sendticket",
    "icon": "/favicon.ico"
};

app.use(function(req, res, next) {
    const turl = req.url;
    for (var key in urls) {
        if (urls[key] == turl) {
            next();
            return;
        }
    }
    res.status(301).set("Location", "/home").end();
});

app.get(urls.icon, function(req, res) {
    res.status(200).sendFile('images/favicon.png', rootFolder);
});

app.get(urls.home, function(req, res) {
    res.status(200).sendFile('html/home.html', rootFolder);
});

app.get(urls.travel, function(req, res) {
    res.status(200).sendFile('html/travel.html', rootFolder);
});

app.get(urls.booking, function(req, res) {
    res.status(200).sendFile('html/booking.html', rootFolder);
});

app.post(urls.logout, function(req, res) {
    res.cookie("jwtToken", "");
    res.status(200).json({ "successMsg": "User logged out successfully" });
});

// to get cities by province
app.post(urls.getcitiesbyprovince, urlEncodedParser, function(req, res) {
    var provinceId = req.body.provinceId;
    const provinceCityUrl = "/canadatourism/api/cities/getcities/" + encodeURIComponent(provinceId);

    sendHttpRequest(touristPlaceServer.host, touristPlaceServer.port, provinceCityUrl, null,
        "GET",
        function(error, result) {
            if (error) {
                console.log("Verification failed (client side) : " + error);
                res.status(500).json({ "errorMsg": "Failed to retrieve bus availability. Try again later." });
            } else {
                if (result.status == 200) {
                    res.status(200).json(result);
                } else {
                    var statusCode = 500;
                    if (result.status >= 400 && result.status <= 510) {
                        statusCode = result.status;
                    }
                    console.log("Verification failed (server side): " + JSON.stringify(result));
                    res.status(statusCode).json(result);
                }
            }
        });
})

// to get national park by city
app.post(urls.loadnationalpark, urlEncodedParser, function(req, res) {
    var cityId = req.body.cityId;
    const nationalParkUrl = "/canadatourism/api/national-parks/city/" + encodeURIComponent(cityId) + "/NationalPark";

    sendHttpRequest(touristPlaceServer.host, touristPlaceServer.port, nationalParkUrl, null,
        "GET",
        function(error, result) {
            if (error) {
                console.log("Verification failed (client side) : " + error);
                res.status(500).json({ "errorMsg": "Failed to retrieve bus availability. Try again later." });
            } else {
                if (result.status == 200) {
                    res.status(200).json(result);
                } else {
                    var statusCode = 500;
                    if (result.status >= 400 && result.status <= 510) {
                        statusCode = result.status;
                    }
                    console.log("Verification failed (server side): " + JSON.stringify(result));
                    res.status(statusCode).json(result);
                }
            }
        });
})


// to get beaches by city
app.post(urls.loadbeaches, urlEncodedParser, function(req, res) {
    var cityId = req.body.cityId;
    const beachesUrl = "/canadatourism/api/beaches/city/" + encodeURIComponent(cityId) + "/beaches";

    sendHttpRequest(touristPlaceServer.host, touristPlaceServer.port, beachesUrl, null,
        "GET",
        function(error, result) {
            if (error) {
                console.log("Verification failed (client side) : " + error);
                res.status(500).json({ "errorMsg": "Failed to retrieve bus availability. Try again later." });
            } else {
                if (result.status == 200) {
                    res.status(200).json(result);
                } else {
                    var statusCode = 500;
                    if (result.status >= 400 && result.status <= 510) {
                        statusCode = result.status;
                    }
                    console.log("Verification failed (server side): " + JSON.stringify(result));
                    res.status(statusCode).json(result);
                }
            }
        });
})

app.post(urls.verifytoken, urlEncodedParser, function(req, res) {
    const cookies = req.headers.cookie;
    if (cookies) {
        var jwtToken = "";
        var email = "";
        const cookieArray = cookies.split(";");
        for (var index in cookieArray) {
            const cookie = cookieArray[index];
            if (cookie.split("=")[0].trim() == "jwtToken") {
                jwtToken = cookie.split("=")[1];
            }
            if (cookie.split("=")[0].trim() == "email") {
                email = cookie.split("=")[1];
                email = decodeURIComponent(email);
            }
        }
        if (jwtToken && email) {
            const param = email;
            sendHttpRequest(userServiceServer.host, userServiceServer.port, verifyTokenUrl, param,
                "POST",
                function(error, result) {
                    if (error) {
                        console.log("Verification failed (client side) : " + error);
                        res.status(500).json({ "errorMsg": "Failed to verify. Try again later." });
                    } else {
                        if (result.status == 200) {
                            res.status(200).json(result);
                        } else {
                            var statusCode = 500;
                            if (result.status >= 400 && result.status <= 510) {
                                statusCode = result.status;
                            }
                            console.log("Verification failed (server side): " + JSON.stringify(result));
                            res.status(statusCode).json(result);
                        }
                    }
                }, jwtToken, false);
        } else {
            res.status(401).json({ "errorMsg": "User not logged in" });
        }
    } else {
        res.status(401).json({ "errorMsg": "User not logged in" });
    }
});

//delete booking

app.post(urls.deletebooking, urlEncodedParser, function(req, res) {
    console.log("Delete Booking");
    var bookingId = req.body.bookingId;
    const deleteBookingUrl = "/canadatourism/api/transaction/delete/" + encodeURIComponent(bookingId);
    var jwtToken = getCookie(req, "jwtToken");
    if (!jwtToken) {
        res.status(401).send({ "errorMsg": "Unauthorized request" });
        return;
    }

    sendHttpRequest(bookingServer.host, bookingServer.port, deleteBookingUrl, null,
        "POST",
        function(error, result) {
            if (error) {
                console.log("Request failed (client side) : " + error);
                res.status(500).json({ "errorMsg": "Failed to retrieve bookings. Try again later." });
            } else {
                if (result.status == 200) {
                    res.status(200).json(result);
                } else {
                    var statusCode = 500;
                    if (result.status >= 400 && result.status <= 510) {
                        statusCode = result.status;
                    }
                    console.log("Request failed (server side): " + JSON.stringify(result));
                    res.status(statusCode).json(result);
                }
            }
        }, jwtToken);
});

app.post(urls.sendticket, urlEncodedParser, function(req, res) {
    console.log("Send Ticket");
    var jwtToken = getCookie(req, "jwtToken");
    var bookingId = req.body.bookingId;
    if (!jwtToken) {
        res.status(401).send({ "errorMsg": "Unauthorized request" });
        return;
    }
    const getTicketUrl = "/canadatourism/api/ticket/all/" + encodeURIComponent(bookingId) + "/transaction";
    sendHttpRequest(bookingServer.host, bookingServer.port, getTicketUrl, null,
        "POST",
        function(error, result) {
            if (error) {
                console.log("Request failed (client side) : " + error);
                res.status(500).json({ "errorMsg": "Failed to retrieve bookings. Try again later." });
            } else {
                if (result.status == 200) {
                    res.status(200).json(result);
                } else {
                    var statusCode = 500;
                    if (result.status >= 400 && result.status <= 510) {
                        statusCode = result.status;
                    }
                    console.log("Request failed (server side): " + JSON.stringify(result));
                    res.status(statusCode).json(result);
                }
            }
        }, jwtToken);
});

app.post(urls.tfa, urlEncodedParser, function(req, res) {
    const token = req.body.token;

    if (!token || token.trim().length == 0) {
        res.status(400).json({ "errorMsg": "Parameter token is missing" });
        return;
    }

    const cookies = req.headers.cookie;
    if (cookies) {
        var email = "";
        const cookieArray = cookies.split(";");
        for (var index in cookieArray) {
            const cookie = cookieArray[index];
            if (cookie.split("=")[0].trim() == "email") {
                email = cookie.split("=")[1];
                email = decodeURIComponent(email);
            }
        }

        const verify2faUrl = "/canadatourism/api/customer/validate2FA/" + encodeURIComponent(email) + "/" + token;

        sendHttpRequest(userServiceServer.host, userServiceServer.port, verify2faUrl, null,
            "POST",
            function(error, result) {
                if (error) {
                    console.log("2FA failed (client side) : " + error);
                    res.status(500).json({ "errorMsg": "Failed to Authenticate using 2FA. Try again later." });
                } else {
                    if (result.status == 200) {
                        res.cookie("jwtToken", result.data.token);
                        res.cookie("email", email);
                        res.status(200).json(result);
                    } else {
                        var statusCode = 500;
                        if (result.status >= 400 && result.status <= 510) {
                            statusCode = result.status;
                        }
                        console.log("2FA failed (server side): " + JSON.stringify(result));
                        res.status(statusCode).json(result);
                    }
                }
            });
    } else {

    }

});

app.post(urls.login, urlEncodedParser, function(req, res) {
    const email = req.body.email;
    const password = req.body.password;

    if (!email || email.trim().length == 0) {
        res.status(400).json({ "errorMsg": "Parameter email is missing in request" });
        return;
    }

    if (!emailRegex.test(email)) {
        res.status(400).json({ "errorMsg": "Invalid email address" });
        return;
    }

    if (!password || password.length == 0) {
        res.status(400).json({ "errorMsg": "Parameter password is missing in request" });
        return;
    }

    if (password.length < 8) {
        res.status(400).json({ "errorMsg": "Invalid password. Should be 8 characters minimum." });
        return;
    }


    const loginParam = { "username": email, "password": password };

    sendHttpRequest(userServiceServer.host, userServiceServer.port, loginUrl, loginParam,
        "POST",
        function(error, result) {
            if (error) {
                console.log("Authentication failed (client side) : " + error);
                res.status(500).json({ "errorMsg": "Failed to Authenticate. Try again later." });
            } else {
                if (result.status == 200) {
                    res.cookie("email", email);
                    res.status(200).json(result);
                } else {
                    var statusCode = 500;
                    if (result.status >= 400 && result.status <= 510) {
                        statusCode = result.status;
                    }
                    console.log("Authentication failed (server side): " + JSON.stringify(result));
                    res.status(statusCode).json(result);
                }
            }
        });
});

app.post(urls.register, urlEncodedParser, function(req, res) {
    const fname = req.body.fname;
    const lname = req.body.lname;
    const mobile = req.body.mobile;
    const email = req.body.email;
    const password = req.body.password;

    if (!fname || fname.trim().length == 0) {
        res.status(400).json({ "errorMsg": "Parameter full name is missing in request" });
        return;
    }
    if (!lname || lname.trim().length == 0) {
        res.status(400).json({ "errorMsg": "Parameter last name is missing in request" });
        return;
    }
    if (!mobile || !phoneRegex.test(mobile)) {
        res.status(400).json({ "errorMsg": "Parameter mobile number is invalid" });
        return;
    }
    if (!email || !(emailRegex.test(email))) {
        res.status(400).json({ "errorMsg": "Parameter email is invalid" });
        return;
    }
    if (email.split("@")[1] != "dal.ca") {
        res.status(400).json({ "errorMsg": "Only dal.ca email is permitted for registration" });
        return;
    }
    const error = validatePassword(password);
    if (error) {
        res.status(400).json({ "errorMsg": "Parameter password is invalid" });
        return;
    }

    const regParam = {
        "customerFname": fname,
        "customerLname": lname,
        "customerNum": mobile,
        "customerEmail": email,
        "customerPassword": password,
        "is2faEnabled": true
    };

    sendHttpRequest(userServiceServer.host, userServiceServer.port, regUrl, regParam,
        "POST",
        function(error, result) {
            if (error) {
                console.log("Registration failed (client side) : " + error);
                res.status(500).json({ "errorMsg": "Failed to register. Try again later." });
            } else {
                if (result.status == 201) {
                    res.status(200).json(result);
                } else {
                    var statusCode = 500;
                    if (result.status >= 400 && result.status <= 510) {
                        statusCode = result.status;
                    }
                    console.log("Registration failed (server side): " + JSON.stringify(result));
                    res.status(statusCode).json(result);
                }
            }
        });
});

app.post(urls.getbusavailability, urlEncodedParser, function(req, res) {
    const busBookingBody = {
        "journeyDate": req.body.journeyDate,
        "sourceLocationId": 0,
        "destinationLocationId": 0
    };
    sendHttpRequest(bookingServer.host, bookingServer.port, busAvailabiltyUrl, busBookingBody,
        "POST",
        function(error, result) {
            if (error) {
                console.log("Verification failed (client side) : " + error);
                res.status(500).json({ "errorMsg": "Failed to retrieve bus availability. Try again later." });
            } else {
                if (result.status == 200) {
                    res.status(200).json(result);
                } else {
                    var statusCode = 500;
                    if (result.status >= 400 && result.status <= 510) {
                        statusCode = result.status;
                    }
                    console.log("Verification failed (server side): " + JSON.stringify(result));
                    res.status(statusCode).json(result);
                }
            }
        });
});

app.get(urls.getbooking, urlEncodedParser, function(req, res) {
    var jwtToken = getCookie(req, "jwtToken");
    if (!jwtToken) {
        res.status(401).send({ "errorMsg": "Unauthorized request" });
        return;
    }
    sendHttpRequest(bookingServer.host, bookingServer.port, getBookingUrl, null,
        "POST",
        function(error, result) {
            if (error) {
                console.log("Request failed (client side) : " + error);
                res.status(500).json({ "errorMsg": "Failed to retrieve bookings. Try again later." });
            } else {
                if (result.status == 200) {
                    res.status(200).json(result);
                } else {
                    var statusCode = 500;
                    if (result.status >= 400 && result.status <= 510) {
                        statusCode = result.status;
                    }
                    console.log("Request failed (server side): " + JSON.stringify(result));
                    res.status(statusCode).json(result);
                }
            }
        }, jwtToken);
});

app.post(urls.makepayment, urlEncodedParser, function(req, res) {
    var jwtToken = getCookie(req, "jwtToken");
    if (!jwtToken) {
        res.status(401).send({ "errorMsg": "Unauthorized request" });
        return;
    }
    var param = {
        "transactionAmount": req.body.bookingAmount,
        "bookingId": req.body.bookingId,
        "cardNumber": req.body.cardNumber
    };
    sendHttpRequest(bookingServer.host, bookingServer.port, paymentUrl, param,
        "POST",
        function(error, result) {
            if (error) {
                console.log("Payment failed (client side) : " + error);
                res.status(500).json({ "errorMsg": "Failed to make payment. Try again later." });
            } else {
                if (result.status == 200) {
                    res.status(200).json(result);
                } else {
                    var statusCode = 500;
                    if (result.status >= 400 && result.status <= 510) {
                        statusCode = result.status;
                    }
                    console.log("Payment failed (server side): " + JSON.stringify(result));
                    res.status(statusCode).json(result);
                }
            }
        }, jwtToken);
});

app.post(urls.makebooking, urlEncodedParser, function(req, res) {

    const bookingBody = {
        "busId": req.body.busId,
        "journeyDate": req.body.journeyDate,
        "sourceLocationId": req.body.sourceLocationId,
        "destinationLocationId": req.body.destinationLocationId,
        "noOfSeats": req.body.noOfSeats
    };

    const cookies = req.headers.cookie;
    if (cookies) {
        var jwtToken = "";
        var email = "";
        const cookieArray = cookies.split(";");
        for (var index in cookieArray) {
            const cookie = cookieArray[index];
            if (cookie.split("=")[0].trim() == "jwtToken") {
                jwtToken = cookie.split("=")[1];
            }
        }
    } else {
        res.status(401).json({ "errorMsg": "User not logged in" });
    }


    if (jwtToken) {
        sendHttpRequest(bookingServer.host, bookingServer.port, bookingUrl, bookingBody,
            "POST",
            function(error, result) {
                if (error) {
                    console.log("Verification failed (client side) : " + error);
                    res.status(500).json({ "errorMsg": "Failed to make booking. Try again later." });
                } else {
                    if (result.status == 200) {
                        res.status(200).json(result);
                    } else {
                        var statusCode = 500;
                        if (result.status >= 400 && result.status <= 510) {
                            statusCode = result.status;
                        }
                        console.log("Verification failed (server side): " + JSON.stringify(result));
                        res.status(statusCode).json(result);
                    }
                }
            }, jwtToken, false);
    } else {
        res.status(401).json({ "errorMsg": "User not logged in" });
    }
});

app.get(urls.getcities, function(req, res) {
    sendHttpRequest(touristPlaceServer.host, touristPlaceServer.port, cities, null,
        "GET",
        function(error, result) {
            if (error) {
                console.log("Request failed (client side) : " + error);
                res.status(500).json({ "errorMsg": "Failed to retrieve bookings. Try again later." });
            } else {
                if (result.status == 200) {
                    res.status(200).json(result);
                } else {
                    var statusCode = 500;
                    if (result.status >= 400 && result.status <= 510) {
                        statusCode = result.status;
                    }
                    console.log("Request failed (server side): " + JSON.stringify(result));
                    res.status(statusCode).json(result);
                }
            }
        });
});

app.get(urls.getprovinces, function(req, res) {
    sendHttpRequest(touristPlaceServer.host, touristPlaceServer.port, provinces, null,
        "GET",
        function(error, result) {
            if (error) {
                console.log("Request failed (client side) : " + error);
                res.status(500).json({ "errorMsg": "Failed to retrieve bookings. Try again later." });
            } else {
                if (result.status == 200) {
                    res.status(200).json(result);
                } else {
                    var statusCode = 500;
                    if (result.status >= 400 && result.status <= 510) {
                        statusCode = result.status;
                    }
                    console.log("Request failed (server side): " + JSON.stringify(result));
                    res.status(statusCode).json(result);
                }
            }
        });
});

const server = https.createServer(options, app).listen(appPort, function(error) {
    if (error) {
        console.log("Failed to start server: " + error);
        return;
    }
    console.log("Server started");
});

process.on("SIGINT", function(signal, code, error) {
    if (error) {
        console.log(error);
    }
    server.close(() => {
        console.log("Server killed");
        process.exit(0);
    });
});

function sendHttpRequest(host, port, url, params, method, callback, jwtToken, notJson) {
    var options = {
        "host": host,
        "port": port,
        "path": url,
        "method": method,
        "headers": {}
    };

    if (params) {
        if (notJson) {
            options.headers = {
                'Content-Type': 'application/x-www-form-urlencoded',
            };
        } else {
            options.headers = {
                'Content-Type': 'application/json',
            };
        }
    }

    if (jwtToken) {
        options.headers["Authorization"] = "Bearer " + jwtToken;
    }

    var error;
    var resp = {};

    var request = http.request(options, function(response) {
        resp.status = response.statusCode;
        resp.data = '';
        response.on('data', function(data) {
            resp.data += data;
        });
        response.on('end', function() {
            try {
                resp.data = JSON.parse(resp.data);
            } catch (e) {}
            callback(error, resp);
        });
    }).on('error', function(error) {
        console.log("Error sending request to '" + host + ":" + port + url + "' : " + error);
        callback(error, resp);
    });

    if (params) {
        if (notJson) {
            request.write(params);
        } else {
            request.write(JSON.stringify(params));
        }
    }
    request.end();
}

function validatePassword(password) {
    var error = "Password must contain: ";
    var valid = true;
    if (!upperCaseRegex.test(password)) {
        error += "<br/>- 1 Uppercase letter";
        valid = false;
    }
    if (!lowerCaseRegex.test(password)) {
        error += "<br/>- 1 Lowercase letter";
        valid = false;
    }
    if (!numberRegex.test(password)) {
        error += "<br/>- 1 Number";
        valid = false;
    }
    if (!specialCharRegex.test(password)) {
        error += "<br/>- 1 Special character";
        valid = false;
    }
    if (password.length < 8) {
        error += "<br/>- 8 characters";
        valid = false;
    }
    if (!valid) {
        return error;
    } else {
        return "";
    }
}

function getCookie(req, name) {
    const cookies = req.headers.cookie;
    if (cookies) {
        var cookie = "";
        const cookieArray = cookies.split(";");
        for (var index in cookieArray) {
            cookie = cookieArray[index];
            if (cookie.split("=")[0].trim() == name) {
                return cookie.split("=")[1];
            }
        }
    }
    return null;
}
