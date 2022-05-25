class Checkers {
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
object Main2 {
  def main(args: Array[String]): Unit = {
    var checkers : Checkers = new Checkers;
  //  checkers.parseInput("lolo")
    var turn = 1;
    var state : State = new State(9, 9);
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