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
  def checkers_initial = Array(
    Array("w","-","w","-","w","-","w","-"),
    Array("-","w","-","w","-","w","-","w"),
    Array("w","-","w","-","w","-","w","-"),
    Array("-","-","-","-","-","-","-","-"),
    Array("-","-","-","-","-","-","-","-"),
    Array("-","b","-","b","-","b","-","b"),
    Array("b","-","b","-","b","-","b","-"),
    Array("-","b","-","b","-","b","-","b"),
  )
  val checkers_BOARDWIDTH:Int = 560//in pixels
  val checkers_click_handler:click_to_move = (x:Double, y:Double)=>{
    val X = ('A'+(x/(checkers_BOARDWIDTH/8)).toInt).toChar
    val Y = 8 - (y/(checkers_BOARDWIDTH/8)).toInt
    s"${X.toString}${Y}"
  }
  var texts :List[Text]= List()
  var board: List[Rectangle] = List()
  val checkers_drawer:drawer =  (x:state)=>{
    var lst: List[Node]= List()
    lst = draw_board()
    for (row <- Range(0, 8)) {
      for (col <- Range(0, 8)) {
        if(x(7-row)(7-col)!="-" )
          lst = lst.appended(draw_piece(((8*col)+row),x(7-row)(7-col)))
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
      val t = new Text(board(board.length-1).x.value  ,board(board.length-1).y.value +70,c)
      t.setStyle("-fx-font: 15 sans-serif;")
      t.fill = Color(0,0,0,0.7)
      texts= texts.appended(t)
    }
    val i =0;
    for( i <- 1 to 8) {
      val n = new Text( board((56+(i-1))).x.value +60  ,board((56+(i-1))).y.value+15 ,(9-i).toString)
      n.setStyle("-fx-font: 15 sans-serif;")
      n.fill = Color(0,0,0,0.7)
      texts= texts.appended(n)
    }
    lst = board ::: texts
    lst
  }

  def revertIndex(x: Int): Int = 7 - x

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
    case 0 => 0
    case 1 => 7
  }
  def offsetMatch(turn: Int, xf: Int, xt: Int) = (turn, xf, xt) match {
    case (0, xf, xt) if xt -xf == -1 => 1
    case (1, xf, xt) if xt - xf == 1 => 1
    case _ => 0
  }
  def forceEat(board: state, turn: turn): ArrayBuffer[String] = {
    val legalMoves = ArrayBuffer[String]();;
    val playerPiece = turnMatch(turn)
    val playerKingPiece = "k".concat(playerPiece)
    if (turn == 0) {
      for (x <- 0 until board.length) {
        for (y <- 0 until board.length) {
          val i = revertIndex(x)
          val j = revertIndex(y)
          if (board(i)(j) == playerPiece || board(i)(j) == playerKingPiece) {
            // down right
            if (i != 0 && j != 0 && (board(i - 1)(j - 1) == "w" || board(i - 1)(j - 1) == "kw"))
              legalMoves += (i - 1).toString.concat((j - 1).toString);
            // down left
            if (i != 0 && j != 7 && (board(i - 1)(j + 1) == "w" || board(i - 1)(j + 1) == "kw"))
              legalMoves += (i - 1).toString.concat((j + 1).toString);
          }
          if (board(i)(j) == playerKingPiece) {
            // up right
            if (i != 7 && j != 0 && (board(i + 1)(j - 1) == "w" || board(i + 1)(j - 1) == "kw"))
              legalMoves += (i + 1).toString.concat((j - 1).toString)
            // up left
            if (i != 7 && j != 7 && (board(i + 1)(j + 1) == "w" || board(i + 1)(j + 1) == "kw"))
              legalMoves += (i + 1).toString.concat((j + 1).toString)
          }
        }
      }

    }
    else {
      for(i <- 0 until board.length) {
        for(j <- 0 until board.length) {
          if(board(i)(j) == "w" || board(i)(j) == "kw") {
            // up left
            if(i != 7 && j != 7 && (board(i+1)(j+1) == "b" || board(i+1)(j+1) == "kb"))
              legalMoves += (i+1).toString.concat((j+1).toString);
            // up right
            if(i != 7 && j != 0 && (board(i+1)(j-1) == "b" || board(i+1)(j-1) == "kb"))
              legalMoves += (i+1).toString.concat((j-1).toString)
          }
          if(board(i)(j) == "kb") {
            // down left
            if (i != 0 && j != 7 && (board(i - 1)(j + 1) == "b" || board(i - 1)(j + 1) == "kb"))
              legalMoves += (i - 1).toString.concat((j + 1).toString);
            // down right
            if (i != 0 && j != 0 && (board(i - 1)(j - 1) == "b" || board(i - 1)(j - 1) == "kb"))
              legalMoves += (i - 1).toString.concat((j - 1).toString);
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
    val yf = revertIndex(convertCharacterToIndex(move.charAt(0)))
    val xf = revertIndex(8 - move.charAt(1).asDigit)
    val yt = revertIndex(convertCharacterToIndex(move.charAt(2)))
    val xt = revertIndex(8 - move.charAt(3).asDigit)

    if(!forceEat(board, turn).isEmpty && !forceEat(board, turn).contains(xt.toString.concat(yt.toString))) {
      return false
    }
    if(!unitMove(xf, xt)) return false

    if(board(xf)(yf) != turnMatch(turn) && board(xf)(yf) != "k".concat(turnMatch(turn)))
      return false

    if(xf == xt || yf == yt) return false
    true
  }
  def isOuterEdge(x: Int): Boolean = {
    if(x == 0 || x == 7) return true;
    false
  }

  def checkers_controller:controller=(board: state,move: input,turn: turn)=> {
    var flag: Boolean = true;
    var xf, yf, xt, yt = 0
    val playerPiece = turnMatch(turn)
    val playerKingPiece = ("k".concat(playerPiece))
    try {
      yf = revertIndex(convertCharacterToIndex(move.charAt(0)))
      xf = revertIndex(8 - move.charAt(1).asDigit)
      yt = revertIndex(convertCharacterToIndex(move.charAt(2)))
      xt = revertIndex(8 - move.charAt(3).asDigit)
    }
    catch {
      case e: Exception => flag = false
    }

    if (!flag) {
      (board, status.Invalid, "Invalid move")
    }
    else {
      flag = false;
       if (!isValidMove(move, board, turn)) {
        flag = false;
      }

      else if (board(xf)(yf) != playerKingPiece) {
        // diagonal movement and to pos is occupied by an opponent/null check
        if (offsetMatch(turn, xf, xt) == 1 && board(xt)(yt) != playerPiece && board(xt)(yt) != playerKingPiece) {
          flag = true;
          board(xf)(yf) = "-" // reset from pos
          if (xt == promotionMatch(turn)) board(xt)(yt) = playerKingPiece // promotion
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
        if (((xt - xf).abs == (yt - yf).abs) && board(xt)(yt) != playerPiece && board(xt)(yt) != playerKingPiece) {
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
            else board(xt)(yt) = playerKingPiece;
          }
          else board(xt)(yt) = playerKingPiece // normal move
        }
      }


      (board, if (flag) if (turn % 2 == 0) status.Player_1_turn else status.Player_0_turn else status.Invalid, if (!flag) "Invalid move" else "")
    }
  }
}