//Start local storage
var gameSettings = new LocalStorage({ debug: false });

var i, j;

//First selection X and Y
var firstSelectionX, firstSelectionY;

//Second selection X and Y
var secondSelectionX, secondSelectionY;

//Direction X and Y
var directionX, directionY;

var playersTurn;
var IsOver;
var Size = 9;
var Start = 1;
var pauseGame = 0;

//Create a grid with the grade values for best move
fieldValue = new Array(Size);
fieldValue[0] = new Array(0, 0, 0, 0, 45, 45, 45, 45, 45);
fieldValue[1] = new Array(0, 0, 0, 45, 50, 50, 50, 50, 45);
fieldValue[2] = new Array(0, 0, 45, 50, 53, 53, 53, 50, 45);
fieldValue[3] = new Array(0, 45, 50, 53, 54, 54, 53, 50, 45);
fieldValue[4] = new Array(45, 50, 53, 54, 54, 54, 53, 50, 45);
fieldValue[5] = new Array(45, 50, 53, 54, 54, 53, 50, 45, 0);
fieldValue[6] = new Array(45, 50, 53, 53, 53, 50, 45, 0, 0);
fieldValue[7] = new Array(45, 50, 50, 50, 50, 45, 0, 0, 0);
fieldValue[8] = new Array(45, 45, 45, 45, 45, 0, 0, 0, 0);

//Setup players
IsPlayer = new Array(2);
IsPlayer[0] = false;
IsPlayer[1] = true;

//Game field array
gameField = new Array(Size);
for (i = 0; i < Size; i++) {
	gameField[i] = new Array(Size);
}

//Temporary Game Field array
tempField = new Array(Size);
for (i = 0; i < Size; i++) {
	tempField[i] = new Array(Size);
}

//Pieces to move
movementSet = new Array(6);
for (i = 0; i < 6; i++) {
	movementSet[i] = new Array(2);
}

//Game grid mapping to find best move
CI = new Array(24);
CJ = new Array(24);
CI[0] = new Array(4,-1, 0, 1);
CJ[0] = new Array(0, 1, 1, 0);
CI[1] = new Array(5,-1, 0);
CJ[1] = new Array(0, 1, 1);
CI[2] = new Array(6,-1, 0);
CJ[2] = new Array(0, 1, 1);
CI[3] = new Array(7,-1, 0);
CJ[3] = new Array(0, 1, 1);
CI[4] = new Array(8,-1,-1, 0);
CJ[4] = new Array(0, 0, 1, 1);
CI[5] = new Array(8,-1,-1);
CJ[5] = new Array(1, 0, 1);
CI[6] = new Array(8,-1,-1);
CJ[6] = new Array(2, 0, 1);
CI[7] = new Array(8,-1,-1);
CJ[7] = new Array(3, 0, 1);
CI[8] = new Array(8, 0,-1,-1);
CJ[8] = new Array(4,-1, 0, 1);
CI[9] = new Array(7, 0,-1);
CJ[9] = new Array(5,-1, 0);
CI[10] = new Array(6, 0,-1);
CJ[10] = new Array(6,-1, 0);
CI[11] = new Array(5, 0,-1);
CJ[11] = new Array(7,-1, 0);
CI[12] = new Array(4, 1, 0,-1);
CJ[12] = new Array(8,-1,-1, 0);
CI[13] = new Array(3, 1, 0);
CJ[13] = new Array(8,-1,-1);
CI[14] = new Array(2, 1, 0);
CJ[14] = new Array(8,-1,-1);
CI[15] = new Array(1, 1, 0);
CJ[15] = new Array(8,-1,-1);
CI[16] = new Array(0, 1, 1, 0);
CJ[16] = new Array(8, 0,-1,-1);
CI[17] = new Array(0, 1, 1);
CJ[17] = new Array(7, 0,-1);
CI[18] = new Array(0, 1, 1);
CJ[18] = new Array(6, 0,-1);
CI[19] = new Array(0, 1, 1);
CJ[19] = new Array(5, 0,-1);
CI[20] = new Array(0, 0, 1, 1);
CJ[20] = new Array(4, 1, 0,-1);
CI[21] = new Array(1, 0, 1);
CJ[21] = new Array(3, 1, 0);
CI[22] = new Array(2, 0, 1);
CJ[22] = new Array(2, 1, 0);
CI[23] = new Array(3, 0, 1);
CJ[23] = new Array(1, 1, 0);

//Player marble images
Pic = new Array(6);
for (i = 0; i < 6; i++) {
	Pic[i] = new Image();
	Pic[i].src = "./images/abalone" + i + ".png";
}

//Setup Player images and movement image
playerImage = new Array(2);
for (i = 0; i < 2; i++) {
	playerImage[i] = new Array(6);
}

//Setup movement images 
for (i = 0; i < 2; i++) {
	for (j = 0; j < 6; j++) {
		playerImage[i][j] = new Image();
		playerImage[i][j].src = "./images/abalone" + eval(i + 3) + j + ".png";
	}
}

//Set Starting player
//startPlayer bool
function SetStart(startPlayer) {
	Start = startPlayer;
}

//Set player type
//playerNumber int
//computerPlayer bool
function SetPlayer(playerNumber, computerPlayer) {
	IsPlayer[playerNumber] = computerPlayer;
	
	//Reset selections
	firstSelectionX = -1;
	firstSelectionY = -1;
	secondSelectionX = -1;
	secondSelectionY = -1;
	
	RefreshScreen();
}

function Init() {
	var ii, jj;

	for (jj = 0; jj < Size; jj++) {
		for (ii = 0; ii < Size; ii++) {
			if (ii + jj < 4 || ii + jj > 12) {
				gameField[ii][jj] = -2;
			} else {
				gameField[ii][jj] = -1;
			}
		}	
	}
	
	
	//Starting board layouts
	var boardSelection = document.getElementById("BoardLayouts");
	
	if (boardSelection.value == "Alien Attack") {
		AlienAttack();
	}
	if (boardSelection.value == "Belgian Daisy") {
		BelgianDaisy();
	}
	if (boardSelection.value == "Dutch Daisy") {
		DutchDaisy();
	}
	if (boardSelection.value == "Face To Face") {
		FaceToFace();
	}
	if (boardSelection.value == "Fujiyama") {
		Fujiyama();
	}
	if (boardSelection.value == "German Daisy") {
		GermanDaisy();
	}
	if (boardSelection.value == "Standard") {
		StandardLayout();
	}	
	

	//Sets the movement set to empty
	for (i = 0; i < 6; i++) {
		movementSet[i][0] = -1;
		movementSet[i][1] = -1;
	}

	//Clears the selection set
	firstSelectionX = -1;
	firstSelectionY = -1;
	secondSelectionX = -1;
	secondSelectionY = -1;
	
	IsOver = 0;
	playersTurn = Start;
	RefreshScreen();
	
	//Sets up the pieces pushed off the board spaces
	for (ii = 0; ii < 6; ii++) {
		//Top spaces
		document.getElementById("gamebox").getElementsByTagName("img")[ii].src = Pic[5].src;
		//Bottom spaces
		document.getElementById("gamebox").getElementsByTagName("img")[ii + 71].src = Pic[5].src;
	}    
}

function Timer() {
	if (pauseGame) {
		return
	}
	if (IsOver) {
		return;
	}
	if (IsPlayer[playersTurn]) {
		return;
	}
	if (firstSelectionX === -1) {
		GetBestMove(playersTurn);
		document.getElementById("gamebox").getElementsByTagName("img")[ImgNum(firstSelectionX, firstSelectionY)].src = Pic[playersTurn + 3].src;
		if (secondSelectionX > 0) {
			document.getElementById("gamebox").getElementsByTagName("img")[ImgNum(secondSelectionX, secondSelectionY)].src = Pic[playersTurn + 3].src;
			if (Math.abs(firstSelectionX - secondSelectionX) === 2 || Math.abs(firstSelectionY - secondSelectionY) === 2) {
				document.getElementById("gamebox").getElementsByTagName("img")[ImgNum((firstSelectionX + secondSelectionX) / 2, (firstSelectionY + secondSelectionY) / 2)].src = Pic[playersTurn + 3].src;
			}
		}
		return;
	}
	MakeMove(directionX, directionY, true);
	OverTest(playersTurn, true);
	firstSelectionX = -1;
	firstSelectionY = -1;
	secondSelectionX = -1;
	secondSelectionY = -1;  
	playersTurn = 1 - playersTurn;
	GetMovementSet();
}

function ImgNum(locationX, locationY) {
	var ii, jj, nn = 6;
	for (jj = 0; jj < Size; jj++) {
		for (ii = 0; ii < Size; ii++) {
			if (ii === locationX && jj === locationY) {
				return nn;
			}
			if (gameField[ii][jj] > -2) {
				nn++;
			}
		}
	}
	return 0;
}

function GetGameField(nn, mm) {
	if (nn < 0) {
		return -2;
	}
	if (nn >= Size) {
		return -2;
	}
	if (mm < 0) {
		return -2;
	}
	if (mm >= Size) {
		return -2;
	}
	return gameField[nn][mm];
}

function GetMovementSet() {
	var ii;
	
	//Clear 
	for (ii = 0; ii < 6; ii++) {
		movementSet[ii][0] = -1;
		movementSet[ii][1] = -1;
	}
	if (firstSelectionX < 0) {
		return;
	}
	if (secondSelectionX < 0) {
		if (GetGameField(firstSelectionX + 1, firstSelectionY) === -1) {
			movementSet[0][0] = firstSelectionX + 1;
			movementSet[0][1] = firstSelectionY;
		}
		if (GetGameField(firstSelectionX, firstSelectionY + 1) === -1) {
			movementSet[1][0] = firstSelectionX;
			movementSet[1][1] = firstSelectionY + 1;
		}
		if (GetGameField(firstSelectionX - 1, firstSelectionY + 1) === -1) {
			movementSet[2][0] = firstSelectionX - 1;
			movementSet[2][1] = firstSelectionY + 1;
		}
		if (GetGameField(firstSelectionX - 1, firstSelectionY) === -1) {
			movementSet[3][0] = firstSelectionX - 1;
			movementSet[3][1] = firstSelectionY;
		}
		if (GetGameField(firstSelectionX, firstSelectionY - 1) === -1) {
			movementSet[4][0] = firstSelectionX;
			movementSet[4][1] = firstSelectionY - 1;
		}
		if (GetGameField(firstSelectionX + 1, firstSelectionY - 1) === -1) {
			movementSet[5][0] = firstSelectionX + 1;
			movementSet[5][1] = firstSelectionY - 1;
		}
		return;
	}
	if (CanMove(1, 0)) {
		if (2 * firstSelectionX + firstSelectionY > 2 * secondSelectionX + secondSelectionY) {
			movementSet[0][0] = firstSelectionX + 1;
			movementSet[0][1] = firstSelectionY;
		} else {
			movementSet[0][0] = secondSelectionX + 1;
			movementSet[0][1] = secondSelectionY;
		}
	}
	if (CanMove(0, 1)) {
		if (firstSelectionX + 2 * firstSelectionY > secondSelectionX + 2 * secondSelectionY) {
			movementSet[1][0] = firstSelectionX;
			movementSet[1][1] = firstSelectionY + 1;
		} else {
			movementSet[1][0] = secondSelectionX;
			movementSet[1][1] = secondSelectionY + 1;
		}
	}
	if (CanMove(-1, 1)) {
		if (-firstSelectionX + 2 * firstSelectionY > -secondSelectionX + 2 * secondSelectionY) {
			movementSet[2][0] = firstSelectionX - 1;
			movementSet[2][1] = firstSelectionY + 1;
		} else {
			movementSet[2][0] = secondSelectionX - 1;
			movementSet[2][1] = secondSelectionY + 1;
		}
	}
	if (CanMove(-1, 0)) {
		if (2 * firstSelectionX + firstSelectionY < 2 * secondSelectionX + secondSelectionY) {
			movementSet[3][0] = firstSelectionX - 1;
			movementSet[3][1] = firstSelectionY;
		} else {
			movementSet[3][0] = secondSelectionX - 1;
			movementSet[3][1] = secondSelectionY;
		}
	}
	if (CanMove(0, -1)) {
		if (firstSelectionX + 2 * firstSelectionY < secondSelectionX + 2 * secondSelectionY) {
			movementSet[4][0] = firstSelectionX;
			movementSet[4][1] = firstSelectionY - 1;
		} else {
			movementSet[4][0] = secondSelectionX;
			movementSet[4][1] = secondSelectionY - 1;
		}
	}
	if (CanMove(1, -1)) {
		if (firstSelectionX - 2 * firstSelectionY > secondSelectionX - 2 * secondSelectionY) {
			movementSet[5][0] = firstSelectionX + 1;
			movementSet[5][1] = firstSelectionY - 1;
		} else {
			movementSet[5][0] = secondSelectionX + 1;
			movementSet[5][1] = secondSelectionY - 1;
		}
	}  
}

function CanMove(ii, jj) {
	var vv, ww;
	//1 marble selected
	if (secondSelectionX < 0) {
		if (GetGameField(firstSelectionX + ii, firstSelectionY + jj) === -1) {
			return true;
		}
		return false;
	}
	//2 marbles selected
	if (Math.abs(firstSelectionX - secondSelectionX) === 1 || Math.abs(firstSelectionY - secondSelectionY) === 1) {
		if ((firstSelectionX - secondSelectionX === ii) && (firstSelectionY - secondSelectionY === jj)) {
			vv = GetGameField(firstSelectionX + ii, firstSelectionY + jj);
			if (vv === -1 || (vv === 1 - playersTurn && GetGameField(firstSelectionX + 2 * ii, firstSelectionY + 2 * jj < 0))) {
				return true;
			}
			return false;
		}
		if ((secondSelectionX - firstSelectionX === ii) && (secondSelectionY - firstSelectionY === jj)) {
			vv = GetGameField(secondSelectionX + ii, secondSelectionY + jj);
			if (vv === -1 || (vv === 1 - playersTurn && GetGameField(secondSelectionX + 2 * ii, secondSelectionY + 2 * jj) < 0)) {
				return true;
			}
			return false;
		}
		if (GetGameField(firstSelectionX + ii, firstSelectionY + jj) === -1 && GetGameField(secondSelectionX + ii, secondSelectionY + jj) === -1) {
			return true;
		}
		return false;
	}
	//3 marbles selected
	if (Math.abs(firstSelectionX - secondSelectionX) === 2 || Math.abs(firstSelectionY - secondSelectionY) === 2) {
		if (firstSelectionX - secondSelectionX === 2 * ii && firstSelectionY - secondSelectionY === 2 * jj) {
			vv = GetGameField(firstSelectionX + ii, firstSelectionY + jj);
			if (vv === -1) {
				return true;
			}
			if (vv !== 1 - playersTurn) {
				return false;
			}
			vv = GetGameField(firstSelectionX + 2 * ii, firstSelectionY + 2 * jj);
			if (vv < 0 || (vv === 1 - playersTurn && GetGameField(firstSelectionX + 3 * ii, firstSelectionY + 3 * jj) < 0)) {
				return true;
			}
			return false;
		}
		if (secondSelectionX - firstSelectionX === 2 * ii && secondSelectionY - firstSelectionY === 2 * jj) {
			vv = GetGameField(secondSelectionX + ii, secondSelectionY + jj);
			if (vv === -1) {
				return true;
			}
			if (vv !== 1 - playersTurn) {
				return false;
			}
			vv = GetGameField(secondSelectionX + 2 * ii, secondSelectionY + 2 * jj);
			if (vv < 0 || (vv === 1 - playersTurn && GetGameField(secondSelectionX + 3 * ii, secondSelectionY + 3 * jj) < 0)) {
				return true;
			}
			return false;
		}
		if (GetGameField(firstSelectionX + ii, firstSelectionY + jj) === -1 && GetGameField(secondSelectionX + ii, secondSelectionY + jj) === -1) {
			return true;
		}
		return false;
	}   
	
}

function MakeMove(ii, jj, ss) {
	var vv;
	if (secondSelectionX < 0) {
		if (GetGameField(firstSelectionX + ii, firstSelectionY + jj) === -1) {
			gameField[firstSelectionX + ii][firstSelectionY + jj] = playersTurn;
			gameField[firstSelectionX][firstSelectionY] = -1;
			if (ss) {
				document.getElementById("gamebox").getElementsByTagName("img")[ImgNum(firstSelectionX + ii, firstSelectionY + jj)].src = Pic[playersTurn + 1].src;
				document.getElementById("gamebox").getElementsByTagName("img")[ImgNum(firstSelectionX, firstSelectionY)].src = Pic[0].src;
			}
			return 1;
		}
		return 0;
	}
	if (Math.abs(firstSelectionX - secondSelectionX) === 1 || Math.abs(firstSelectionY - secondSelectionY) === 1) {
		if (firstSelectionX - secondSelectionX === ii && firstSelectionY - secondSelectionY === jj) {
			vv = GetGameField(firstSelectionX + ii, firstSelectionY + jj);
			if (vv === -1) {
				gameField[firstSelectionX + ii][firstSelectionY + jj] = playersTurn;
				gameField[secondSelectionX][secondSelectionY] = -1;
				if (ss) {
					document.getElementById("gamebox").getElementsByTagName("img")[ImgNum(firstSelectionX + ii, firstSelectionY + jj)].src = Pic[playersTurn + 1].src;
					document.getElementById("gamebox").getElementsByTagName("img")[ImgNum(firstSelectionX, firstSelectionY)].src = Pic[playersTurn + 1].src;
					document.getElementById("gamebox").getElementsByTagName("img")[ImgNum(secondSelectionX, secondSelectionY)].src = Pic[0].src;
				}
				return 1;
			}
			if (vv !== 1 - playersTurn) {
				return 0;
			}
			vv = GetGameField(firstSelectionX + 2 * ii, firstSelectionY + 2 * jj);
			if (vv < 0) {
				if (vv === -1) {
					gameField[firstSelectionX + 2 * ii][firstSelectionY + 2 * jj] = 1 - playersTurn;
				}
				gameField[firstSelectionX + ii][firstSelectionY + jj] = playersTurn;
				gameField[secondSelectionX][secondSelectionY] = -1;
				if (ss) {
					if (vv === -1) {
						document.getElementById("gamebox").getElementsByTagName("img")[ImgNum(firstSelectionX + 2 * ii, firstSelectionY + 2 * jj)].src = Pic[2 - playersTurn].src;
					}
					document.getElementById("gamebox").getElementsByTagName("img")[ImgNum(firstSelectionX + ii, firstSelectionY + jj)].src = Pic[playersTurn + 1].src;
					document.getElementById("gamebox").getElementsByTagName("img")[ImgNum(firstSelectionX, firstSelectionY)].src = Pic[playersTurn + 1].src;
					document.getElementById("gamebox").getElementsByTagName("img")[ImgNum(secondSelectionX, secondSelectionY)].src = Pic[0].src;
				}
				return -vv;
			}
			return 0;
		}
		if (secondSelectionX - firstSelectionX === ii && secondSelectionY - firstSelectionY === jj) {
			vv = GetGameField(secondSelectionX + ii, secondSelectionY + jj);
			if (vv === -1) {
				gameField[secondSelectionX + ii][secondSelectionY + jj] = playersTurn;
				gameField[firstSelectionX][firstSelectionY] = -1;
				if (ss) {
					document.getElementById("gamebox").getElementsByTagName("img")[ImgNum(secondSelectionX + ii, secondSelectionY + jj)].src = Pic[playersTurn + 1].src;
					document.getElementById("gamebox").getElementsByTagName("img")[ImgNum(secondSelectionX, secondSelectionY)].src = Pic[playersTurn + 1].src;
					document.getElementById("gamebox").getElementsByTagName("img")[ImgNum(firstSelectionX, firstSelectionY)].src = Pic[0].src;
				}
				return 1;
			}
			if (vv !== 1 - playersTurn) {
				return 0;
			}
			vv = GetGameField(secondSelectionX + 2 * ii, secondSelectionY + 2 * jj);
			if (vv < 0) {
				if (vv === -1) {
					gameField[secondSelectionX + 2 * ii][secondSelectionY + 2 * jj] = 1 - playersTurn;
				}
				gameField[secondSelectionX + ii][secondSelectionY + jj] = playersTurn;
				gameField[firstSelectionX][firstSelectionY] = -1;
				if (ss) {
					if (vv === -1) {
						document.getElementById("gamebox").getElementsByTagName("img")[ImgNum(secondSelectionX + 2 * ii, secondSelectionY + 2 * jj)].src = Pic[2 - playersTurn].src;
					}
					document.getElementById("gamebox").getElementsByTagName("img")[ImgNum(secondSelectionX + ii, secondSelectionY + jj)].src = Pic[playersTurn + 1].src;
					document.getElementById("gamebox").getElementsByTagName("img")[ImgNum(secondSelectionX, secondSelectionY)].src = Pic[playersTurn + 1].src;
					document.getElementById("gamebox").getElementsByTagName("img")[ImgNum(firstSelectionX, firstSelectionY)].src = Pic[0].src;
				}
				return -vv;
			}
			return 0;
		}
		if (GetGameField(firstSelectionX + ii, firstSelectionY + jj) === -1 && GetGameField(secondSelectionX + ii, secondSelectionY + jj) === -1) {
			gameField[firstSelectionX + ii][firstSelectionY + jj] = playersTurn;
			gameField[firstSelectionX][firstSelectionY] = -1;
			gameField[secondSelectionX + ii][secondSelectionY + jj] = playersTurn;
			gameField[secondSelectionX][secondSelectionY] = -1;
			if (ss) {
				document.getElementById("gamebox").getElementsByTagName("img")[ImgNum(firstSelectionX + ii, firstSelectionY + jj)].src = Pic[playersTurn + 1].src;
				document.getElementById("gamebox").getElementsByTagName("img")[ImgNum(firstSelectionX, firstSelectionY)].src = Pic[0].src;
				document.getElementById("gamebox").getElementsByTagName("img")[ImgNum(secondSelectionX + ii, secondSelectionY + jj)].src = Pic[playersTurn + 1].src;
				document.getElementById("gamebox").getElementsByTagName("img")[ImgNum(secondSelectionX, secondSelectionY)].src = Pic[0].src;
			}
			return 1;
		}
		return 0;
	}
	if (Math.abs(firstSelectionX - secondSelectionX) === 2 || Math.abs(firstSelectionY - secondSelectionY) === 2) {
		if (firstSelectionX - secondSelectionX === 2 * ii && firstSelectionY - secondSelectionY === 2 * jj) {
			vv = GetGameField(firstSelectionX + ii, firstSelectionY + jj);
			if (vv === -1) {
				gameField[firstSelectionX + ii][firstSelectionY + jj] = playersTurn;
				gameField[secondSelectionX][secondSelectionY] = -1;
				if (ss) {
					document.getElementById("gamebox").getElementsByTagName("img")[ImgNum(firstSelectionX + ii, firstSelectionY + jj)].src = Pic[playersTurn + 1].src;
					document.getElementById("gamebox").getElementsByTagName("img")[ImgNum(firstSelectionX, firstSelectionY)].src = Pic[playersTurn + 1].src;
					document.getElementById("gamebox").getElementsByTagName("img")[ImgNum(firstSelectionX - ii, firstSelectionY - jj)].src = Pic[playersTurn + 1].src;
					document.getElementById("gamebox").getElementsByTagName("img")[ImgNum(secondSelectionX, secondSelectionY)].src = Pic[0].src;
				}
				return 1;
			}
			if (vv !== 1 - playersTurn) {
				return 0;
			}
			vv = GetGameField(firstSelectionX + 2 * ii, firstSelectionY + 2 * jj);
			if (vv < 0) {
				if (vv === -1) {
					gameField[firstSelectionX + 2 * ii][firstSelectionY + 2 * jj] = 1 - playersTurn;
				}
				gameField[firstSelectionX + ii][firstSelectionY + jj] = playersTurn;
				gameField[secondSelectionX][secondSelectionY] = -1;
				if (ss) {
					if (vv === -1) {
						document.getElementById("gamebox").getElementsByTagName("img")[ImgNum(firstSelectionX + 2 * ii, firstSelectionY + 2 * jj)].src = Pic[2 - playersTurn].src;
					}
					document.getElementById("gamebox").getElementsByTagName("img")[ImgNum(firstSelectionX + ii, firstSelectionY + jj)].src = Pic[playersTurn + 1].src;
					document.getElementById("gamebox").getElementsByTagName("img")[ImgNum(firstSelectionX, firstSelectionY)].src = Pic[playersTurn + 1].src;
					document.getElementById("gamebox").getElementsByTagName("img")[ImgNum(firstSelectionX - ii, firstSelectionY - jj)].src = Pic[playersTurn + 1].src;
					document.getElementById("gamebox").getElementsByTagName("img")[ImgNum(secondSelectionX, secondSelectionY)].src = Pic[0].src;
				}
				return -vv;
			}
			if (vv !== 1 - playersTurn) {
				return 0;
			}
			vv = GetGameField(firstSelectionX + 3 * ii, firstSelectionY + 3 * jj);
			if (vv < 0) {
				if (vv === -1) {
					gameField[firstSelectionX + 3 * ii][firstSelectionY + 3 * jj] = 1 - playersTurn;
				}
				gameField[firstSelectionX + ii][firstSelectionY + jj] = playersTurn;
				gameField[secondSelectionX][secondSelectionY] = -1;
				if (ss) {
					if (vv === -1) {
						document.getElementById("gamebox").getElementsByTagName("img")[ImgNum(firstSelectionX + 3 * ii, firstSelectionY + 3 * jj)].src = Pic[2 - playersTurn].src;
					}
					document.getElementById("gamebox").getElementsByTagName("img")[ImgNum(firstSelectionX + ii, firstSelectionY + jj)].src = Pic[playersTurn + 1].src;
					document.getElementById("gamebox").getElementsByTagName("img")[ImgNum(firstSelectionX, firstSelectionY)].src = Pic[playersTurn + 1].src;
					document.getElementById("gamebox").getElementsByTagName("img")[ImgNum(firstSelectionX - ii, firstSelectionY - jj)].src = Pic[playersTurn + 1].src;
					document.getElementById("gamebox").getElementsByTagName("img")[ImgNum(secondSelectionX, secondSelectionY)].src = Pic[0].src;
				}
				return -vv;
			}
			return 0;
		}
		if (secondSelectionX - firstSelectionX === 2 * ii && secondSelectionY - firstSelectionY === 2 * jj) {
			vv = GetGameField(secondSelectionX + ii, secondSelectionY + jj);
			if (vv === -1) {
				gameField[secondSelectionX + ii][secondSelectionY + jj] = playersTurn;
				gameField[firstSelectionX][firstSelectionY] = -1;
				if (ss) {
					document.getElementById("gamebox").getElementsByTagName("img")[ImgNum(secondSelectionX + ii, secondSelectionY + jj)].src = Pic[playersTurn + 1].src;
					document.getElementById("gamebox").getElementsByTagName("img")[ImgNum(secondSelectionX, secondSelectionY)].src = Pic[playersTurn + 1].src;
					document.getElementById("gamebox").getElementsByTagName("img")[ImgNum(secondSelectionX - ii, secondSelectionY - jj)].src = Pic[playersTurn + 1].src;
					document.getElementById("gamebox").getElementsByTagName("img")[ImgNum(firstSelectionX, firstSelectionY)].src = Pic[0].src;
				}
				return 1;
			}
			if (vv !== 1 - playersTurn) {
				return 0;
			}
			vv = GetGameField(secondSelectionX + 2 * ii, secondSelectionY + 2 * jj);
			if (vv < 0) {
				if (vv === -1) {
					gameField[secondSelectionX + 2 * ii][secondSelectionY + 2 * jj] = 1 - playersTurn;
				}
				gameField[secondSelectionX + ii][secondSelectionY + jj] = playersTurn;
				gameField[firstSelectionX][firstSelectionY] = -1;
				if (ss) {
					if (vv === -1) {
						document.getElementById("gamebox").getElementsByTagName("img")[ImgNum(secondSelectionX + 2 * ii, secondSelectionY + 2 * jj)].src = Pic[2 - playersTurn].src;
					}
					document.getElementById("gamebox").getElementsByTagName("img")[ImgNum(secondSelectionX + ii, secondSelectionY + jj)].src = Pic[playersTurn + 1].src;
					document.getElementById("gamebox").getElementsByTagName("img")[ImgNum(secondSelectionX, secondSelectionY)].src = Pic[playersTurn + 1].src;
					document.getElementById("gamebox").getElementsByTagName("img")[ImgNum(secondSelectionX - ii, secondSelectionY - jj)].src = Pic[playersTurn + 1].src;
					document.getElementById("gamebox").getElementsByTagName("img")[ImgNum(firstSelectionX, firstSelectionY)].src = Pic[0].src;
				}
				return -vv;
			}
			if (vv !== 1 - playersTurn) {
				return 0;
			}
			vv = GetGameField(secondSelectionX + 3 * ii, secondSelectionY + 3 * jj);
			if (vv < 0) {
				if (vv === -1) {
					gameField[secondSelectionX + 3 * ii][secondSelectionY + 3 * jj] = 1 - playersTurn;
				}
				gameField[secondSelectionX + ii][secondSelectionY + jj] = playersTurn;
				gameField[firstSelectionX][firstSelectionY] = -1;
				if (ss) {
					if (vv === -1) {
						document.getElementById("gamebox").getElementsByTagName("img")[ImgNum(secondSelectionX + 3 * ii, secondSelectionY + 3 * jj)].src = Pic[2 - playersTurn].src;
					}
					document.getElementById("gamebox").getElementsByTagName("img")[ImgNum(secondSelectionX + ii, secondSelectionY + jj)].src = Pic[playersTurn + 1].src;
					document.getElementById("gamebox").getElementsByTagName("img")[ImgNum(secondSelectionX, secondSelectionY)].src = Pic[playersTurn + 1].src;
					document.getElementById("gamebox").getElementsByTagName("img")[ImgNum(secondSelectionX - ii, secondSelectionY - jj)].src = Pic[playersTurn + 1].src;
					document.getElementById("gamebox").getElementsByTagName("img")[ImgNum(firstSelectionX, firstSelectionY)].src = Pic[0].src;
				}
				return -vv;
			}
			return 0;
		}
		if (GetGameField(firstSelectionX + ii, firstSelectionY + jj) === -1 && GetGameField(secondSelectionX + ii, secondSelectionY + jj) === -1 && GetGameField((firstSelectionX + secondSelectionX) / 2 + ii, (firstSelectionY + secondSelectionY) / 2 + jj) === -1) {
			gameField[firstSelectionX + ii][firstSelectionY + jj] = playersTurn;
			gameField[firstSelectionX][firstSelectionY] = -1;
			gameField[secondSelectionX + ii][secondSelectionY + jj] = playersTurn;
			gameField[secondSelectionX][secondSelectionY] = -1;
			gameField[(firstSelectionX + secondSelectionX) / 2 + ii][(firstSelectionY + secondSelectionY) / 2 + jj] = playersTurn;
			gameField[(firstSelectionX + secondSelectionX) / 2][(firstSelectionY + secondSelectionY) / 2] = -1;
			if (ss) {
				document.getElementById("gamebox").getElementsByTagName("img")[ImgNum(firstSelectionX + ii, firstSelectionY + jj)].src = Pic[playersTurn + 1].src;
				document.getElementById("gamebox").getElementsByTagName("img")[ImgNum(firstSelectionX, firstSelectionY)].src = Pic[0].src;
				document.getElementById("gamebox").getElementsByTagName("img")[ImgNum(secondSelectionX + ii, secondSelectionY + jj)].src = Pic[playersTurn + 1].src;
				document.getElementById("gamebox").getElementsByTagName("img")[ImgNum(secondSelectionX, secondSelectionY)].src = Pic[0].src;
				document.getElementById("gamebox").getElementsByTagName("img")[ImgNum((firstSelectionX + secondSelectionX) / 2 + ii, (firstSelectionY + secondSelectionY) / 2 + jj)].src = Pic[playersTurn + 1].src;
				document.getElementById("gamebox").getElementsByTagName("img")[ImgNum((firstSelectionX + secondSelectionX) / 2, (firstSelectionY + secondSelectionY) / 2)].src = Pic[0].src;
			}
			return 1;
		}
		return 0;
	}   
}

function MouseDown(ii, jj) {
	var nn, mm = -1;
	if (IsOver > 0) {
		return;
	}
	if (!IsPlayer[playersTurn]) {
		return;
	}
	if (firstSelectionX < 0) {
		if (gameField[ii][jj] !== playersTurn) {
			return;
		}
		firstSelectionX = ii;
		firstSelectionY = jj;
		document.getElementById("gamebox").getElementsByTagName("img")[ImgNum(ii, jj)].src = Pic[playersTurn + 3].src;
		GetMovementSet();
		return;
	}
	if (secondSelectionX < 0) {
		if (gameField[ii][jj] === -1) {
			if (ii === firstSelectionX && Math.abs(jj - firstSelectionY) === 1 || jj === firstSelectionY && Math.abs(ii - firstSelectionX) === 1 || ii === firstSelectionX + 1 && jj === firstSelectionY - 1 || ii === firstSelectionX - 1 && jj === firstSelectionY + 1) {
				MakeMove(ii - firstSelectionX, jj - firstSelectionY, true);
				OverTest(playersTurn, true);
				firstSelectionX = -1;
				firstSelectionY = -1;
				playersTurn = 1 - playersTurn;
				GetMovementSet();
			}
			return;
		}
		if (gameField[ii][jj] === playersTurn) {
			if (ii === firstSelectionX && jj === firstSelectionY) {
				document.getElementById("gamebox").getElementsByTagName("img")[ImgNum(firstSelectionX, firstSelectionY)].src = Pic[playersTurn + 1].src;
				firstSelectionX = -1;
				firstSelectionY = -1;
				GetMovementSet();
				return;
			}
			if (ii === firstSelectionX && Math.abs(jj - firstSelectionY) <= 2 || jj === firstSelectionY && Math.abs(ii - firstSelectionX) <= 2 || ii === firstSelectionX + 1 && jj === firstSelectionY - 1 || ii === firstSelectionX - 1 && jj === firstSelectionY + 1 || ii === firstSelectionX + 2 && jj === firstSelectionY - 2 || ii === firstSelectionX - 2 && jj === firstSelectionY + 2) {
				if (Math.abs(ii - firstSelectionX) === 1 || Math.abs(jj - firstSelectionY) === 1) {
					secondSelectionX = ii;
					secondSelectionY = jj;
					document.getElementById("gamebox").getElementsByTagName("img")[ImgNum(secondSelectionX, secondSelectionY)].src = Pic[playersTurn + 3].src;
					GetMovementSet();
					return;
				}
				if (gameField[(ii + firstSelectionX) / 2][(jj + firstSelectionY) / 2] === playersTurn) {
					secondSelectionX = ii;
					secondSelectionY = jj;
					document.getElementById("gamebox").getElementsByTagName("img")[ImgNum(secondSelectionX, secondSelectionY)].src = Pic[playersTurn + 3].src;
					document.getElementById("gamebox").getElementsByTagName("img")[ImgNum((firstSelectionX + secondSelectionX) / 2, (firstSelectionY + secondSelectionY) / 2)].src = Pic[playersTurn + 3].src;
					GetMovementSet();
					return;
				}
			}
			document.getElementById("gamebox").getElementsByTagName("img")[ImgNum(firstSelectionX, firstSelectionY)].src = Pic[playersTurn + 1].src;
			firstSelectionX = -1;
			firstSelectionY = -1;
			MouseDown(ii, jj);
			return;
		}
	}
	if (gameField[ii][jj] === playersTurn) {
		if (Math.abs(secondSelectionX - firstSelectionX) === 2 || Math.abs(secondSelectionY - firstSelectionY) === 2) {
			document.getElementById("gamebox").getElementsByTagName("img")[ImgNum((firstSelectionX + secondSelectionX) / 2, (firstSelectionY + secondSelectionY) / 2)].src = Pic[playersTurn + 1].src;
		}
		document.getElementById("gamebox").getElementsByTagName("img")[ImgNum(firstSelectionX, firstSelectionY)].src = Pic[playersTurn + 1].src;
		document.getElementById("gamebox").getElementsByTagName("img")[ImgNum(secondSelectionX, secondSelectionY)].src = Pic[playersTurn + 1].src;
		firstSelectionX = -1;
		firstSelectionY = -1;
		secondSelectionX = -1;
		secondSelectionY = -1;
		MouseDown(ii, jj);
		return;
	}
	for (nn = 0; nn < 6; nn++) {
		if (movementSet[nn][0] === ii && movementSet[nn][1] === jj) mm = nn;
	}
	if (mm < 0) {
		return;
	}
	if (Math.abs(2 * (ii - firstSelectionX) + (jj - firstSelectionY)) + 2 * Math.abs(jj - firstSelectionY) < Math.abs(2 * (ii - secondSelectionX) + (jj - secondSelectionY)) + 2 * Math.abs(jj - secondSelectionY)) {
		MakeMove(ii - firstSelectionX, jj - firstSelectionY, true);
	} else {
		MakeMove(ii - secondSelectionX, jj - secondSelectionY, true);
	}
	OverTest(playersTurn, true);
	firstSelectionX = -1;
	firstSelectionY = -1;
	secondSelectionX = -1;
	secondSelectionY = -1;
	playersTurn = 1 - playersTurn;
	GetMovementSet();
}

function MouseOver(ii, jj) {
	var nn, mm = -1;
	if (IsOver > 0) {
		return;
	}
	if (!IsPlayer[playersTurn]) {
		return;
	}
	if (firstSelectionX < 0) {
		return;
	}
	for (nn = 0; nn < 6; nn++) {
		if (movementSet[nn][0] === ii && movementSet[nn][1] === jj) {
			mm = nn;
		}
	}
	if (mm < 0) {
		return;
	}
	document.getElementById("gamebox").getElementsByTagName("img")[ImgNum(firstSelectionX, firstSelectionY)].src = playerImage[playersTurn][mm].src;
	if (secondSelectionX >= 0) {
		document.getElementById("gamebox").getElementsByTagName("img")[ImgNum(secondSelectionX, secondSelectionY)].src = playerImage[playersTurn][mm].src;
		if (Math.abs(secondSelectionX - firstSelectionX) === 2 || Math.abs(secondSelectionY - firstSelectionY) === 2) {
			document.getElementById("gamebox").getElementsByTagName("img")[ImgNum((firstSelectionX + secondSelectionX) / 2, (firstSelectionY + secondSelectionY) / 2)].src = playerImage[playersTurn][mm].src;
		}
	}
}

function MouseOut(ii, jj) {
	var nn, mm = -1;
	if (IsOver > 0) {
		return;
	}
	if (!IsPlayer[playersTurn]) {
		return;
	}
	if (firstSelectionX < 0) {
		return;
	}
	for (nn = 0; nn < 6; nn++) {
		if (movementSet[nn][0] === ii && movementSet[nn][1] === jj) {
			mm = nn;
		}
	}
	if (mm < 0) {
		return;
	}
	document.getElementById("gamebox").getElementsByTagName("img")[ImgNum(firstSelectionX, firstSelectionY)].src = Pic[playersTurn + 3].src;
	if (secondSelectionX >= 0) {
		document.getElementById("gamebox").getElementsByTagName("img")[ImgNum(secondSelectionX, secondSelectionY)].src = Pic[playersTurn + 3].src;
		if (Math.abs(secondSelectionX - firstSelectionX) === 2 || Math.abs(secondSelectionY - firstSelectionY) === 2) {
			document.getElementById("gamebox").getElementsByTagName("img")[ImgNum((firstSelectionX + secondSelectionX) / 2, (firstSelectionY + secondSelectionY) / 2)].src = Pic[playersTurn + 3].src;
		}
	}  
}

function RestoreGameField() {
	var ii, jj;
	
	//Set Fld to tempField values
	for (ii = 0; ii < Size; ii++) {
		for (jj = 0; jj < Size; jj++) {
			gameField[ii][jj] = tempField[ii][jj];
		}
	}  
}

function GetBestMove(currentPlayer) {
	var ii, jj, nn, vv, ii0_best, jj0_best, ii1_best, jj1_best, iid_best, jjd_best, vv_best = -10000;
	var ddi = new Array(1, 0, -1, -1, 0, 1);
	var ddj = new Array(0, 1, 1, 0, -1, -1);
	
	//Set tempField to Fld values
	for (ii = 0; ii < Size; ii++) {
		for (jj = 0; jj < Size; jj++) {
			tempField[ii][jj] = gameField[ii][jj];
		}
	}
	for (ii = 0; ii < Size; ii++) {
		for (jj = 0; jj < Size; jj++) {
			if (gameField[ii][jj] === currentPlayer) {
				firstSelectionX = ii; 
				firstSelectionY = jj; 
				secondSelectionX = -1; 
				secondSelectionY = -1;
				
				for (nn = 0; nn < 6; nn++) {
					vv = MakeMove(ddi[nn], ddj[nn], false);
					if (vv > 0) {
						vv = EvalGameField(currentPlayer) + Math.random();
						if (vv_best < vv) {
							ii0_best = firstSelectionX; 
							jj0_best = firstSelectionY; 
							ii1_best = secondSelectionX; 
							jj1_best = secondSelectionY;
							iid_best = ddi[nn]; 
							jjd_best = ddj[nn]; 
							vv_best = vv;
						}
						RestoreGameField();
					}
				}
			}
		}
	}
	for (ii = 0; ii < Size - 1; ii++) {
		for (jj = 0; jj < Size; jj++) {
			if (gameField[ii][jj] === currentPlayer && gameField[ii + 1][jj] === currentPlayer) {
				firstSelectionX = ii; 
				firstSelectionY = jj; 
				secondSelectionX = ii + 1; 
				secondSelectionY = jj;
				for (nn = 0; nn < 6; nn++) {
					vv = MakeMove(ddi[nn], ddj[nn], false);
					if (vv > 0) {
						vv = EvalGameField(currentPlayer) + Math.random() * 2;
						if (vv_best < vv) {
							ii0_best = firstSelectionX; 
							jj0_best = firstSelectionY; 
							ii1_best = secondSelectionX; 
							jj1_best = secondSelectionY;
							iid_best = ddi[nn]; 
							jjd_best = ddj[nn]; 
							vv_best = vv;
						}
						RestoreGameField();
					}
				}
			}
		}
	}
	for (ii = 0; ii < Size; ii++) {
		for (jj = 0; jj < Size - 1; jj++) {
			if (gameField[ii][jj] === currentPlayer && gameField[ii][jj + 1] === currentPlayer) {
				firstSelectionX = ii; 
				firstSelectionY = jj; 
				secondSelectionX = ii; 
				secondSelectionY = jj + 1;
				for (nn = 0; nn < 6; nn++) {
					vv = MakeMove(ddi[nn], ddj[nn], false);
					if (vv > 0) {
						vv = EvalGameField(currentPlayer) + Math.random() * 2;
						if (vv_best < vv) {
							ii0_best = firstSelectionX; 
							jj0_best = firstSelectionY; 
							ii1_best = secondSelectionX; 
							jj1_best = secondSelectionY;
							iid_best = ddi[nn]; 
							jjd_best = ddj[nn]; 
							vv_best = vv;
						}
						RestoreGameField();
					}
				}
			}
		}
	}
	for (ii = 1; ii < Size; ii++) {
		for (jj = 0; jj < Size - 1; jj++) {
			if (gameField[ii][jj] === currentPlayer && gameField[ii - 1][jj + 1] === currentPlayer) {
				firstSelectionX = ii; 
				firstSelectionY = jj; 
				secondSelectionX = ii - 1; 
				secondSelectionY = jj + 1;
				for (nn = 0; nn < 6; nn++) {
					vv = MakeMove(ddi[nn], ddj[nn], false);
					if (vv > 0) {
						vv = EvalGameField(currentPlayer) + Math.random() * 2;
						if (vv_best < vv) {
							ii0_best = firstSelectionX; 
							jj0_best = firstSelectionY; 
							ii1_best = secondSelectionX; 
							jj1_best = secondSelectionY;
							iid_best = ddi[nn]; 
							jjd_best = ddj[nn]; 
							vv_best = vv;
						}
						RestoreGameField();
					}
				}
			}
		}
	}
	for (ii = 0; ii < Size - 2; ii++) {
		for (jj = 0; jj < Size; jj++) {
			if (gameField[ii][jj] === currentPlayer && gameField[ii + 1][jj] === currentPlayer && gameField[ii + 2][jj] === currentPlayer) {
				firstSelectionX = ii; 
				firstSelectionY = jj; 
				secondSelectionX = ii + 2; 
				secondSelectionY = jj;
				for (nn = 0; nn < 6; nn++) {
					vv = MakeMove(ddi[nn], ddj[nn], false);
					if (vv > 0) {
						vv = EvalGameField(currentPlayer) + Math.random() * 3;
						if (vv_best < vv) {
							ii0_best = firstSelectionX; 
							jj0_best = firstSelectionY; 
							ii1_best = secondSelectionX; 
							jj1_best = secondSelectionY;
							iid_best = ddi[nn]; 
							jjd_best = ddj[nn]; 
							vv_best = vv;
						}
						RestoreGameField();
					}
				}
			}
		}
	}
	for (ii = 0; ii < Size; ii++) {
		for (jj = 0; jj < Size - 2; jj++) {
			if (gameField[ii][jj] === currentPlayer && gameField[ii][jj + 1] === currentPlayer && gameField[ii][jj + 2] === currentPlayer) {
				firstSelectionX = ii; 
				firstSelectionY = jj; 
				secondSelectionX = ii; 
				secondSelectionY = jj + 2;
				for (nn = 0; nn < 6; nn++) {
					vv = MakeMove(ddi[nn], ddj[nn], false);
					if (vv > 0) {
						vv = EvalGameField(currentPlayer) + Math.random() * 3;
						if (vv_best < vv) {
							ii0_best = firstSelectionX; 
							jj0_best = firstSelectionY; 
							ii1_best = secondSelectionX; 
							jj1_best = secondSelectionY;
							iid_best = ddi[nn]; 
							jjd_best = ddj[nn]; 
							vv_best = vv;
						}
						RestoreGameField();
					}
				}
			}
		}
	}
	for (ii = 2; ii < Size; ii++) {
		for (jj = 0; jj < Size - 2; jj++) {
			if (gameField[ii][jj] === currentPlayer && gameField[ii - 1][jj + 1] === currentPlayer && gameField[ii - 2][jj + 2] === currentPlayer) {
				firstSelectionX = ii; 
				firstSelectionY = jj; 
				secondSelectionX = ii - 2; 
				secondSelectionY = jj + 2;
				for (nn = 0; nn < 6; nn++) {
					vv = MakeMove(ddi[nn], ddj[nn], false);
					if (vv > 0) {
						vv = EvalGameField(currentPlayer) + Math.random() * 3;
						if (vv_best < vv) {
							ii0_best = firstSelectionX; 
							jj0_best = firstSelectionY; 
							ii1_best = secondSelectionX; 
							jj1_best = secondSelectionY;
							iid_best = ddi[nn]; 
							jjd_best = ddj[nn]; 
							vv_best = vv;
						}
						RestoreGameField();
					}
				}
			}
		}
	}
	firstSelectionX = ii0_best;
	firstSelectionY = jj0_best;
	secondSelectionX = ii1_best;
	secondSelectionY = jj1_best;
	directionX = iid_best;
	directionY = jjd_best;
}

function EvalGameField(checkCurrentPlayer) {
	var ii, jj, vv = 0;
	for (jj = 0; jj < Size; jj++) {
		for (ii = 0; ii < Size; ii++) {
			if (gameField[ii][jj] >= 0) {
				vv += fieldValue[ii][jj] * (2 * gameField[ii][jj] - 1);
			}
		}
	}
	if (checkCurrentPlayer === 0) {
		vv *= -1;
	}
	for (ii = 0; ii < 24; ii++)
	{
		if (gameField[CI[ii][0]][CJ[ii][0]] === checkCurrentPlayer) {
			for (jj = 1; jj < CI[ii].length; jj++) {
				if (gameField[CI[ii][0] + CI[ii][jj]][CJ[ii][0] + CJ[ii][jj]] === 1 - checkCurrentPlayer && gameField[CI[ii][0] + 2 * CI[ii][jj]][CJ[ii][0] + 2 * CJ[ii][jj]] === 1 - checkCurrentPlayer) {
					vv -= 16;
				} else {
					if (gameField[CI[ii][0] + CI[ii][jj]][CJ[ii][0] + CJ[ii][jj]] === checkCurrentPlayer &&
						gameField[CI[ii][0] + 2 * CI[ii][jj]][CJ[ii][0] + 2 * CJ[ii][jj]] === 1 - checkCurrentPlayer &&
						gameField[CI[ii][0] + 3 * CI[ii][jj]][CJ[ii][0] + 3 * CJ[ii][jj]] === 1 - checkCurrentPlayer &&
						gameField[CI[ii][0] + 4 * CI[ii][jj]][CJ[ii][0] + 4 * CJ[ii][jj]] === 1 - checkCurrentPlayer) {
						vv -= 16;
					}
				}
			}
		}
		if (gameField[CI[ii][0]][CJ[ii][0]] === 1 - checkCurrentPlayer) {
			for (jj = 1; jj < CI[ii].length; jj++) {
				if (gameField[CI[ii][0] + CI[ii][jj]][CJ[ii][0] + CJ[ii][jj]] === checkCurrentPlayer &&
					gameField[CI[ii][0] + 2 * CI[ii][jj]][CJ[ii][0] + 2 * CJ[ii][jj]] === checkCurrentPlayer)
					vv += 8;
				else {
					if (gameField[CI[ii][0] + CI[ii][jj]][CJ[ii][0] + CJ[ii][jj]] === 1 - checkCurrentPlayer &&
						gameField[CI[ii][0] + 2 * CI[ii][jj]][CJ[ii][0] + 2 * CJ[ii][jj]] === checkCurrentPlayer &&
						gameField[CI[ii][0] + 3 * CI[ii][jj]][CJ[ii][0] + 3 * CJ[ii][jj]] === checkCurrentPlayer &&
						gameField[CI[ii][0] + 4 * CI[ii][jj]][CJ[ii][0] + 4 * CJ[ii][jj]] === checkCurrentPlayer) {
						vv += 8;
					}	
				}
			}
		}
	}
	return vv;
}  

function OverTest(nn, mmsg) {
	var ii, jj, mm = 1 - nn, vv = 0;
	IsOver = false;
	for (jj = 0; jj < Size; jj++) {
		for (ii = 0; ii < Size; ii++) {
			if (gameField[ii][jj] === mm) {
				vv++;
			}
		}
	}
	if (mmsg && vv < 14) {
		document.getElementById("gamebox").getElementsByTagName("img")[13 - vv + nn * 71].src = Pic[mm + 1].src;
	}
	if (vv < 9) {
		IsOver = true;
		if (mmsg) {
			if (nn === 0) {
				alert("Red has won !");
			} else {
				alert("Blue has won !");
			}
		}
	}
	return vv;
}

function RefreshScreen() {
	var ii, jj, nn = 6;
	for (jj = 0; jj < Size; jj++) {
		for (ii = 0; ii < Size; ii++) {
			if (gameField[ii][jj] > -2) {
				document.getElementById("gamebox").getElementsByTagName("img")[nn++].src = Pic[gameField[ii][jj] + 1].src;
			}
		}
	}
}

function ShowHelp() {
	var modal = document.getElementById("helpbox");
	modal.style.display = "block"
}

function HideHelp() {
	var modal = document.getElementById("helpbox");
	modal.style.display = "none";
}

function PauseGame() {
	var pausebutton = document.getElementById("PauseGameButton");
	if (!pauseGame) {
		pausebutton.style.backgroundColor = "red";
		pauseGame = 1;
	} else {
		pausebutton.style.backgroundColor = "";
		pauseGame = 0;
	}
	
}

//Board Layouts
function StandardLayout() {
	
	//Row 0
	//gameField[0][0] = 0;
	//gameField[1][0] = 0;
	//gameField[2][0] = 0;
	//gameField[3][0] = 0;
	gameField[4][0] = 0;
	gameField[5][0] = 0;
	gameField[6][0] = 0;
	gameField[7][0] = 0;
	gameField[8][0] = 0;
	
	//Row 1
	//gameField[0][1] = 0;
	//gameField[1][1] = 0;
	//gameField[2][1] = 0;
	gameField[3][1] = 0;
	gameField[4][1] = 0;
	gameField[5][1] = 0;
	gameField[6][1] = 0;
	gameField[7][1] = 0;
	gameField[8][1] = 0;
	
	//Row 2
	//gameField[0][2] = 0;
	//gameField[1][2] = 0;
	//gameField[2][2] = 0;
	//gameField[3][2] = 0;
	gameField[4][2] = 0;
	gameField[5][2] = 0;
	gameField[6][2] = 0;
	//gameField[7][2] = 0;
	//gameField[8][2] = 0;
	
	//Row 3
	//gameField[0][3] = 0;
	//gameField[1][3] = 0;
	//gameField[2][3] = 0;
	//gameField[3][3] = 0;
	//gameField[4][3] = 0;
	//gameField[5][3] = 0;
	//gameField[6][3] = 0;
	//gameField[7][3] = 0;
	//gameField[8][3] = 0;
	
	//Row 4
	//gameField[0][4] = 0;
	//gameField[1][4] = 0;
	//gameField[2][4] = 0;
	//gameField[3][4] = 0;
	//gameField[4][4] = 0;
	//gameField[5][4] = 0;
	//gameField[6][4] = 0;
	//gameField[7][4] = 0;
	//gameField[8][4] = 0;
	
	//Row 5
	//gameField[0][5] = 0;
	//gameField[1][5] = 0;
	//gameField[2][5] = 0;
	//gameField[3][5] = 0;
	//gameField[4][5] = 0;
	//gameField[5][5] = 0;
	//gameField[6][5] = 0;
	//gameField[7][5] = 0;
	//gameField[8][5] = 0;
	
	//Row 6
	//gameField[0][6] = 1;
	//gameField[1][6] = 1;
	gameField[2][6] = 1;
	gameField[3][6] = 1;
	gameField[4][6] = 1;
	//gameField[5][6] = 1;
	//gameField[6][6] = 1;
	//gameField[7][6] = 1;
	//gameField[8][6] = 1;
	
	//Row 7
	gameField[0][7] = 1;
	gameField[1][7] = 1;
	gameField[2][7] = 1;
	gameField[3][7] = 1;
	gameField[4][7] = 1;
	gameField[5][7] = 1;
	//gameField[6][7] = 1;
	//gameField[7][7] = 1;
	//gameField[8][7] = 1;
	
	//Row 8
	gameField[0][8] = 1;
	gameField[1][8] = 1;
	gameField[2][8] = 1;
	gameField[3][8] = 1;
	gameField[4][8] = 1;
	//gameField[5][8] = 1;
	//gameField[6][8] = 1;
	//gameField[7][8] = 1;
	//gameField[8][8] = 1;
}

function AlienAttack() {
	//Row 0
	gameField[4][0] = 0;
	gameField[6][0] = 0;
	gameField[8][0] = 0;
	
	//Row 1
	gameField[4][1] = 0;
	gameField[5][1] = 1;
	gameField[6][1] = 1;
	gameField[7][1] = 0;
		
	//Row 2
	gameField[3][2] = 0;
	gameField[4][2] = 1;
	gameField[5][2] = 0;
	gameField[6][2] = 1;
	gameField[7][2] = 0;
		
	//Row 3
	gameField[4][3] = 0;
	gameField[5][3] = 0;
		
	//Row 5
	gameField[3][5] = 1;
	gameField[4][5] = 1;
		
	//Row 6
	gameField[1][6] = 1;
	gameField[2][6] = 0;
	gameField[3][6] = 1;
	gameField[4][6] = 0;
	gameField[5][6] = 1;
		
	//Row 7
	gameField[1][7] = 1;
	gameField[2][7] = 0;
	gameField[3][7] = 0;
	gameField[4][7] = 1;
		
	//Row 8
	gameField[0][8] = 1;
	gameField[2][8] = 1;
	gameField[4][8] = 1;
}

function BelgianDaisy() {
	//Row 0
	gameField[4][0] = 1;
	gameField[5][0] = 1;
	gameField[7][0] = 0;
	gameField[8][0] = 0;
	
	//Row 1
	gameField[3][1] = 1;
	gameField[4][1] = 1;
	gameField[5][1] = 1;
	gameField[6][1] = 0;
	gameField[7][1] = 0;
	gameField[8][1] = 0;
	
	//Row 2
	gameField[3][2] = 1;
	gameField[4][2] = 1;
	gameField[6][2] = 0;
	gameField[7][2] = 0;
	
	//Row 6
	gameField[1][6] = 0;
	gameField[2][6] = 0;
	gameField[4][6] = 1;
	gameField[5][6] = 1;
	
	//Row 7
	gameField[0][7] = 0;
	gameField[1][7] = 0;
	gameField[2][7] = 0;
	gameField[3][7] = 1;
	gameField[4][7] = 1;
	gameField[5][7] = 1;

	//Row 8
	gameField[0][8] = 0;
	gameField[1][8] = 0;
	gameField[3][8] = 1;
	gameField[4][8] = 1;

}

function DutchDaisy() {
	//Row 0
	gameField[4][0] = 1;
	gameField[5][0] = 1;
	gameField[7][0] = 0;
	gameField[8][0] = 0;
	
	//Row 1
	gameField[3][1] = 1;
	gameField[4][1] = 0;
	gameField[5][1] = 1;
	gameField[6][1] = 0;
	gameField[7][1] = 1;
	gameField[8][1] = 0;
	
	//Row 2
	gameField[3][2] = 1;
	gameField[4][2] = 1;
	gameField[6][2] = 0;
	gameField[7][2] = 0;
	
	//Row 6
	gameField[1][6] = 0;
	gameField[2][6] = 0;
	gameField[4][6] = 1;
	gameField[5][6] = 1;
	
	//Row 7
	gameField[0][7] = 0;
	gameField[1][7] = 1;
	gameField[2][7] = 0;
	gameField[3][7] = 1;
	gameField[4][7] = 0;
	gameField[5][7] = 1;
	
	//Row 8
	gameField[0][8] = 0;
	gameField[1][8] = 0;
	gameField[3][8] = 1;
	gameField[4][8] = 1;

}

function FaceToFace() {

	//Row 2
	gameField[2][2] = 1;
	gameField[3][2] = 1;
	gameField[7][2] = 0;
	gameField[8][2] = 0;
	
	//Row 3
	gameField[1][3] = 1;
	gameField[2][3] = 1;
	gameField[3][3] = 1;
	gameField[6][3] = 0;
	gameField[7][3] = 0;
	gameField[8][3] = 0;
	
	//Row 4
	gameField[0][4] = 1;
	gameField[1][4] = 1;
	gameField[2][4] = 1;
	gameField[3][4] = 1;
	gameField[5][4] = 0;
	gameField[6][4] = 0;
	gameField[7][4] = 0;
	gameField[8][4] = 0;
	
	//Row 5
	gameField[0][5] = 1;
	gameField[1][5] = 1;
	gameField[2][5] = 1;
	gameField[5][5] = 0;
	gameField[6][5] = 0;
	gameField[7][5] = 0;
	
	//Row 6
	gameField[0][6] = 1;
	gameField[1][6] = 1;
	gameField[5][6] = 0;
	gameField[6][6] = 0;

}

function Fujiyama() {
	//Row 0
	gameField[4][0] = 1;
	gameField[5][0] = 1;
	gameField[6][0] = 1;
	gameField[7][0] = 1;
	gameField[8][0] = 1;
	
	//Row 1
	gameField[4][1] = 1;
	gameField[5][1] = 0;
	gameField[6][1] = 0;
	gameField[7][1] = 1;
	
	//Row 2
	gameField[4][2] = 1;
	gameField[5][2] = 0;
	gameField[6][2] = 1;
	
	//Row 3
	gameField[4][3] = 1;
	gameField[5][3] = 1;
	
	//Row 5
	gameField[3][5] = 0;
	gameField[4][5] = 0;
	
	//Row 6
	gameField[2][6] = 0;
	gameField[3][6] = 1;
	gameField[4][6] = 0;
	
	//Row 7
	gameField[1][7] = 0;
	gameField[2][7] = 1;
	gameField[3][7] = 1;
	gameField[4][7] = 0;
	
	//Row 8
	gameField[0][8] = 0;
	gameField[1][8] = 0;
	gameField[2][8] = 0;
	gameField[3][8] = 0;
	gameField[4][8] = 0;

}

function GermanDaisy() {
	
	//Row 1
	gameField[3][1] = 1;
	gameField[4][1] = 1;
	gameField[7][1] = 0;
	gameField[8][1] = 0;
	
	//Row 2
	gameField[2][2] = 1;
	gameField[3][2] = 1;
	gameField[4][2] = 1;
	gameField[6][2] = 0;
	gameField[7][2] = 0;
	gameField[8][2] = 0;
	
	//Row 3
	gameField[2][3] = 1;
	gameField[3][3] = 1;
	gameField[6][3] = 0;
	gameField[7][3] = 0;

	//Row 5
	gameField[1][5] = 0;
	gameField[2][5] = 0;
	gameField[5][5] = 1;
	gameField[6][5] = 1;
	
	//Row 6
	gameField[0][6] = 0;
	gameField[1][6] = 0;
	gameField[2][6] = 0;
	gameField[4][6] = 1;
	gameField[5][6] = 1;
	gameField[6][6] = 1;
	
	//Row 7
	gameField[0][7] = 0;
	gameField[1][7] = 0;
	gameField[4][7] = 1;
	gameField[5][7] = 1;

}

//Splash screen function
var SplashScreen = new Class({
	Implements: Options,
	options: {
		'delay': 4000,
		'showOnce': true
	},
	initialize: function (element, options) {
		this.setOptions(options);
		this.splash = document.id(element);
		if (this.options.showOnce) {
			// check cookie to see if splash has been shown in this session
			var splashCookie = Cookie.read('showSplashCookie') || 'yes';
			if (splashCookie == 'yes') {
				this.showSplash();
				Cookie.write('showSplashCookie', 'no');
			}
		} else {
			this.showSplash();
		}
	},
	showSplash: function () {
		this.splash.setStyles({
			'height': 430,
			'display': 'block',
			'opacity': '1',
			'width': 600
		});

		this.closeSplash.delay(this.options.delay, this);
	},
	closeSplash: function () {
		this.splash.fade('out');
	}
});

//Setup window and run Splashs
window.addEvent('domready', function () {

	document.body.addEvent('touch', function (e) {
		e.preventDefault();
	});

	document.body.addEvent('touchmove', function (e) {
		e.preventDefault();
	});

	document.body.addEvent('touchend', function () {

	});
	
	var splash = new SplashScreen('splashlmglogo', { 'delay': '3000', 'showOnce': false });
	var splash2 = new SplashScreen('splashmslogo', { 'delay': '6000', 'showOnce': false });

	Init();
	setInterval("Timer()", 500);
}); 