<?php 


 
$servername = "localhost";
$username = "root";
$password = "";
$dbname = "data";



// Create connection
$conn = mysqli_connect($servername, $username, $password, $dbname);
// Check connection
if (!$conn) {
  die("Connection failed: " . mysqli_connect_error());
}

if(isset($_POST['id'])) {
	$id   = $_POST['id'];
    $bmac   = $_POST['bmac'];
    $loc  = $_POST['loc'];
    $blueFound   = $_POST['blueFound'];
	$timeStamp = $_POST['timeStamp'];
	
	
$sql = "SELECT id, bmac, loc,blueFound,timeStamp FROM loca";
$result = mysqli_query($conn, $sql);

if (mysqli_num_rows($result) > 0) {
  // output data of each row
  



    while($row = mysqli_fetch_assoc($result)) {
	  
	//  echo $row["id"];
    if($row["id"] == $id){
		
		
	$sql = "UPDATE loca SET bmac='".$bmac."', loc='".$loc."' , blueFound='".$blueFound."', timeStamp='".$timeStamp."' WHERE id='".$id."'";

	if (mysqli_query($conn, $sql)) {
	 // echo "Updated successfully".PHP_EOL;
	} else {
	 // echo "Error updating record: " . mysqli_error($conn).PHP_EOL;
	}
	
	
	
	}else{
	  
	  
	  
$sqll = "INSERT INTO loca (id, bmac, loc, blueFound, timeStamp) VALUES ('$id', '$bmac', '$loc', '$blueFound', '$timeStamp')";

if (mysqli_query($conn, $sqll)) {
  $last_id = mysqli_insert_id($conn);
 // echo "Last Insert =: " . $last_id.PHP_EOL;
} else {
  //echo "Error: " . $sql . "<br>" . mysqli_error($conn).PHP_EOL ."</br>";
}
  }
  }
  
}else{


$sqll = "INSERT INTO loca (id, bmac, loc, blueFound, timeStamp) VALUES ('$id', '$bmac', '$loc', '$blueFound', '$timeStamp')";

if (mysqli_query($conn, $sqll)) {
  $last_id = mysqli_insert_id($conn);
 // echo "Last Insert =: " . $last_id.PHP_EOL;
} else {
  //echo "Error: " . $sql . "<br>" . mysqli_error($conn).PHP_EOL ."</br>";
}

	

}


}

		


$sql = "SELECT id, bmac, loc,blueFound,timeStamp FROM loca";
$result = mysqli_query($conn, $sql);

if (mysqli_num_rows($result) > 0) {
  // output data of each row
  
  $retarr = array();
  while($row = mysqli_fetch_assoc($result)) {
	  $ra['id'] =$row["id"];
	  $ra['bmac'] =$row["bmac"];
	  $ra['loc'] =$row["loc"];
	  array_push($retarr , $ra);
    //echo "id=" . $row["id"]. "*bmac=" . $row["bmac"]. "*loc=" . $row["loc"] ."-" .PHP_EOL ;
  }
  echo json_encode(array('data' =>$retarr));
} else {
  echo "0 results".PHP_EOL;
}

mysqli_close($conn);
 
 
 
 ?>