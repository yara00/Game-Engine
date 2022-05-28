import definitions.state

class Connect4Controller {
  var rowForCol1 = 5
  var rowForCol2 = 5
  var rowForCol3 = 5
  var rowForCol4 = 5
  var rowForCol5 = 5
  var rowForCol6 = 5
  var rowForCol7 = 5

  def intialstate () : state = Array(
    Array(".", ".", ".",".",".",".","."),
    Array(".", ".", ".",".",".",".","."),
    Array(".", ".", ".",".",".",".","."),
    Array(".", ".", ".",".",".",".","."),
    Array(".", ".", ".",".",".",".","."),
    Array(".", ".", ".",".",".",".","."),
 //   Array(".", ".", ".",".",".",".","."),
  )

  def printState(state: state): Unit = {

    println()
    for(i <- 0 until state.length) {
      for(j <- 0 until state(i).length) {
        print(state(i)(j) + " ")
      }
      println()
    }
  }


  def getColumn (col: Int) = col -1

  def controller (move  : String , state: state , turn : Int): Boolean ={

    var flag :Boolean=false
    var column : Int = 0


    if(move.length == 1){
      val col = move.toInt
      println("Column >>" + col)
      if(col >=1 && col<=7){          // valid input
          column = col - 1
          println(" actual column >> " + column)
          if(column == 0 && rowForCol1>=0){
            if(turn ==0){
              state(rowForCol1)(column) = "x"
            }else{
              state(rowForCol1)(column) = "y"
            }
            rowForCol1 = rowForCol1 -1
            true
          }else if (column == 1 && rowForCol2 >=0){
            if(turn ==0){
              state(rowForCol2)(column) = "x"
            }else{
              state(rowForCol2)(column) = "y"
            }
            rowForCol2 = rowForCol2 -1
            true
          }else if (column == 2 && rowForCol3 >=0) {
            if (turn == 0) {
              state(rowForCol3)(column) = "x"
            } else {
              state(rowForCol3)(column)= "y"
            }
            rowForCol3 = rowForCol3 - 1
            true
          }else if (column == 3 && rowForCol4 >=0) {
            if (turn == 0) {
              state(rowForCol4)(column) = "x"
            } else {
              state(rowForCol4)(column) = "y"
            }
            rowForCol4 = rowForCol4 - 1
            true
          }else if (column == 4 && rowForCol5 >=0) {
            if (turn == 0) {
              state(rowForCol5)(column) = "x"
            } else {
              state(rowForCol5)(column) = "y"
            }
            rowForCol5 = rowForCol5 - 1
            true
          }else if (column == 5 && rowForCol6 >=0) {
            if (turn == 0) {
              state(rowForCol6)(column) = "x"
            } else {
              state(rowForCol6)(column) = "y"
            }
            rowForCol6 = rowForCol6 - 1
            true
          }else if (column == 6 && rowForCol7 >=0) {
            if (turn == 0) {
              state(rowForCol7)(column) = "x"
            } else {
              state(rowForCol7)(column) = "y"
            }
            rowForCol7 = rowForCol7 - 1
            true
          }else{
            false
          }

      }else{
        flag = false
        flag
      }
    }else{
      flag = false
      flag
    }




  }




}
object MainConnect4 {
  def main(args: Array[String]): Unit={
    var connect: Connect4Controller = new Connect4Controller
    var turn = 1;
    var board = connect.intialstate()
    var flag = true

    while (true) {
      if(flag){
        turn = turn ^ 1
      }

      connect.printState(board)
      println("Please enter -from- and -to- pos: ");
      val move = scala.io.StdIn.readLine()
      flag = connect.controller(move, board, turn)
    }
  }

}
