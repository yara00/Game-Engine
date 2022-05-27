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

  def validQueenMove (index : Array[Int],state: State) : Boolean ={
    validRookMove(index,state) || validBishopMove(index,state)
  }

  def validRookMove (index : Array[Int],state: State) : Boolean = {
    if((index(0)==index(2) && index(1)!=index(3)) || (index(0)!=index(2) && index(1)==index(3))){
      clearRookWay(index,state)
    }else {
      false
    }
  }

  def clearRookWay(index:Array[Int],state: State):Boolean = {
    var deltaX:Int = 0
    var deltaY:Int = 0
    def setDeltas(dx:Int,dy:Int):Unit = (dx,dy) match {
      case (dx:Int,0) if dx>0 =>
        deltaX=1
      case (dx:Int,0) if dx<0 =>
        deltaX= -1
      case (0,dy:Int) if dy>0 =>
        deltaY= 1
      case (0,dy:Int) if dy<0 =>
        deltaY = -1
      case (0,0) => println("failed to match")
    }
    setDeltas(index(3)-index(1) , index(2)-index(0))
    var startX=index(1)+deltaX
    var startY=index(0)+deltaY
    while( (startY!=index(2) || startX!=index(3)) && !hasObsticale(startX,startY,state)){
      startY+=deltaY
      startX+=deltaX
    }
    startY==index(2) && startX==index(3)
  }



  def validBishopMove(index:Array[Int],state: State): Boolean ={
    if(Math.abs(index(3)-index(1)) == Math.abs(index(2)-index(0)))
      clearBishopWay(index,state)
    else false
  }


  def clearBishopWay(index:Array[Int],state: State):Boolean = {
    var deltaX:Int = 0;
    var deltaY:Int = 0

    def setDeltas(dx:Int,dy:Int):Unit = (dx,dy) match {
      case (dx:Int,dy:Int) if dx>0 && dy>0 =>
        deltaY=1
        deltaX=1
      case (dx:Int,dy:Int) if dx>0 && dy<0 =>
        deltaY= -1
        deltaX=1
      case (dx:Int,dy:Int) if dx<0 && dy>0 =>
        deltaY=1
        deltaX= -1
      case (dx:Int,dy:Int) if dx<0 && dy<0 =>
        deltaY = -1
        deltaX = -1
      case (0,0) => println("failed to match")
    }

    setDeltas(index(3)-index(1) , index(2)-index(0))

    var startX=index(1)+deltaX
    var startY=index(0)+deltaY


    while( startY!=index(2) && startX!=index(3) && !hasObsticale(startX,startY,state)){
      startY+=deltaY
      startX+=deltaX
    }

    startY==index(2) && startX==index(3)
  }

  def inBounds(x:Int,y:Int):Boolean = x<8 && x>=0 && y<8 && y>=0
  def hasObsticale(x:Int,y:Int,state: State):Boolean = state.state(y)(x) != "-" && state.state(y)(x) != "."



def validKingMove(index:Array[Int]):Boolean = {
  if(Math.abs(index(3)-index(1))<=1 && Math.abs(index(2)-index(0))<=1){
    true
  }else {
    false
  }
}

  def validKnightMove(index:Array[Int]): Boolean ={
    if((Math.abs(index(3)-index(1))==1 && Math.abs(index(2)-index(0))==2) ||
      (Math.abs(index(3)-index(1))==2 && Math.abs(index(2)-index(0))==1)){
      true
    }else {
     false
    }
  }


  def validPawnMove(index : Array[Int] , turn : Int, board : State) : Boolean ={
    if(turn == 0){      // white pawn

      if(board.state(index(2))(index(3)) != "." && board.state(index(2))(index(3)) !="-") {  // eat
        if ((index(0)-index(2)==1) && (Math.abs(index(3)-index(1))==1) ){
          println("eaaat white")
          true
        }else{
          println("falseee eat")
          false
        }
      }else{      // move
        if(index(0)==6){   // first move
          if((index(3)==index(1)) && ((index(0)-index(2))<=2)){
            println("forward white")
            true
          }else{
            println("falseee white")
            false
          }
        }else{
          if((index(3)==index(1)) && (index(0)-index(2)==1)){   // move
            true
          }else{
            false
          }
        }

      }
    }else{    // black pawn
      if(board.state(index(2))(index(3)) !="." && board.state(index(2))(index(3))!= "-") {  // eat
        if ((index(2)-index(0)==1 ) && (Math.abs(index(3)-index(1))==1) ){
          true
        }else{
          false
        }
      }else{    // move
        if(index(0) == 1){
          if((index(3)==index(1)) && ((index(2)-index(0))<=2)){
            true
          }else{
            false
          }
        }else{
          if((index(3)==index(1)) && (index(2)-index(0)==1)){
            true
          }else{
            false
          }
        }

      }

    }
  }
  // validate that it's my piece and going to an enemy piece or empty place
  def generalValidation(index : Array[Int] , turn : Int , board: State) : Boolean ={
    if(index(0) == index(2) && index(1) == index(3)){
      false
    }else
    if(turn==0){
      if(board.state(index(0))(index(1)) >"a" && board.state(index(0))(index(1))<"z"
        && ((board.state(index(2))(index(3))>"A"  && board.state(index(2))(index(3))<"Z") ||
              board.state(index(2))(index(3)) =="-" ||  board.state(index(2))(index(3)) ==".") ){
        println("trueeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee");
        true
      }else
        {
          println("falseeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee");
          false
        }

    }else{
      if(board.state(index(0))(index(1))>"A" && board.state(index(0))(index(1))<"Z"
        && ((board.state(index(2))(index(3))>"a"  && board.state(index(2))(index(3)) <"z") ||
        board.state(index(2))(index(3))=="-" ||  board.state(index(2))(index(3))==".") ){
        true
      }else {
        println("falseeeeeeeeeee222222222222222222")
        false
      }
    }
  }

  def moveValidation (index : Array[Int] , turn : Int ,board :State ) : Boolean ={
    println(board.state(index(0))(index(1)))
    if(board.state(index(0))(index(1)) == "P" || board.state(index(0))(index(1)) == "p"){
      validPawnMove(index,turn,board)
    }else if(board.state(index(0))(index(1)) == "R" || board.state(index(0))(index(1)) == "r"){
      validRookMove(index,board)
    }else if(board.state(index(0))(index(1)) == "K" || board.state(index(0))(index(1)) == "k"){
      validKingMove(index)
    }else if(board.state(index(0))(index(1)) == "N" || board.state(index(0))(index(1)) == "n"){
      validKnightMove(index)
    }else if(board.state(index(0))(index(1)) == "Q" || board.state(index(0))(index(1)) == "q"){
      validQueenMove(index,board)
    }else if(board.state(index(0))(index(1)) == "B" || board.state(index(0))(index(1)) == "b"){
      validBishopMove(index,board)
    }else{
      false
    }

  }

  def moveGenerate (index : Array[Int] , state : State) : State = {
    println("movveeeee")
    state.state(index(2))(index(3)) = state.state(index(0))(index(1))

    if((index(0) + index(1)) % 2 ==0){
      state.state(index(0)) (index(1)) = "-"
    }else{
      state.state(index(0)) (index(1)) = "."
    }
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

    if(xfrom.<('A') || xfrom.>('H') || xto.<('A') || xto.>('H') || yfrom.<(1) || yfrom.>(8) || yto.<(1) || yto.>(8) ) {
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
      case (8) =>  0
      case (7) =>  1
      case (6) =>  2
      case (5) =>  3
      case (4) =>  4
      case (3) =>  5
      case (2) =>  6
      case (1) =>  7
    }

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

       println(generalValidation(index,turn,state))
       println(moveValidation(index,turn,state))
       if(generalValidation(index,turn,state) && moveValidation(index,turn,state)){
         println("validatiiiion done")
         moveGenerate(index,state)
         state.flag = true
       }else{
         println("not valide")
         state.flag = false
       }


     }else{
       state.flag = false
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

    while(true ) {
      if(state.flag){
        turn = turn ^ 1
      }
      println(turn)
      state.printState()
      println("Please enter -from- and -to- pos: ");
      val move = scala.io.StdIn.readLine()
      chess.controller(move, state, turn)
    }
  }
}
