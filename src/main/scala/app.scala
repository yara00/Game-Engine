import app.BOARD_WIDTH
import scalafx.application.JFXApp3
import scalafx.geometry.Insets
import scalafx.scene.{Node, Scene}
import scalafx.scene.effect.DropShadow
import scalafx.scene.input.{DragEvent, MouseEvent}
import scalafx.scene.layout.{GridPane, HBox}
import scalafx.scene.paint.Color._
import scalafx.scene.paint._
import scalafx.scene.shape.Rectangle
import scalafx.scene.text.Text
import status.status

import scala.::
import scala.language.postfixOps

object status extends Enumeration{
  type status = Value
  //for move:
  val Valid = Value("Valid")
  val Invalid = Value("Invalid")
  //for game and turns:
  //running
  val Player_0_turn = Value("First Player's turn")
  val Player_1_turn = Value("Second Player's turn")
  //ended
  val Player_0_won = Value("First Player Won")
  val Player_1_won = Value("Second Player Won")
  val Draw = Value("Draw")

}
object app extends JFXApp3 {
  def BOARD_WIDTH:Int = 600;
  type input = String
  type turn = Int
  type state = List[List[Char]]
  type drawable = List[Node]
  type drawer = (state)=>drawable
  type controller = (state,input,turn)=>(state,status,String)
  type clickHandler = (MouseEvent)=>input

  val xo_initial:state = List(List[Char]('x','o','x'),List[Char]('o','x','x'),List[Char]('x','x','o'))
  val xo_controller:controller=(x:state,in:input,turn:turn)=>{
    val getSymbol = (turn:turn)=>{
      turn match {
        case 0 => 'x'
        case _ => 'o'
      }
    }
    val getStatus:(state,turn)=>status = (s:state,p:turn)=>{
      var rowcount=Array.fill[Int](3)(0);
      var colcount=Array.fill[Int](3)(0);new Array[Int](3);
      var full = true;
      for(row<-Range(0,3)){
        for(col<-Range(0,3)){
          if(s(row)(col)==' '){
                full=false;
          }
          if(s(row)(col)==getSymbol(p))
            rowcount(row)=rowcount(row)+1;
          colcount(col)=colcount(col)+1;
        }
      }
      if(rowcount.contains(3)||colcount.contains(3)) {
        turn match {
          case 0 => (status.Player_0_won)
          case _ => (status.Player_1_won)
        }
      }
      else if(full){
        status.Draw
      }
      else {
        turn match {
          case 0 => (status.Player_1_turn)
          case 1 => (status.Player_0_turn)
        }
      }
    }
    val sel = in.toInt - 1;
    val row = sel/3;
    val col = sel%3;
    val player = getSymbol(turn);
    var returned = (null,
      if(turn%2==0) status.Player_0_turn else status.Player_1_turn,
      "invalid square");
    if((x(row)(col))!=player){
      returned;
    }
    else{
      x(row).updated(col,player)
      (x,getStatus(x,player),"Valid")
    }

  }
  val xo_drawer:drawer = (x:state)=> {
    //  for(i<-Range(0,x.size)){
    //    for(j<-Range(0,x(0).size)){
    //      //...
    //    }
    //  }
    val CELLWIDTH = BOARD_WIDTH / 3;
    var lst: List[Node]= List()
    var t_lst:List[Node] = List()
    for {
      i <- List(0, 1, 2)
      j <- List(0, 1, 2)
    } {
      val r = new Rectangle()
      r.layoutX = j * CELLWIDTH
      r.layoutY = i * CELLWIDTH
      r.width = CELLWIDTH
      r.height = CELLWIDTH
      r.fill = White;
      r.stroke = Black;
      val text= if (x(i)(j)=='x') "X" else if (x(i)(j)=='o') "O" else "";
      val center_y = (j+0.5)*CELLWIDTH
      var center_x = (i+0.5)*CELLWIDTH
      if(x(i)(j)=='o')
        center_x=center_x- CELLWIDTH/40;
      val gap = CELLWIDTH/5;
      val t = new Text(x = center_x-CELLWIDTH/2+gap/2, y = center_y+CELLWIDTH/2-gap/2, t = text);

      t.fill = if (x(i)(j)=='x') Red else if (x(i)(j)=='o') Blue else White;
      t.stroke = Black;
      t.style =  s"-fx-font:normal ${CELLWIDTH-gap}pt ariel"
//      val t = new Text {
//        text= if (x(i)(j)=='X') "X" else if (x(i)(j)=='o') "O" else "";
//        style = s"-fx-font:normal bold ${CELLWIDTH}px sans-serif"
//        fill=
//      }
      lst = lst .:: (r)
      t_lst = t_lst .:: (t)
    }
    lst=lst.appendedAll(t_lst)
    lst
  }

  val makeRed = (x:AnyRef)=>{
    val y= x.asInstanceOf[Rectangle]
    y.fill = Red
  }
  override def start(): Unit = {
    stage = new JFXApp3.PrimaryStage {
      title = "ScalaFX Hello World"
      scene = new Scene(600,600) {
        content = (xo_drawer(xo_initial));
      }
    }
  }
}