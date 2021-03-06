const express = require('express');
const https = require("https"),
    fs = require("fs");

const options = {
    key: fs.readFileSync("./key.key"),
    cert: fs.readFileSync("./cert.pem"),
    passphrase: "bmserv"
};

const app = express();
const serverPort = 8443;

app.use(function(req, res, next) {
    res.setHeader('Access-Control-Allow-Origin', req.header('origin') || "*");
    next();
});
app.use(express.static('static'));

const server = https.createServer(options, app).listen(serverPort, function(error) {
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