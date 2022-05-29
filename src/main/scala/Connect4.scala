import Engine.status.{Invalid, status}
import Engine.{click_to_move, controller, drawer, input, state, status, turn}
import javafx.scene.shape.StrokeLineJoin
import scalafx.scene.Node
import scalafx.scene.paint.Color.{Black, Blue, Brown, Burlywood, CadetBlue, Chocolate, DarkBlue, DarkCyan, DarkKhaki, DarkMagenta, DarkOliveGreen, DarkSalmon, DarkViolet, Goldenrod, Green, IndianRed, LavenderBlush, LightBlue, LightCyan, LightPink, LimeGreen, Magenta, MediumBlue, OrangeRed, Pink, PowderBlue, Red, White}
import scalafx.scene.paint.{Color, LinearGradient}
import scalafx.scene.shape.{Circle, Rectangle}
import scalafx.scene.shape.Shape.sfxShape2jfx
import scalafx.scene.text.Text
import scalafx.scene.text.Text.sfxText2jfx

import scala.util.control.Breaks.break

object Connect4 {
  def connect4_initial = Array.fill[String](6,7)(".")
  val connect4_BOARDWIDTH = 600
  val connect4_click_handler:click_to_move =(x:Double,y:Double)=>{
    s"${Math.floor(x/(connect4_BOARDWIDTH/7)).toInt}"
  }
  def connect4_drawer:drawer=(x:state)=>{
    val CELLWIDTH = connect4_BOARDWIDTH / 7
    val CELLHEIGHT = connect4_BOARDWIDTH / 6
    val gap = CELLWIDTH/5
    val CIRCLERADIUS = CELLWIDTH/2 - gap/2
    var lst: List[Node]= List()
    var t_lst:List[Node] = List()
    for{
      i <- 0 until 6
      j <- 0 until 7
    }{
      val r = new Rectangle()
      r.layoutX = j * CELLWIDTH
      r.layoutY = i * CELLHEIGHT
      r.width = CELLWIDTH
      r.height = CELLHEIGHT
      r.fill = Color(0.3,0.3,0.3,1)
      val center_y = (i+0.5)*CELLHEIGHT
      val center_x = (j+0.5)*CELLWIDTH
      val c = new Circle(){
        this.centerX = center_x
        this.centerY = center_y
        this.fill = if(x(i)(j)=="x") CadetBlue else if(x(i)(j)=="y") Chocolate else White
        this.radius = CIRCLERADIUS
        this.stroke = Color(0.2,0.2,0.2,1)
        this.strokeWidth = 4
      }
      sfxShape2jfx(r).setSmooth(true)
      lst = lst .:: (r)
      t_lst = t_lst .:: (c)
    }
    lst=lst.appendedAll(t_lst)
    lst
  }
  def getStatus(x:state,turn:turn): Unit ={
    val p = if (turn==0) "x" else "y"

  }
  def connect4_controller:controller= ( state: state , move:input, turn : Int)=>{
    var flag :Boolean=false
    var returned:(state,status,String)=null
    var col = 0
    println(move)
    if(move.length == 1){
      try {
        col = move.toInt
      }
      catch {
        case e:Exception => (state,status.Invalid,status.Invalid.toString)
      }
      if(col >= 0 && col<= 6){// valid input
        var i=5
        while(!flag && i > -1){
          if(state(i)(col)=="."){
            state(i)(col)= if(turn==0)"x" else "y"
            val s =if(turn==0) status.Player_1_turn else status.Player_0_turn
            returned = (state,s,s.toString);
            flag = true
          }
          i-=1;
        }
      }
    }
    if(!flag){
      (state,status.Invalid,"Invalid")
    }
    else {
      returned
    }
  }
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
  }




