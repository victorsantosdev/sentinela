//Events object
function codigoslei(){};
//exported through exports.Terrenos
exports.codigoslei = codigoslei;

//DB client is passed and inititialized from outside...
exports.setAndConnectClient = function(_client){
  client = _client;  //assign it to the module's client var
  client.connect();  //connect to DB...
}

//GET ALL CODIGOS LEI!
codigoslei.prototype.getAllCodigosLei = function(callback){
   var allCodigosLei = [];
   var query =  client.query('SELECT * FROM codigoslei', function(err, result){
      allCodigosLei = result.rows;
      //return an empty object if no events exist
      if(allCodigosLei.length == 0) {
         callback([]);
      }else callback(allCodigosLei);
    });
}
