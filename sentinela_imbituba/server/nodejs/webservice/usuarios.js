//Events object
function usuarios(){};
//exported through exports.Terrenos
exports.usuarios = usuarios;

//DB client is passed and inititialized from outside...
exports.setAndConnectClient = function(_client){
  client = _client;  //assign it to the module's client var
  client.connect();  //connect to DB...
}

//GET ALL USUARIOS!
usuarios.prototype.getAllUsuarios = function(callback){
   var allUsuarios = [];
   var query =  client.query('SELECT * FROM usuario', function(err, result){
      allUsuarios = result.rows;
      //return an empty object if no events exist
      if(allUsuarios.length == 0) {
         callback([]);
      }else callback(allUsuarios);
    });
}
