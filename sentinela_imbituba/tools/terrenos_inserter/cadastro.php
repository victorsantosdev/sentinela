<?php
//include 'debuglib.php';
//mostra erros no código!
error_reporting(E_ALL);
ini_set('display_errors', '1');

/**
    Make a nested path , creating directories down the path
    Recursion !!
*/
function make_path($path)
{
    $dir = pathinfo($path , PATHINFO_DIRNAME);
     
    if( is_dir($dir) )
    {	//echo "Diretório existe";
        return true;
    }
    else
    {
        if( make_path($dir) )
        {
            if( mkdir($dir) )
            {
		//echo "Diretório criado";
                chmod($dir , 0777);
                return true;
            }
        }
    }
    //echo "Erro ao criar diretório"; 
    return false;
}



//recebe informações do formulário

//variaveis
$proprietario = $_POST['proprietario']; 
$endereco = $_POST['endereco'];
$numero = intval($_POST['numero']); 
$bairro = $_POST['bairro'];
$cidade = $_POST['cidade'];
$estado = $_POST['estado'];
$topografia = $_POST['topografia'];
$area = floatval(str_replace(',', '.', $_POST['area']));
$configuracao = $_POST['configuracao'];
$situacaocadastral = $_POST['situacaocadastral'];


if (ctype_alpha($proprietario) == FALSE) {
	exit("Nome do proprietário inválido");
}

if (ctype_alpha($endereco) == FALSE) {
	exit("Endereço inválido");
}

if (is_numeric($numero) == FALSE) {
	exit("Número inválido");
}

if (ctype_alpha($bairro) == FALSE) {
	exit("Bairro inválido");
}

if (ctype_alpha($cidade) == FALSE) {
	exit("Cidade inválido");
}

if (ctype_alpha($estado) == FALSE) {
	exit("Estado inválido");
}
if (ctype_alpha($topografia) == FALSE) {
	exit("Topografia inválida");
}

if (is_float($area) == FALSE) {
	exit("Area inválida");
}

if (ctype_alpha($configuracao) == FALSE) {
	exit("Configuracao do terreno inválida");
}

if (ctype_alpha($situacaocadastral) == FALSE) {
	exit("Situação cadastral inválida");
}


//remover espaços antes e depois 
//trim($proprietario = $_POST['proprietario']); 

trim($proprietario);
trim($endereco); 
trim($numero);
trim($bairro);
trim($cidade); 
trim($estado); 
trim($topografia);
trim($area);
trim($configuracao); 
trim($situacaocadastral);


//conecta-se ao banco de dados
// Connecting, selecting database
$dbconn = pg_connect("host=localhost dbname=sentinela user=postgres password=postgres")
    or die('Could not connect to database: ' . pg_last_error());

//$mysql['servidor'] = "localhost";
//$mysql['usuario'] = "postgres";
//$mysql['senha'] = "postgres";
//$mysql['banco'] = "sentinela";
//$table = $mysql['banco'];
//$con = mysql_connect($mysql['servidor'],$mysql['usuario'],$mysql['senha']);
$table_terreno = "terreno";
if($dbconn == NULL)
{
	
///********************************* HTML PARA MOSTRAR ERRO DE CONEXAO COM O SERVIDOR *************************************************************	
	?>
    	<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link rel="stylesheet" type="text/css" href="resbusca.css" />
		<title>Resultado</title>
		<link rel="stylesheet"  href="resbusca.css" type="text/css" />
<script type="text/JavaScript" src="/curvycorners-2.1/curvycorners.js"></script>

<style>

.aviso {
	
    margin: 0.5in auto;
    color:  #333;
    width: 60%;
    padding: 5px;
    text-align: center;
    background-color:  #CCC;
    border: 2px solid  #333;
    /* Do rounding (native in Firefox and Safari) */
    -webkit-border-radius: 20px;
    -moz-border-radius: 20px;
}

}
</style>

	</head>
	<body>
    <div class= "aviso"><table cellpadding = "0" cellspacing="0" border="0" width="800" align="center">
    		<tr><td width="600" height="200" align="center" ><? echo "ERRO AO CONECTAR COM O SERVIDOR!"; ?></td></tr></table></div>

	</body>
</html>
	
<?php
//************************************************************************************************************************	
	
	exit();
}
else
{

///********************************* HTML PARA MOSTRAR ERRO DE CONEXAO COM O BD *************************************************************	
		
?>
<!--
    	<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link rel="stylesheet" type="text/css" href="resbusca.css" />
		<title>Resultado</title>
		<link rel="stylesheet"  href="resbusca.css" type="text/css" />
<script type="text/JavaScript" src="/curvycorners-2.1/curvycorners.js"></script>

<style>

.aviso {
	
    margin: 0.5in auto;
    color:  #333;
    width: 60%;
    padding: 5px;
    text-align: center;
    background-color:  #CCC;
    border: 2px solid  #333;
    /* Do rounding (native in Firefox and Safari) */
    -webkit-border-radius: 20px;
    -moz-border-radius: 20px;
}

}
</style>

	</head>
	<body>
    <div class= "aviso"><table cellpadding = "0" cellspacing="0" border="0" width="800" align="center">
    		<tr><td width="600" height="200" align="center" ><? echo "ERRO AO ACESSAR A BASE DE DADOS!"; ?></td></tr></table></div>

	</body>
</html>
-->
<?php
///********************************************* *************************************************************	

}
//tratamento de imagens

$foto = $_FILES['foto']['name'];
if (make_path('/imagens') == TRUE) {
	$image_path = "imagens/";
} else {
	exit("Falha ao criar diretório de imagens");
}

$foto_tmp = $_FILES['foto']['tmp_name'];

//envia a foto pra a pasta imagens

move_uploaded_file($foto_tmp,$image_path.$foto);

//monta a query que irá gravar as informações 
$res=pg_query("SELECT nextval('terreno_terreno_id_seq') as key");
$row=pg_fetch_array($res, 0);
$key=$row['key'];

$full_img_path = $image_path.$foto;
//$key = 1;
// now we have the serial value in $key, let's do the insert
//pg_query("INSERT INTO foo (key, foo) VALUES ($key, 'blah blah')");
//$next_id_query = "SELECT nextval('terreno_terreno_id_seq')";
//$next_id = pg_query($next_id_query);

//$query = "INSERT INTO terreno VALUES('$key','$proprietario','$endereco','$numero','$bairro','$cidade','$estado','$topografia','$area','$configuracao','$situacaocadastral','$image_path')"; 
$query = "INSERT INTO terreno VALUES('$key','$proprietario','$endereco','$numero','$bairro','$cidade','$estado','$topografia','$area','$configuracao','$situacaocadastral','$full_img_path')"; 


//grava as informações 
$result  = pg_query($query); 
$caminho="./";

//redimensiona imagem e insere a marca dagua da loja!/
include('m2brimagem.class.php');
$oImg = new m2brimagem($image_path.$foto);
$oImg->redimensiona(400,300,'crop');
$oImg->marca('watermark.png',5,5,NULL);
$oImg->grava($image_path.$foto,100);
			
//conta o número de colunas afetadas. Se for 1, a gravação foi efetuada 
$num_linha = pg_affected_rows($result);
 
if($num_linha == 1) {
//----------------------- HTML TERRENO CADASTRADO COM SUCESSO	
?>

<!--MOSTRA O PRODUTO CADSATRADO-->
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link rel="stylesheet" type="text/css" href="resbusca.css" />
		<title>Resultado</title>
		<link rel="stylesheet"  href="resbusca.css" type="text/css" />
<script type="text/JavaScript" src="curvycorners-2.1/curvycorners.js"></script>

<style>

.titulo {
	
    margin: 0.5in auto;
    color:  #333;
    width: 60%;
    padding: 5px;
    text-align: center;
    background-color:  #CCC;
    border: 2px solid  #333;
    /* Do rounding (native in Firefox and Safari) */
    -webkit-border-radius: 20px;
    -moz-border-radius: 20px;
}

.geral {
	
    margin: 0.5in auto;
    color:  #333;
    width: 60%;
    padding: 10px;
    text-align: center;
    background-color:  #CCC;
    border: 3px solid  #333;
    /* Do rounding (native in Firefox and Safari) */
    -webkit-border-radius: 20px;
    -moz-border-radius: 20px;
}


</style>

	</head>
	<body>
    		
		<div class="geral"><table cellpadding="0" cellspacing="0" border="0" width="800" align="center">
			<tr><td width="800" height="60" align="center"><?php echo "TERRENO CADASTRADO COM SUCESSO!";?></td></tr>
				<td width="400" height="300"><img src="<?php echo $caminho.$image_path.$foto; ?>" border="1"  width="400" height="300" alt="" /></td>
				<td width="400" height="300"><table cellpadding="2" cellspacing="2" border="0" width="400">
					<tr>
						<td width="250" height="20">Proprietario:</td>
						<td width="300"><?php echo $proprietario ?></td>
					</tr>
					<tr>
						<td height="20">Endereco:</td>
						<td><?php echo $endereco; ?></td>
					</tr>
					<tr>
						<td height="20">Bairro:</td>
						<td><?php echo $bairro; ?></td>
					</tr>
					<tr>
						<td height="20">Número:</td>
						<td><?php echo $numero; ?></td>
					</tr>
					<tr>
						<td height="20">Cidade:</td>
						<td><?php echo $cidade; ?></td>
					</tr>
					<tr>
                    <td height="20">Estado:</td>
						<td><?php echo $estado; ?></td>
					</tr>
					<tr>
						<td height="20">Topografia:</td>
						<td><?php echo $topografia; ?></td>
					</tr>
					<tr>
						<td height="20">Configuração:</td>
						<td><?php echo $configuracao; ?></td>
					</tr>
					<tr>
						<td height="20">Situação cadastral:</td>
						<td><?php echo $situacaocadastral; ?></td>
				
				</table></td>
			</tr></table>
			<tr><td width="600" height="200" align="center" >
			<br>
			<br>
			<?php
			
			echo "<a href=javascript:history.back()>VOLTAR...</a>"; ?></td></tr>
			
		</div>
	</body>
</html>
    
    
<?php	
 //link para a página anterior  
 
 
//--------------------------------------------------------------------------------------------------------------------------------
                    }
					
else {
///********************************* HTML PARA MOSTRAR ERRO DE CADASTRO *************************************************************	
		
		?>
    	<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link rel="stylesheet" type="text/css" href="resbusca.css" />
		<title>Resultado</title>
		<link rel="stylesheet"  href="resbusca.css" type="text/css" />
<script type="text/JavaScript" src="/sistema/curvycorners-2.1/curvycorners.js"></script>

<style>

.aviso {
	
    margin: 0.5in auto;
    color:  #333;
    width: 60%;
    padding: 5px;
    text-align: center;
    background-color:  #CCC;
    border: 2px solid  #333;
    /* Do rounding (native in Firefox and Safari) */
    -webkit-border-radius: 20px;
    -moz-border-radius: 20px;
}

}
</style>

	</head>
	<body>
    <div class= "aviso"><table cellpadding = "0" cellspacing="0" border="0" width="800" align="center">
    		<tr><td width="600" height="200" align="center" >
			<?php 
			echo "<p align='center'>FALHA AO CADASTRAR TERRENO!</p> <br>"; 
			echo "<p align='center'>VOLTE E TENTE CADASTRAR NOVAMENTE...</p> <br>";
			//link para a página anterior
			echo "<a href=javascript:history.back()>VOLTAR...</a>";  
			?>
			</td></tr></table></div>

	</body>
</html>
	
<?php
///**********************************************************************************************************		
	


//fecha a conexão 
					}
pg_close($dbconn); ?> 