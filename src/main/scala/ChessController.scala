class ChessController {

  def intialState () : State ={
    var state : State = new State(8,8);
    state.state = Array(
                          Array("R", "N", "B","Q","K","B","N","R"),
                          Array("P", "P", "P","P","P", "P", "P","P"),
                          Array("-",".","-",".","-",".","-","."),
                          Array(".","-",".","-",".","-",".","-"),
                          Array("-",".","-",".","-",".","-","."),
                          Array(".","-",".","-",".","-",".","-"),
                          Array("p", "p", "p","p","p", "p", "p","p"),
                          Array("r", "n", "b","q","k","b","n","r"),
                          )
    state.flag=true
    state
  }
  def validInput(move: String) : Boolean = {
    if(move.length!=4){
      return false
    }
    val xfrom = move.charAt(0)
    val yfrom = move.charAt(1).asDigit
    val xto = move.charAt(2)
    val yto = move.charAt(3).asDigit

    if(xfrom.<('A') || xfrom.>('H') || xto.<('A') || xto.>('H') || yfrom.<(1) || yfrom.>(8) || yto.<(1) || yto.>(8) ){
          println("Out of index")
          return false
    }
    true
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
    }
  }

  def convertIndexToIndex(x: Int) : Int ={
    x match {
      case (8) => return 0
      case (7) => return 1
      case (6) => return 2
      case (5) => return 3
      case (4) => return 4
      case (3) => return 5
      case (2) => return 6
      case (1) => return 7
    }
    x
  }

  def controller(move: String, state: State, turn: Int): State = {
    var index= new Array[Int](4);
    println(move)
    /*
    steps >> 1) validate input  "tmam an l7rka gwa al board w anha kmlaa"
             2) valiate move
     */
     if(validInput(move)){
       index(0) = convertIndexToIndex(move(1).asDigit)
       index(1) = convertCharacterToIndex(move(0))
       index(2) = convertIndexToIndex(move(3).asDigit)
       index(3) = convertCharacterToIndex(move(2))


       println(index(0) + " " + index(1) + " " + index(2) + " " + index(3))
     }

    state
  }

  }

object MainChess{
  def main(args: Array[String]): Unit = {
    var chess : ChessController = new ChessController;
    var turn = 1;
   var state : State = new State(8, 8);
    state = chess.intialState()

    while(true) {
      turn = turn ^ 1
     state.printState()
      println("Please enter -from- and -to- pos: ");
      val move = scala.io.StdIn.readLine()
      chess.controller(move, state, turn)
    }
  }
}
