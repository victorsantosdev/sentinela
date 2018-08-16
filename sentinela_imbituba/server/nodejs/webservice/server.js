//Define the port to listen to
//Include retify.js framework
//var restify = require('restify');
var express = require('express'), 
    server = express(),
    fs = require('fs');


var PORT = process.env.PORT || 30000;

/// Include the express body parser
server.configure(function () {
    server.use(express.bodyParser());
});

//Create the server

//Use bodyParser to read the body of incoming requests

console.log("listening "+PORT);

//Include db_conn file
var db_conn = require('./db_connection');

//TERRENOS RESOURCES
var terrenosResource = require('./terrenos');
terrenosResource.setAndConnectClient(db_conn.client);

//DEFINE THE URIs THE SERVER IS RESPONDING TO
server.get('/terrenos', function(req, res) {

    var terrenos = new terrenosResource.terrenos() ;

    //Get all terrenos from DB
    terrenos.getAllTerrenos(function(result){

        var allTerrenos = result;
        var terrenos_jsonObj = {};
        terrenos_jsonObj['terrenos'] = result;
        //If no events exist return 200 and and empty JSON
        if(allTerrenos.length == 0) {
            res.send([]);
            return;
        }else res.send(terrenos_jsonObj);
    });

});


//CODIGOSINFRACOES RESOURCES
var codigosLeiResource = require('./codigoslei');

//DEFINE THE URIs THE SERVER IS RESPONDING TO
server.get('/codigoslei', function(req, res) {

    var codigoslei = new codigosLeiResource.codigoslei() ;

    //Get all events from DB
    codigoslei.getAllCodigosLei(function(result){

        var allCodigosLei = result;
        var codigosLei_jsonObj = {};
        codigosLei_jsonObj['codigoslei'] = result;
        //If no events exist return 200 and and empty JSON
        if(allCodigosLei.length == 0) {
            res.send([]);
            return;
        }else res.send(codigosLei_jsonObj);
    });

});


//USUARIOS RESOURCES
var usuariosResource = require('./usuarios');

//DEFINE THE URIs THE SERVER IS RESPONDING TO
server.get('/usuarios', function(req, res) {

    var usuarios = new usuariosResource.usuarios() ;

    //Get all events from DB
    usuarios.getAllUsuarios(function(result){

        var allUsuarios = result;
        var usuarios_jsonObj = {};
        usuarios_jsonObj['usuarios'] = result;
        //If no events exist return 200 and and empty JSON
        if(allUsuarios.length == 0) {
            //res.send(200, []);
            res.send([]);
            return;
            //}else res.send(200, res_jsonObj);
    }else res.send(usuarios_jsonObj);
    });

    });

server.use(express.static(__dirname+'/app_imagensterrenos'));

//IMAGENS TERRENO RESOURCE
server.get("/timg", function (req, res) {
//server.get('/t1foto', function (req, res) {
    /* browser usage: http://localhost:30000/timg?path=/imagens/terreno1.jpg */
    var imagepath = __dirname + req.query.path;
    //var imagepath = __dirname + "/imagens/terreno1.jpg";
    var img = fs.readFileSync(imagepath);
    console.log("fetching image: ", imagepath);
    res.contentType = 'image/jpg';
    res.end(img, 'binary');
});

server.listen(process.env.PORT || 30000);
