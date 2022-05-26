import definitions.{controller, drawer, input, state, turn}
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
object Chess extends JFXApp3{

  val Chess_drawer:drawer=(state:state)=>{
    //... prepare a list of nodes from state...
    //... return the list
  }
  val Chess_controller:controller=(state,input,turn)=>{
    //input form: "x0 y0 x1 y1 "
    // (0,0) is the top left square of the board as it's shown in the scene
    // (0,7) is the bottom left square


//      (0,0)--------- x increases ---------->
//        |             |
//        |             y
//        |             |
//        |-----x-----(x,y)
//   y increases
//        |
//        |
//       \ /

    //validate the move from (x0,y0) to (x1,y1)
    // ... perform move or return Invalid
    // return:
    // (state:new state or [null or old state if invalid] (whatever u like), status:[check status in definitions in xo.scala], String:[a message to show on screen(not necessary)])
  }

  override def start(): Unit = {
    stage = new JFXApp3.PrimaryStage {
      scene = new Scene(500, 500) {
        fill = Grey
        var a = 0;
        var b = 0;
        var board: List[Rectangle] = List()
        for( a <- 1 to 8){
          for( b <- 1 to 8){
          val rect = Rectangle(0+(50*a),0+(50*b),50,50)
            if(b%2==0) {
              if (a % 2 == 0) {
                rect.setFill(Color.Black)
              }
              else {
                rect.setFill(Color.White)
              }
            }
            else
              {
                if (a % 2 == 0) {
                  rect.setFill(Color.White)
                }
                else {
                  rect.setFill(Color.Black)
                }
              }
         board = board.appended(rect)
          content = board
          }
        }
      }
    }
  }
}