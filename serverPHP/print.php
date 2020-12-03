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
$keep = True;

while ($keep == True) {
    // Create connection
    $conn = mysqli_connect($servername, $username, $password, $dbname);
    // Check connection
    if (!$conn) {
    die("Connection failed: " . mysqli_connect_error());
    }

    time = time() #prende tempo in unix
    timeToPrint = time - 1000 #printa tutto quello che negli ultimi 1000 minuti
    #Prende select:
    $sql = "SELECT id, bmac, loc,blueFound,timeStamp FROM loca WHERE timeStamp < timeToPrint";
    $result = mysqli_query($conn, $sql);

    if (mysqli_num_rows($result) > 0) {
    // output data of each row
    while($row = mysqli_fetch_assoc($result)) {
        echo "id: " . $row["id"]. " - bmac: " . $row["bmac"]. " loc: " . $row["loc"]. "<br>";
    }
    } else {
    echo "0 results";
    }

    mysqli_close($conn);

    sleep(1) #dorme 1 secondo 
}
 
 
 ?>