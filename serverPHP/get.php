<?php 

$id   = $_POST['id'];
$bmac   = $_POST['bmac'];
$loc  = $_POST['loc'];
$blueFound   = $_POST['blueFound'];
$timeStamp = $_POST['timeStamp'];
 
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
  $sql = "INSERT INTO loca (id, bmac, loc, blueFound, timeStamp)
  VALUES ('$id', '$bmac', '$loc', '$blueFound', '$timeStamp')";

  if (mysqli_query($conn, $sql)) {
    $last_id = mysqli_insert_id($conn);
    echo "New record created successfully. Last inserted ID is: " . $last_id;
  } else {
    echo "Error: " . $sql . "<br>" . mysqli_error($conn);
  }
}

mysqli_close($conn);
 
 
 
 ?>