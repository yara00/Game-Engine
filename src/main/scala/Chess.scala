import definitions.{click_to_move, controller, drawer, input, state, status, turn}
import scalafx.application.JFXApp3
import scalafx.geometry.Insets
import scalafx.scene.text._
import scalafx.scene.{Node, Scene}
import scalafx.scene.effect.DropShadow
import scalafx.scene.image.{Image, ImageView}
import scalafx.scene.input.{DragEvent, MouseEvent}
import scalafx.scene.layout.{GridPane, HBox}
import scalafx.scene.paint.Color._
import scalafx.scene.paint._
import scalafx.scene.shape.Rectangle

import scala.::

import scala.jdk.CollectionConverters._
import scala.language.postfixOps

object Chess{
  def chess_BOARDWIDTH = 560
  def chess_initial () : state = Array(
    Array("R", "N", "B","Q","K","B","N","R"),
    Array("P", "P", "P","P","P", "P", "P","P"),
    Array("-",".","-",".","-",".","-","."),
    Array(".","-",".","-",".","-",".","-"),
    Array("-",".","-",".","-",".","-","."),
    Array(".","-",".","-",".","-",".","-"),
    Array("p", "p", "p","p","p", "p", "p","p"),
    Array("r", "n", "b","q","k","b","n","r"),
  )
  def chess_click_handler:click_to_move =(x:Double,y:Double)=>{
    val X = ('A'+(x/(chess_BOARDWIDTH/8)).toInt).toChar
    val Y = 8 - (y/(chess_BOARDWIDTH/8)).toInt
    s"${X.toString}${Y}"
  }
  def promotedpiece (turn : Int , piece : String) : Boolean = {
    println("Your pawn can be promoted choose a piece")

    println(piece)
    println(turn)
    if(turn == 0){
      if(piece!="r" && piece!="q" && piece!="n" && piece!="b"){
        println("not valid piece")
        false
      }else{
        true
      }
    }else{
      if(piece!="R" && piece!="Q" && piece!="N" && piece!="B"){
        println("not valid piece")
        false
      }else{
       true
      }
    }
  }

  def canPrometed (turn : Int , board : state) : Boolean = {
   var found : Boolean = false
    if(turn == 0){        // white
      for( i<-0 until 8){
        if(board(0)(i) == "p"){
          found = true
        }
      }
      found
    }else{
      for( i<-0 until 8){
        if(board(7)(i) == "P"){
          found = true
        }
      }
      found
    }
  }

  def Promotion (turn : Int , board : state , piece : String) : state = {
    println(" func promotion >>>")
    var y_pos = -1

    if(turn == 0){      // white turn
      for(i <- 0 until 8) {
        if (board(0)(i) == "p") {
          y_pos = i
        }
      }
      println(y_pos)
      if(y_pos != -1) {
       // val piece = promotedpiece(turn)
        println("your piece is >> " + piece)
        board(0)(y_pos) = piece
      }
      board
    }else{        // black turn
      for(i <- 0 until 8) {
        if (board(7)(i) == "P") {
          y_pos = i

        }
      }

      println(y_pos)
      if(y_pos != -1){
       // val piece = promotedpiece(turn)
        println("your piece is >> " + piece)
        board(7)(y_pos) = piece
      }

      board

    }
  }

  def validQueenMove (index : Array[Int],state: state) : Boolean ={
    validRookMove(index,state) || validBishopMove(index,state)
  }
  def validRookMove (index : Array[Int],state: state) : Boolean = {
    if((index(0)==index(2) && index(1)!=index(3)) || (index(0)!=index(2) && index(1)==index(3))){
      clearRookWay(index,state)
    }else {
      false
    }
  }

  def clearRookWay(index:Array[Int],state: state):Boolean = {
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



  def validBishopMove(index:Array[Int],state: state): Boolean ={
    if(Math.abs(index(3)-index(1)) == Math.abs(index(2)-index(0))) {
      clearBishopWay(index,state)
    } else false
  }

  def clearBishopWay(index:Array[Int],state: state):Boolean = {
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
  def hasObsticale(x:Int,y:Int,state: state):Boolean = state(y)(x) != "-" && state(y)(x) != "."
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
  def validPawnMove(index : Array[Int] , turn : Int, board : state) : Boolean ={
    if(turn == 0){      // white pawn

      if(board(index(2))(index(3)) != "." && board(index(2))(index(3)) !="-") {  // eat
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
      if(board(index(2))(index(3)) !="." && board(index(2))(index(3))!= "-") {  // eat
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
  def generalValidation(index : Array[Int] , turn : Int , board: state) : Boolean ={
    if(index(0) == index(2) && index(1) == index(3)){
      false
    }else
      if(turn==0){
        if(board(index(0))(index(1)) >"a" && board(index(0))(index(1))<"z"
          && ((board(index(2))(index(3))>"A"  && board(index(2))(index(3))<"Z") ||
          board(index(2))(index(3)) =="-" ||  board(index(2))(index(3)) ==".") ){
          println("trueeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee");
          true
        }else
        {
          println("falseeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee");
          false
        }

      }else{
        if(board(index(0))(index(1))>"A" && board(index(0))(index(1))<"Z"
          && ((board(index(2))(index(3))>"a"  && board(index(2))(index(3)) <"z") ||
          board(index(2))(index(3))=="-" ||  board(index(2))(index(3))==".") ){
          true
        }else {
          println("falseeeeeeeeeee222222222222222222")
          false
        }
      }
  }
  def moveValidation (index : Array[Int] , turn : Int ,board :state ) : Boolean ={
    println(board(index(0))(index(1)))
    if(board(index(0))(index(1)) == "P" || board(index(0))(index(1)) == "p"){
      validPawnMove(index,turn,board)
    }else if(board(index(0))(index(1)) == "R" || board(index(0))(index(1)) == "r"){
      validRookMove(index,board)
    }else if(board(index(0))(index(1)) == "K" || board(index(0))(index(1)) == "k"){
      validKingMove(index)
    }else if(board(index(0))(index(1)) == "N" || board(index(0))(index(1)) == "n"){
      validKnightMove(index)
    }else if(board(index(0))(index(1)) == "Q" || board(index(0))(index(1)) == "q"){
      validQueenMove(index,board)
    }else if(board(index(0))(index(1)) == "B" || board(index(0))(index(1)) == "b"){
      validBishopMove(index,board)
    }else{
      false
    }

  }
  def moveGenerate (index : Array[Int] , state : state) : state = {
    println("movveeeee")
    state(index(2))(index(3)) = state(index(0))(index(1))

    if((index(0) + index(1)) % 2 ==0){
      state(index(0)) (index(1)) = "-"
    }else{
      state(index(0)) (index(1)) = "."
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
      case (8) => return 0
      case (7) => return 1
      case (6) => return 2
      case (5) => return 3
      case (4) => return 4
      case (3) => return 5
      case (2) => return 6
      case (1) => return 7
    }

  }

  def chess_drawer:drawer =  (x:state)=>{
    var lst: List[Node]= List()
    lst = draw_board()
    for (row <- Range(0, 8)) {
      for (col <- Range(0, 8)) {
        if(x(row)(col)!="." && x(row)(col)!="-" )
        lst = lst.appended(draw_piece(((8*col)+row),x(row)(col)))
      }
    }
    lst
  }

  def draw_wk(n: Int): ImageView = {
    val WK = new Image("file:assets/wk.png", 70, 70, false, false)
    val wk = new ImageView(WK)
    wk.setX(board(n).x.value)
    wk.setY(board(n).y.value)
    wk
  }
  def draw_wq(n: Int): ImageView = {
    val WQ = new Image("file:assets/wq.png", 60, 60, true, false)
    val wq = new ImageView(WQ)
    wq.setX(board(n).x.value+5)
    wq.setY(board(n).y.value+5)
    wq
  }
  def draw_wr(n: Int): ImageView = {
    val WR = new Image("file:assets/wr.png", 60, 60, false, false)
    val wr = new ImageView(WR)
    wr.setX(board(n).x.value+5)
    wr.setY(board(n).y.value+5)
    wr
  }
  def draw_wn(n: Int): ImageView = {
    val WKn = new Image("file:assets/wkn.png", 60, 60, false, false)
    val wkn = new ImageView(WKn)
    wkn.setX(board(n).x.value+5)
    wkn.setY(board(n).y.value+5)
    wkn
  }
  def draw_wb(n: Int): ImageView = {
    val WB = new Image("file:assets/wb.png", 70, 70, true, false)
    val wb = new ImageView(WB)
    wb.setX(board(n).x.value)
    wb.setY(board(n).y.value)
    wb
  }
  def draw_wp(n: Int): ImageView = {
    val WP = new Image("file:assets/wp.png", 50, 50, true, false)
    val wp = new ImageView(WP)
    wp.setX(board(n).x.value+13)
    wp.setY(board(n).y.value+10)
    wp
  }
  ///
  def draw_bk(n: Int): ImageView = {
    val BK = new Image("file:assets/bk.png",  55, 55, true, false)
    val bk = new ImageView(BK)
    bk.setX(board(n).x.value+5)
    bk.setY(board(n).y.value+5)
    bk
  }
  def draw_bq(n: Int): ImageView = {
    val BQ = new Image("file:assets/bq.png",  58, 58, false, false)
    val bq = new ImageView(BQ)
    bq.setX(board(n).x.value+5)
    bq.setY(board(n).y.value+5)
    bq
  }
  def draw_br(n: Int): ImageView = {
    val BR = new Image("file:assets/br.png",  60, 60, false, false)
    val br = new ImageView(BR)
    br.setX(board(n).x.value+5)
    br.setY(board(n).y.value+5)
    br
  }
  def draw_bn(n: Int): ImageView = {
    val BKn = new Image("file:assets/bkn.png", 60, 60, false, false)
    val bkn = new ImageView(BKn)
    bkn.setX(board(n).x.value+5)
    bkn.setY(board(n).y.value+5)
    bkn
  }
  def draw_bb(n: Int): ImageView = {
    val BB = new Image("file:assets/bb.png",  50, 50, true, false)
    val bb = new ImageView(BB)
    bb.setX(board(n).x.value+9)
    bb.setY(board(n).y.value+9)
    bb
  }
  def draw_bp(n: Int): ImageView = {
    val BP = new Image("file:assets/bp.png",  60, 60, true, false)
    val bp = new ImageView(BP)
    bp.setX(board(n).x.value+5)
    bp.setY(board(n).y.value+5)
    bp
  }
  def draw_piece(n:Int, s:String) : ImageView ={
    s match {
      case "k"  => draw_wk(n)
      case "q"  => draw_wq(n)
      case "r"  => draw_wr(n)
      case "n"  => draw_wn(n)
      case "b"  => draw_wb(n)
      case "p"  => draw_wp(n)
      case "K"  => draw_bk(n)
      case "Q"  => draw_bq(n)
      case "R"  => draw_br(n)
      case "N"  => draw_bn(n)
      case "B"  => draw_bb(n)
      case "P"  => draw_bp(n)
    }

  }
  def chess_controller:controller = ( state: state,move: String, turn: Int)=> {

    var index= new Array[Int](4);
    println(move)
    var flag :Boolean=false;
    var promotion :Boolean = false;

    println(canPrometed(turn,state))
    println(promotedpiece(turn, move))

    if(canPrometed(turn, state) && promotedpiece(turn, move)){
      println("Promation validation")
      Promotion(turn,state,move)
      flag = true
      promotion = true

//      (state,if( flag ) if(turn%2==0)status.Player_0_turn else status.Player_1_turn else status.Invalid,
//        if(!flag && !promotion) "Invalid move" else if (promotion) "Promotion" else "")
    }

//    if(flag && promotion){
//      println("vvvvvvvvvvvv")
//      (state, if(turn%2==0)status.Player_1_turn else status.Player_0_turn ,"After Promotion" )
//    }

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
        flag = true

        if(canPrometed(turn , state)) {
          promotion = true;
          flag = false
          (state,if(flag ) if(turn%2==0)status.Player_1_turn else status.Player_0_turn else status.Invalid,
            if(!flag && !promotion) "Invalid move" else if (promotion) "Promotion" else "")
        }


      }else{
        println("not valid")
        flag = false
      }


    }else{
      if(promotion){
        flag = true
      }else{
        flag = false
      }

    }

    (state,if( flag ) if(turn%2==0)status.Player_0_turn else status.Player_1_turn else status.Invalid,
            if(!flag && !promotion) "Invalid move" else if (promotion && !flag) "Promotion" else "")

  }

  /*
  val WK = new Image("file:assets/wk.png", 70, 70, false, false)
  val WQ = new Image("file:assets/wq.png", 60, 60, true, false)
  val WB = new Image("file:assets/wb.png", 70, 70, true, false)
  val WKn = new Image("file:assets/wkn.png", 60, 60, false, false)
  val WR = new Image("file:assets/wr.png", 60, 60, false, false)
  val WP = new Image("file:assets/wp.png", 50, 50, true, false)
  //  val WP3 = new Image("file:assets/wq.png",70,70,false,false)
  //  val WP4 = new Image("file:assets/wq.png",70,70,false,false)
  //  val WP5 = new Image("file:assets/wq.png",70,70,false,false)
  //  val WP6 = new Image("file:assets/wq.png",70,70,false,false)
  //  val WP7 = new Image("file:assets/wq.png",70,70,false,false)
  //  val WP8 = new Image("file:assets/wq.png",70,70,false,false)


  val BQ = new Image("file:assets/bq.png",  58, 58, false, false)
  val BK = new Image("file:assets/bk.png",  55, 55, true, false)
  val BB = new Image("file:assets/bb.png",  50, 50, true, false)
  val BKn = new Image("file:assets/bkn.png", 60, 60, false, false)
  val BR = new Image("file:assets/br.png",  60, 60, false, false)
  val BP = new Image("file:assets/bp.png",  60, 60, true, false)
  */

  var texts :List[Text]= List()
  var board: List[Rectangle] = List()
  var pieces: List[ImageView] = List()

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
  def letters(n:Int): String ={
    n match{
      case 1  => return "A"
      case 2  =>return "B"
      case 3  =>return "C"
      case 4  =>return "D"
      case 5  =>return "E"
      case 6  =>return "F"
      case 7  =>return "G"
      case 8  =>return "H"
    }
  }
 /* val board_Map = Map("a8" -> 0 , "a7" -> 1 , "a6" -> 2, "a5" -> 3, "a4" -> 4, "a3" -> 5, "a2" -> 6, "a1" -> 7,
    "b8" -> 8, "b7" -> 9, "b6" -> 10, "b5" -> 11    , "b4" -> 12  , "b3" -> 13  , "b2" -> 14  , "b1" -> 15,
    "c8" -> 16, "c7" -> 17, "c6" -> 18, "c5" -> 19    , "c4" -> 20  , "c3" -> 21  , "c2" -> 22  , "c1" -> 23,
    "d8" -> 24, "d7" -> 25, "d6" -> 26, "d5" -> 27    , "d4" -> 28  , "d3" -> 29  , "d2" -> 30  , "d1" -> 31,
    "e8" -> 32, "e7" -> 33, "e6" -> 34, "e5" -> 35   , "e4" -> 36  , "e3" -> 37  , "e2" -> 38  , "e1" -> 39,
    "f8" -> 40, "f7" -> 41, "f6" -> 42, "f5" -> 43    , "f4" -> 44  , "f3" -> 45 , "f2" -> 46  , "f1" -> 47,
    "g8" -> 48, "g7" -> 49, "g6" -> 50, "g5" -> 51    , "g4" -> 52  , "g3" -> 53  , "g2" -> 54  , "g1" -> 55,
    "h8" -> 56, "h7" -> 57, "h6" -> 58, "h5" -> 59    , "h4" -> 60  , "h3" ->61   , "h2" -> 62  , "h1" -> 63,
  )
  def WhiteQ(): List[ImageView] = {
    var pieces: List[ImageView] = List()


    val wk = new ImageView(WK)
    val wq = new ImageView(WQ)
    val wb1 = new ImageView(WB)
    val wb2 = new ImageView(WB)
    val wr1 =new ImageView(WR)
    val wr2 =new ImageView(WR)
    val wkn1 = new ImageView(WKn)
    val wkn2 = new ImageView(WKn)
    val wp1 = new ImageView(WP)
    val wp2 = new ImageView(WP)
    val wp3 = new ImageView(WP)
    val wp4 = new ImageView(WP)
    val wp5 = new ImageView(WP)
    val wp6 = new ImageView(WP)
    val wp7 = new ImageView(WP)
    val wp8 = new ImageView(WP)

   /////////////////
    val bk = new ImageView(BK)
    val bq = new ImageView(BQ)
    val bb1 = new ImageView(BB)
    val bb2 = new ImageView(BB)
    val br1 =new ImageView(BR)
    val br2 =new ImageView(BR)
    val bkn1 = new ImageView(BKn)
    val bkn2 = new ImageView(BKn)
    val bp1 = new ImageView(BP)
    val bp2 = new ImageView(BP)
    val bp3 = new ImageView(BP)
    val bp4 = new ImageView(BP)
    val bp5 = new ImageView(BP)
    val bp6 = new ImageView(BP)
    val bp7 = new ImageView(BP)
    val bp8 = new ImageView(BP)
    val imageView4 = new ImageView(WKn)
    val imageView5 = new ImageView(WR)
    wk.setX(board(board_Map("e1")).x.value)
    wk.setY(board(board_Map("e1")).y.value)
    wq.setX(board(board_Map("d1")).x.value+5)
    wq.setY(board(board_Map("d1")).y.value+5)
    wb1.setX(board(board_Map("c1")).x.value)
    wb1.setY(board(board_Map("c1")).y.value)
    wb2.setX(board(board_Map("f1")).x.value)
    wb2.setY(board(board_Map("f1")).y.value)
    wr1.setX(board(board_Map("a1")).x.value+5)
    wr1.setY(board(board_Map("a1")).y.value+5)
    wr2.setX(board(board_Map("h1")).x.value+5)
    wr2.setY(board(board_Map("h1")).y.value+5)
    wkn1.setX(board(board_Map("b1")).x.value+5)
    wkn1.setY(board(board_Map("b1")).y.value+5)
    wkn2.setX(board(board_Map("g1")).x.value+5)
    wkn2.setY(board(board_Map("g1")).y.value+5)
    wp1.setX(board(board_Map("a2")).x.value + 13)
    wp1.setY(board(board_Map("a2")).y.value + 10)
    wp2.setX(board(board_Map("b2")).x.value + 13)
    wp2.setY(board(board_Map("b2")).y.value + 10)
    wp3.setX(board(board_Map("c2")).x.value + 13)
    wp3.setY(board(board_Map("c2")).y.value + 10)
    wp4.setX(board(board_Map("d2")).x.value + 13)
    wp4.setY(board(board_Map("d2")).y.value + 10)
    wp5.setX(board(board_Map("e2")).x.value + 13)
    wp5.setY(board(board_Map("e2")).y.value + 10)
    wp6.setX(board(board_Map("f2")).x.value + 13)
    wp6.setY(board(board_Map("f2")).y.value + 10)
    wp7.setX(board(board_Map("g2")).x.value + 13)
    wp7.setY(board(board_Map("g2")).y.value + 10)
    wp8.setX(board(board_Map("h2")).x.value + 13)
    wp8.setY(board(board_Map("h2")).y.value + 10)
    bk.setX(board(board_Map("e8")).x.value+5)
    bk.setY(board(board_Map("e8")).y.value+5)
    bq.setX(board(board_Map("d8")).x.value+5)
    bq.setY(board(board_Map("d8")).y.value+5)
    bb1.setX(board(board_Map("c8")).x.value+9)
    bb1.setY(board(board_Map("c8")).y.value+9)
    bb2.setX(board(board_Map("f8")).x.value+9)
    bb2.setY(board(board_Map("f8")).y.value+9)
    br1.setX(board(board_Map("a8")).x.value+5)
    br1.setY(board(board_Map("a8")).y.value+5)
    br2.setX(board(board_Map("h8")).x.value+5)
    br2.setY(board(board_Map("h8")).y.value+5)
    bkn1.setX(board(board_Map("b8")).x.value+5)
    bkn1.setY(board(board_Map("b8")).y.value+5)
    bkn2.setX(board(board_Map("g8")).x.value+5)
    bkn2.setY(board(board_Map("g8")).y.value+5)
    bp1.setX(board(board_Map("a7")).x.value + 5)
    bp1.setY(board(board_Map("a7")).y.value + 5)
    bp2.setX(board(board_Map("b7")).x.value + 5)
    bp2.setY(board(board_Map("b7")).y.value + 5)
    bp3.setX(board(board_Map("c7")).x.value + 5)
    bp3.setY(board(board_Map("c7")).y.value + 5)
    bp4.setX(board(board_Map("d7")).x.value + 5)
    bp4.setY(board(board_Map("d7")).y.value + 5)
    bp5.setX(board(board_Map("e7")).x.value + 5)
    bp5.setY(board(board_Map("e7")).y.value + 5)
    bp6.setX(board(board_Map("f7")).x.value + 5)
    bp6.setY(board(board_Map("f7")).y.value + 5)
    bp7.setX(board(board_Map("g7")).x.value + 5)
    bp7.setY(board(board_Map("g7")).y.value + 5)
    bp8.setX(board(board_Map("h7")).x.value + 5)
    bp8.setY(board(board_Map("h7")).y.value + 5)
    pieces = pieces.appended(wk) ::: pieces.appended(wq) ::: pieces.appended(wp1) ::: pieces.appended(wp2) ::: pieces.appended(wp3) :::
      pieces.appended(wp4) ::: pieces.appended(wp5) ::: pieces.appended(wp6) ::: pieces.appended(wp7) ::: pieces.appended(wp8) ::: pieces.appended(bp1) ::: pieces.appended(bp2) ::: pieces.appended(bp3) :::
      pieces.appended(bp4) ::: pieces.appended(bp5) ::: pieces.appended(bp6) ::: pieces.appended(bp7) ::: pieces.appended(bp8) ::: pieces.appended(wb1) ::: pieces.appended(wb2)  :::
          pieces.appended(wr2)  :::pieces.appended(wr1) :::pieces.appended(wkn1):::pieces.appended(wkn2) ::: pieces.appended(bk) ::: pieces.appended(bq)::: pieces.appended(bb1) ::: pieces.appended(bb2)  :::
      pieces.appended(br2)  :::pieces.appended(br1) :::pieces.appended(bkn1):::pieces.appended(bkn2)
    pieces
  }*/
}