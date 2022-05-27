import definitions.input

import scala.collection.mutable.{ArrayBuffer, ListBuffer}

class Checkers {
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
  def initState(state: State): State = {
    var board = state.state
    state.flag = true
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
    state
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
  def forceEat(state: State, turn: Int): ArrayBuffer[String] = {
    val legalMoves = ArrayBuffer[String]();;
    val board = state.state;
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

  def isValidMove(move: String, state: State, turn: Int): Boolean = {
    val board = state.state;
    if(!forceEat(state, turn).contains(move.substring(2,4)) && !unitMove(move.charAt(0), move.charAt(2))) {
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
    if(!isValidInput(move)) {
      state.flag = false
      return state
    }
    val xf = convertCharacterToIndex(move.charAt(0))
    val yf = move.charAt(1).asDigit
    val xt = convertCharacterToIndex(move.charAt(2))
    val yt = move.charAt(3).asDigit
    var legalMoves = ArrayBuffer[String]()
    legalMoves = forceEat(state, turn)
    /* val moveTo: String = move.charAt(2).toString.concat(move.charAt(3).toString)
     for(moves <- legalMoves if !legalMoves.contains(moveTo)) {
       println("lolo")
       state.flag = false
       return state;
     }
 */
    val playerPiece = turnMatch(turn)
    val playerKingPiece = "k".concat(playerPiece)
    val board = state.state
    // moving a wrong piece (color) or from null pos or diagonal move length > 1
    //  val diagonal = (xf - xt).abs;
    if(!isValidMove(move, state, turn)) {
      state.flag = false
      return state
    }
    /*
      if(board(xf)(yf) != playerPiece && board(xf)(yf) != playerKingPiece) {
        state.flag = false
        return state
      };
*/
    if(board(xf)(yf) != playerKingPiece) {
      // diagonal movement and to pos is occupied by an opponent/null check
      println("ana hena hehe")
      if(offsetMatch(turn, xf, xt) == 1 && board(xt)(yt) != playerPiece && board(xt)(yt) != playerKingPiece) {
        state.flag = true;
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
      else state.flag = false;
    }
    else { // king piece
      // diagonal movement and to pos is occupied by an opponent/null check
      if(((xt - xf).abs == (yt - yf).abs) && board(xt)(yt) != playerPiece && board(xt)(yt) != playerKingPiece) {
        state.flag = true;
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
    state
  }

  def drawer(): Unit = {

  }
}
object Main2 {
  def main(args: Array[String]): Unit = {
    var checkers : Checkers = new Checkers;
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
    }
  }
}