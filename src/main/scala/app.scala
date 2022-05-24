import scalafx.application.JFXApp3
import scalafx.geometry.Insets
import scalafx.scene.Scene
import scalafx.scene.effect.DropShadow
import scalafx.scene.input.{DragEvent, MouseEvent}
import scalafx.scene.layout.{GridPane, HBox}
import scalafx.scene.paint.Color._
import scalafx.scene.paint._
import scalafx.scene.shape.Rectangle

import scala.::
import scala.language.postfixOps

object app extends JFXApp3 {
  def BOARD_WIDTH:Int = 600;
  type input
  type state = List[List[Char]]
  type drawable = List[Rectangle]
  type drawer = (state)=>drawable
  type controller = (state,input)=>state
  type clickHandler = (MouseEvent)=>input

  val xo_initial:state = List(List[Char](' ','x',' '),List[Char]('o',' ',' '),List[Char](' ',' ',' '))
  val xo_drawer:drawer = (x:state)=> {
    //  for(i<-Range(0,x.size)){
    //    for(j<-Range(0,x(0).size)){
    //      //...
    //    }
    //  }
    val CELLWIDTH = BOARD_WIDTH / 3;
    var lst: List[Rectangle]= List()
    for {
      i <- List(0, 1, 2)
      j <- List(0, 1, 2)
    } {
      val r = new Rectangle()
      r.layoutX = j * CELLWIDTH
      r.layoutY = i * CELLWIDTH
      r.width = CELLWIDTH
      r.height = CELLWIDTH

      r.fill = if (x(i)(j) == 'x') Red else if (x(i)(j) == 'o') Green else Black;
      lst = lst .:: (r)
    }
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