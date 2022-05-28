import Chess.letters
import definitions.{controller, drawer, state}
import scalafx.scene.Node
import scalafx.scene.image.{Image, ImageView}
import scalafx.scene.paint.Color
import scalafx.scene.shape.Rectangle
import scalafx.scene.text.Text

object Checkers {
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
  }
  def initState(state: State): State = {
    var board = state.state
    state.flag = true
    for(i <- 1 until(board.length)) {
      for (j <- 1 until (board.length)) {
        if (i < board.length / 2) {
          if (i % 2 != 0 && j % 2 == 0) board(i)(j) = "b"
          else if(i % 2 == 0 && j % 2 != 0) board(i)(j) = "b"
          else board(i)(j) = "-"
        }
        else if(i > (board.length/2) + 1) {
          if (i % 2 != 0 && j % 2 == 0) board(i)(j) = "w"
          else if(i % 2 == 0 && j % 2 != 0) board(i)(j) = "w"
          else board(i)(j) = "-"
        }
        else board(i)(j) = "-"
      }
    }
    state
  }

  def controller(move: String, state: State, turn: Int): State = {
    /**
     *
     * 1. validateInput
     * 2. parseInput
     * 3. checkPermittedMoves
     * 4. updateState
     *
     * returns updatedState to be passed to the drawer function
     */
      
    val xf = move.charAt(0).asDigit
    val yf = move.charAt(1).asDigit
    val xt = move.charAt(2).asDigit
    val yt = move.charAt(3).asDigit

    val board = state.state
    if(turn == 0){
      // moving a wrong piece (white) or from null pos
      if(board(xf)(yf) != "b" && board(xf)(yf) != "kb") state.flag = false;
      else if(board(xf)(yf) != "kb") {
          // diagonal movement and to pos is occupied by an opponent/null check
        if((xt - xf == (yt - yf).abs) && board(xt)(yt) != "b" && board(xt)(yt) != "kb") {
          state.flag = true;
          board(xf)(yf) = "-" // reset from pos
          // promotion
          if (xt == 8) board(xt)(yt) = "kb"
          // eat
          else if (board(xt)(yt) != "-") {
            if (board(xt + xt - xf)(yt + yt - yf) != "-") board(xt)(yt) = "b"
            else if (board(xt + xt - xf)(yt + yt - yf) == "-") { // jump case
              board(xt)(yt) = "-"
              if(xt +xt - xf == 8) board(xt + xt - xf)(yt + yt - yf) = "kb"
              else board(xt + xt - xf)(yt + yt - yf) = "b"
            }
          }
          else board(xt)(yt) = "b" // normal move
        }
        else state.flag = false;
      }
      else { // king piece
        // diagonal movement and to pos is occupied by an opponent/null check
        if(((xt - xf).abs == (yt - yf).abs) && board(xt)(yt) != "b" && board(xt)(yt) != "kb") {
          state.flag = true;
          board(xf)(yf) = "-" // reset from pos
          // eat
          if (board(xt)(yt) != "-") {
            if (board(xt + xt - xf)(yt + yt - yf) != "-") board(xt)(yt) = "kb"
            else if (board(xt + xt - xf)(yt + yt - yf) == "-") { // jump case
              board(xt)(yt) = "-"
              board(xt + xt - xf)(yt + yt - yf) = "kb"
            }
          }
          else board(xt)(yt) = "kb" // normal move
        }
      }
    }
      /**** player white turn ***/
    else {
      // moving a wrong piece (block) or from null pos
      if(board(xf)(yf) != "w" && board(xf)(yf) != "kw") state.flag = false;
      else if(board(xf)(yf) != "kw") {
        // diagonal movement and to pos is occupied by an opponent/null check
        if((xf - xt == (yt - yf).abs) && board(xt)(yt) != "w" && board(xt)(yt) != "kw") {
          state.flag = true;
          board(xf)(yf) = "-" // reset from pos
          // promotion
          if (xt == 1) board(xt)(yt) = "kw"
          // eat
          else if (board(xt)(yt) != "-") {
            if (board(xt + xt - xf)(yt + yt - yf) != "-") board(xt)(yt) = "w"
            else if (board(xt + xt - xf)(yt + yt - yf) == "-") { // jump case
              board(xt)(yt) = "-"
              if(xt + xt - xf == 1) board(xt + xt - xf)(yt + yt - yf) = "kw"
              else board(xt + xt - xf)(yt + yt - yf) = "w"
            }
          }
          else board(xt)(yt) = "w" // normal move
        }
        else state.flag = false;
      }
      else { // king piece
        // diagonal movement and to pos is occupied by an opponent/null check
        if(((xf - xt).abs == (yt - yf).abs) && board(xt)(yt) != "w" && board(xt)(yt) != "kw") {
          state.flag = true;
          board(xf)(yf) = "-" // reset from pos
          // eat
          if (board(xt)(yt) != "-") {
            if (board(xt + xt - xf)(yt + yt - yf) != "-") board(xt)(yt) = "kw"
            else if (board(xt + xt - xf)(yt + yt - yf) == "-") { // jump case
              board(xt)(yt) = "-"
              board(xt + xt - xf)(yt + yt - yf) = "kw"
            }
          }
          else board(xt)(yt) = "kw" // normal move
        }
      }
    }
    state
  }

  def drawer(): Unit = {

  }
}
//object Main2 {
//  def main(args: Array[String]): Unit = {
//    var checkers : Checkers = new Checkers;
//  //  checkers.parseInput("lolo")
//    var turn = 1;
//    var state : State = new State(9, 9);
//    state = checkers.initState(state);
//   // state.state(8)(3) = "kb"
//    while(true) {
//      turn = turn ^ 1
//      state.printState()
//      println("Please enter -from- and -to- pos: ");
//      val move = scala.io.StdIn.readLine()
//      checkers.controller(move, state, turn)
//    }
//  }
//}