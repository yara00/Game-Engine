import Chess.letters
import Engine.state
import definitions.{click_to_move, controller, drawer, input, state, status, turn}
import scalafx.scene.Node
import scalafx.scene.image.{Image, ImageView}
import scalafx.scene.paint.Color
import scalafx.scene.shape.Rectangle
import scalafx.scene.text.Text

import scala.collection.mutable.ArrayBuffer

object Checkers {
    val checkers_BOARDWIDTH:Int = ???//in pixels

  val checkers_click_handler:click_to_move = (x:Double, y:Double)=>{
    val X ='A' + Math.floor(x%(checkers_BOARDWIDTH/8))
    val Y = 8  - Math.floor(y%(checkers_BOARDWIDTH/8))
    s"${x}${Y}"
  }
  var texts :List[Text]= List()
  var board: List[Rectangle] = List()
  val checkers_drawer:drawer =  (x:state)=>{
    var lst: List[Node]= List()
    lst = draw_board()
    for (row <- Range(0, 7)) {
      for (col <- Range(0, 7)) {
        if(x(row)(col)!="-" )
          lst = lst.appended(draw_piece(((8*row)+col),x(row)(col)))
      }
    }
    lst
  }
  def draw_piece(n:Int, s:String) : ImageView = {
    s match {
      case "w" => return draw_w(n)
      case "b" => return draw_b(n)
      case "kw" => return draw_kw(n)
      case "kb" => return draw_kb(n)

    }
  }
    def draw_w(n: Int): ImageView = {
      val W = new Image("file:assets/r.png", 70, 70, false, false)
      val w = new ImageView(W)
      w.setX(board(n).x.value)
      w.setY(board(n).y.value)
      w
    }

  def draw_b(n: Int): ImageView = {
    val B  = new Image("file:assets/b.png", 70, 70, false, false)
    val b = new ImageView(B)
    b.setX(board(n).x.value)
    b.setY(board(n).y.value)
    b
  }
    def draw_kb(n: Int): ImageView = {
      val KB  = new Image("file:assets/kb.png", 70, 70, false, false)
      val kb = new ImageView(KB)
      kb.setX(board(n).x.value)
      kb.setY(board(n).y.value)
      kb
    }
    def draw_kw(n: Int): ImageView = {
      val KW  = new Image("file:assets/rk.png", 70, 70, false, false)
      val kw = new ImageView(KW)
      kw.setX(board(n).x.value)
      kw.setY(board(n).y.value)
      kw
    }

  def draw_board(): List[Node]={
    var lst: List[Node]= List()
    var a = 0;
    var b = 0;
    for( a <- 1 to 8){
      for( b <- 1 to 8){
        val rect = Rectangle(70*(a-1),70*(b-1),70,70)
        if(b%2==0) {
          if (a % 2 == 0) {
            rect.setFill(Color.web("#f0d9b5"))
          }
          else {
            rect.setFill(Color.web("#b58863"))
          }
        }
        else
        {
          if (a % 2 == 0) {
            rect.setFill(Color.web("#b58863"))
          }
          else {
            rect.setFill(Color.web("#f0d9b5"))

          }
        }
        rect.setId(a.toString+ b.toString)
        board = board.appended(rect)
        // content = board

      }
      val c = letters(a)
      val t = new Text(board(board.length-1).x.value+25  ,board(board.length-1).y.value +120,c)
      t.setStyle("-fx-font: 30 sans-serif;")
      t.setFill(Color.Black)
      texts= texts.appended(t)
    }
    val i =0;
    for( i <- 1 to 8) {
      val n = new Text( board((56+(i-1))).x.value + 120  ,board((56+(i-1))).y.value +45 ,(9-i).toString)
      n.setStyle("-fx-font: 30 sans-serif;")
      texts= texts.appended(n)
    }
    lst = board ::: texts
    lst
  }

//  val Checkers_controller:controller=(state,input,turn)=>{
//    //input form: "x0 y0 x1 y1 "
//    // (0,0) is the top left square of the board as it's shown in the scene
//    // (0,7) is the bottom left square
//
//
//    //      (0,0)------ x increases ---------->
//    //        |             |
//    //        |             y
//    //        |             |
//    //        |-----x----->(x,y)
//    //   y increases
//    //        |
//    //        |
//    //       \ /
//
//    //validate the move from (x0,y0) to (x1,y1)
//    // ... perform move or return Invalid
//    // return:
//    // (state:new state or [null or old state if invalid] (whatever u like), status:[check status in definitions in xo.scala], String:[a message to show on screen(not necessary)])
//  }
//
  // override
  def parseInput(input: String): Unit = {
    println("Please enter -from- pos: ");
    val from = scala.io.StdIn.readLine()
    println("Please enter -to- pos: ");
    val to = scala.io.StdIn.readLine()
    println("Input recieved is from " + from + " and to " + to)
    println(revertIndex(from.charAt(0).asDigit))
  }
  def revertIndex(x: Int): Int = 8 - x
  def initState(board: state): state = {
   // var board = state.state
   // state.flag = true
    for(i <- 0 until(board.length)) {
      for (j <- 0 until (board.length)) {
        if (i < (board.length / 2) -1) {
          if (i % 2 == 0 && j % 2 != 0) board(i)(j) = "b"
          else if(i % 2 != 0 && j % 2 == 0) board(i)(j) = "b"
          else board(i)(j) = "-"
        }
        else if(i > (board.length/2)) {
          if (i % 2 != 0 && j % 2 == 0) board(i)(j) = "w"
          else if(i % 2 == 0 && j % 2 != 0) board(i)(j) = "w"
          else board(i)(j) = "-"
        }
        else board(i)(j) = "-"
      }
    }
    board
  }

  def convertCharacterToIndex(x: Char) : Int ={
    x match {
      case ('A') => return 0
      case ('B') => return 1
      case ('C') => return 2
      case ('D') => return 3
      case ('E') => return 4
      case ('F') => return 5
      case ('G') => return 6
      case ('H') => return 7
      case _ => return 8
    }
  }

  def turnMatch(turn: Int): String = turn match {
    case 0 => "b"
    case 1 => "w"
  }

  def promotionMatch(turn: Int): Int = turn match {
    case 0 => 7
    case 1 => 0
  }
  def offsetMatch(turn: Int, xf: Int, xt: Int) = (turn, xf, xt) match {
    case (0, xf, xt) if xt -xf == 1 => 1
    case (1, xf, xt) if xf - xt == 1 => 1
    case _ => 0
  }
  def forceEat(board: state, turn: turn): ArrayBuffer[String] = {
    val legalMoves = ArrayBuffer[String]();;
   // val board = state.state;
    val playerPiece = turnMatch(turn)
    val playerKingPiece = "k".concat(playerPiece)
    if (turn == 0) {
      for (i <- 0 until board.length) {
        for (j <- 0 until board.length) {
          if (board(i)(j) == playerPiece || board(i)(j) == playerKingPiece) {
            // down left
            //00 --
            //77 -+
            if (i != 7 && j != 0 && (board(i + 1)(j - 1) == playerPiece || board(i + 1)(j - 1) == playerKingPiece))
              legalMoves += (i + 1).toString.concat((j - 1).toString);
            // down right
            if (i != 7 && j != 7 && (board(i + 1)(j + 1) == playerPiece || board(i + 1)(j + 1) == playerKingPiece))
              legalMoves += (i + 1).toString.concat((j + 1).toString);
          }
          if (board(i)(j) == playerKingPiece) {
            // up left
            //70 +-
            //77 ++
            if (i != 0 && j != 0 && (board(i - 1)(j - 1) == playerPiece || board(i - 1)(j - 1) == playerKingPiece))
              legalMoves += (i - 1).toString.concat((j - 1).toString)
            // up right
            if (i != 0 && j != 7 && (board(i - 1)(j + 1) == playerPiece || board(i - 1)(j + 1) == playerKingPiece))
              legalMoves += (i - 1).toString.concat((j + 1).toString)
          }
        }
      }

    }
    else {
      for(i <- 0 until board.length) {
        for(j <- 0 until board.length) {
          if(board(i)(j) == "w" || board(i)(j) == "kw") {
            // up left
            if(i != 0 && j != 0 && (board(i-1)(j-1) == "b" || board(i-1)(j-1) == "kb"))  legalMoves += (i-1).toString.concat((j-1).toString)
            // up right
            if(i != 0 && j != 7 && (board(i-1)(j+1) == "b" || board(i-1)(j+1) == "kb"))  legalMoves += (i-1).toString.concat((j+1).toString)
          }
          if(board(i)(j) == "kb") {
            // down left
            if(i != 7 && j != 0 && (board(i+1)(j-1) == "b" || board(i+1)(j-1) == "kb"))  legalMoves += (i+1).toString.concat((j-1).toString);
            // down right
            if(i != 7 && j != 7 && (board(i+1)(j+1) == "b" || board(i+1)(j+1) == "kb"))  legalMoves += (i+1).toString.concat((j+1).toString);
          }
        }
      }
    }
    legalMoves
  }

  def unitMove(xf: Int, xt: Int): Boolean = {
    if((xf - xt).abs == 1) return true
    false
  }

  def isValidMove(move: String, board: state, turn: Int): Boolean = {
   // val board = state.state;
    if(!forceEat(board, turn).contains(move.substring(2,4)) && !unitMove(move.charAt(0), move.charAt(2))) {
      println("lolo")
      return false
    }
    val xf = convertCharacterToIndex(move.charAt(0))
    val yf = move.charAt(1).asDigit
    if(board(xf)(yf) != turnMatch(turn) && board(xf)(yf) != "k".concat(turnMatch(turn))) {
      println("soso")
      return false
    }
    true
  }
  def isOuterEdge(x: Int): Boolean = {
    if(x == 0 || x == 7) return true;
    false
  }
  def isValidInput(input: input): Boolean = {
    val xf = convertCharacterToIndex(input.charAt(0))
    val yf = convertCharacterToIndex(input.charAt(2))
    val xt = input.charAt(1).asDigit
    val yt = input.charAt(3).asDigit
    if (xf.<(0) || xf.>(7) || xt.<(0) || xt.>(7) || yf.<(0) || yf.>(7) || yt.<(0) || yt.>(7)) {
      println("Out of index")
      return false
    }
    true
  }
  val Checkers_controller:controller=(board: state,move: input,turn: turn)=>{
    /**
     *
     * 1. validateInput
     * 2. parseInput
     * 3. checkPermittedMoves
     * 4. updateState
     *
     * returns updatedState to be passed to the drawer function
     */
    val xf = convertCharacterToIndex(move.charAt(0))
    val yf = move.charAt(1).asDigit
    val xt = convertCharacterToIndex(move.charAt(2))
    val yt = move.charAt(3).asDigit
    var legalMoves = ArrayBuffer[String]()
    val playerPiece = turnMatch(turn)
    val playerKingPiece = ("k".concat(playerPiece))
    var flag :Boolean=false;
    if(!isValidInput(move)) {
      flag = false

    }

    //legalMoves = forceEat(state, turn)
    /* val moveTo: String = move.charAt(2).toString.concat(move.charAt(3).toString)
     for(moves <- legalMoves if !legalMoves.contains(moveTo)) {
       println("lolo")
       state.flag = false
       return state;
     }
 */


 //   val board = state.state
    // moving a wrong piece (color) or from null pos or diagonal move length > 1
    //  val diagonal = (xf - xt).abs;
    else if(!isValidMove(move, board, turn)) {
     flag = false;
    }
    /*
      if(board(xf)(yf) != playerPiece && board(xf)(yf) != playerKingPiece) {
        state.flag = false
        return state
      };
*/
    else if(board(xf)(yf) != playerKingPiece) {
      // diagonal movement and to pos is occupied by an opponent/null check
      println("ana hena hehe")
      if(offsetMatch(turn, xf, xt) == 1 && board(xt)(yt) != playerPiece && board(xt)(yt) != playerKingPiece) {
       flag = true;
        board(xf)(yf) = "-" // reset from pos
        if (xt == promotionMatch(turn)) board(xt)(yt) = playerKingPiece  // promotion
        else if (board(xt)(yt) != "-") { // eat
          if (!isOuterEdge(yt)) {
            if (board(xt + xt - xf)(yt + yt - yf) != "-") board(xt)(yt) = playerPiece
            else if (board(xt + xt - xf)(yt + yt - yf) == "-") { // jump case
              board(xt)(yt) = "-"
              if (xt + xt - xf == promotionMatch(turn)) board(xt + xt - xf)(yt + yt - yf) = playerKingPiece
              else board(xt + xt - xf)(yt + yt - yf) = playerPiece
            }
          }
          else board(xt)(yt) = playerPiece;
        }
        else {
          board(xt)(yt) = playerPiece
        } // normal move
      }
      else flag = false;
    }
    else { // king piece
      // diagonal movement and to pos is occupied by an opponent/null check
      if(((xt - xf).abs == (yt - yf).abs) && board(xt)(yt) != playerPiece && board(xt)(yt) != playerKingPiece) {
        flag = true;
        board(xf)(yf) = "-" // reset from pos
        if (board(xt)(yt) != "-") { // eat
          if (!isOuterEdge(yt)) {
            if (board(xt + xt - xf)(yt + yt - yf) != "-") board(xt)(yt) = playerKingPiece
            else if (board(xt + xt - xf)(yt + yt - yf) == "-") { // jump case
              board(xt)(yt) = "-"
              board(xt + xt - xf)(yt + yt - yf) = playerKingPiece
            }
          }
          else board(xt)(yt) = playerPiece;
        }
        else board(xt)(yt) = playerKingPiece // normal move
      }
    }
    // 2132 5041 3241 6150 5647 2534 4736
    (board,if(flag) if(turn%2==0)status.Player_1_turn else status.Player_0_turn else status.Invalid,if(!flag) "Invalid move" else "")
  }

  def drawer(): Unit = {

  }
}
object Main2 {
  def main(args: Array[String]): Unit = {
  /*  var checkers : Checkers = new Checkers;
    //checkers.parseInput("lolo")
    var turn = 1;
    var state : State = new State(8, 8);
    state = checkers.initState(state);
    // state.state(8)(3) = "kb"
    while(true) {
      turn = turn ^ 1
      state.printState()
      println("Please enter -from- and -to- pos: ");
      val move = scala.io.StdIn.readLine()
      checkers.controller(move, state, turn)
    }*/
  }
}