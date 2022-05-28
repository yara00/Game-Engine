import definitions.{click_to_move, controller, drawer, state}
import javafx.scene.shape.StrokeLineJoin
import scalafx.scene.Node
import scalafx.scene.paint.Color.{Black, Blue, DarkBlue, DarkCyan, DarkMagenta, DarkViolet, LightPink, Red, White}
import scalafx.scene.paint.{Color, LinearGradient}
import scalafx.scene.shape.{Circle, Rectangle}
import scalafx.scene.shape.Shape.sfxShape2jfx
import scalafx.scene.text.Text
import scalafx.scene.text.Text.sfxText2jfx

object Connect4 {
  def connect4_initial = Array.fill[String](6,7)(".")
  val connect4_BOARDWIDTH = 600.0
  val connect4_click_handler:click_to_move =(x:Double,y:Double)=>{
    s"${Math.floor(x/(connect4_BOARDWIDTH/7))})"
  }
  val connect4_drawer:drawer=(x:state)=>{
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
      r.fill = Color.DarkCyan
      val center_y = (i+0.5)*CELLHEIGHT
      val center_x = (j+0.5)*CELLWIDTH
      val c = new Circle(){
        this.centerX = center_x
        this.centerY = center_y
        this.fill = if(x(i)(j)=="x") DarkMagenta else if(x(i)(j)=="y") LightPink else White
        this.radius = CIRCLERADIUS
      }
      sfxShape2jfx(r).setSmooth(true)
      lst = lst .:: (r)
      t_lst = t_lst .:: (c)
    }
    lst=lst.appendedAll(t_lst)
    lst
  }
  val connect4_controller:controller=(state,input,turn)=>{
    (null,null,null)
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
